package codes.apophis.kafka.connect.client

import arrow.core.Either
import arrow.core.computations.either
import arrow.core.rightIfNotNull
import com.google.api.client.util.ExponentialBackOff
import javax.ws.rs.BadRequestException
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status.Family.CLIENT_ERROR
import javax.ws.rs.core.Response.Status.Family.SERVER_ERROR
import java.util.concurrent.TimeUnit

interface JerseyResponseHandler : Logging {

    fun <U> invoke(request: () -> Response, filter: JerseyRetryFilter, paramClass: Class<U>) : Either<HttpError.ClientError, U> =
        either.runCatching {
        execWithBackoff(request, filter).fold({Either.Left(it)},{
            if(checkResponse(it)) {
                Either.Right(it.readEntity(paramClass))
            } else {
                Either.Left(handleError(it))
            }
        })
    }.getOrElse {
            Either.Left(HttpError.ClientError("Unable to deserialize response object", it))
    }



    fun invoke(request: () -> Response,
                    filter: JerseyRetryFilter) : Either<HttpError.ClientError, Response> =
        execWithBackoff(request, filter).fold({ Either.Left(it) },{
            if(!checkResponse(it)) { Either.Left(handleError(it)) } else { Either.Right(it) }
        })


    fun handleError(errorResponse: Response) : HttpError.ClientError {
        val errorMessage = errorResponse.readEntity(String::class.java)
        return when(errorResponse.statusInfo.toEnum()) {
            Response.Status.BAD_REQUEST -> {
                HttpError.ClientError(errorMessage, BadRequestException(errorMessage))
            }
            else -> HttpError.ClientError(errorMessage, WebApplicationException(errorMessage))
        }
    }

    fun execWithBackoff(request: () ->  Response, filter: JerseyRetryFilter) : Either<HttpError.ClientError, Response> {
        return either.runCatching {
            val backoff = ExponentialBackOff.Builder().setMaxElapsedTimeMillis(60000)
                .build()
            var delay = 0L
            var response: Response?
            var clientErrorRetry = false
            do {

                milliDelay(delay)
                response = request.invoke()
                val currentState: Response.StatusType = response.statusInfo

                when(currentState.family) {
                    CLIENT_ERROR -> {
                        logDebug("Client Error Detected, Current State $currentState")
                        if(filter.checkStatus(currentState.toEnum())) {
                            response.bufferEntity()
                            val message = response.readEntity(String::class.java)
                            logDebug("Client error message: $message")
                            if(filter.checkMessage(currentState.toEnum(), message)) {
                                logDebug("Retry Filter Matched ${currentState.toEnum()}, $message")
                                clientErrorRetry = true
                                logDebug("Retry Delay: $delay")
                            }
                        }
                    }
                    SERVER_ERROR -> {
                        logWarn("Server error $currentState, delaying $delay")
                    }
                    else -> {
                        clientErrorRetry = false
                    }
                }
                delay = backoff.nextBackOffMillis()

            } while (delay != ExponentialBackOff.STOP &&
                (response?.statusInfo?.family === SERVER_ERROR || clientErrorRetry))

            response.rightIfNotNull {
                HttpError.ClientError("Unable to retrieve a response") }
        }.getOrElse {
            Either.Left(HttpError.ClientError(it.message, it))
        }

    }

    /**
     * List of "Good" statuses
     */
    fun checkResponse(response: Response) : Boolean {
        return listOf(
            Response.Status.OK.statusCode,
            Response.Status.NO_CONTENT.statusCode,
            Response.Status.CREATED.statusCode,
            Response.Status.ACCEPTED.statusCode
        ).contains(response.status)
    }

    fun milliDelay(delay: Long) {
        try {
            TimeUnit.MILLISECONDS.sleep(delay)
        } catch (e: InterruptedException) {
            logError("Thread interrupted")
        }
    }

}