package codes.apophis.kafka.connect.client

import arrow.core.Either
import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider
import jakarta.ws.rs.client.Client
import jakarta.ws.rs.client.ClientBuilder
import jakarta.ws.rs.client.Entity
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.MultivaluedHashMap
import jakarta.ws.rs.core.MultivaluedMap
import jakarta.ws.rs.core.Response
import org.glassfish.jersey.client.ClientConfig
import java.net.URI
import java.time.Instant

open class JerseyProxyClient(
    val client: Client,
    val endpoint: URI,
    val oauth2Client: Oauth2Client?,
    val retryFilters: JerseyRetryFilter,
): Logging, JerseyResponseHandler {

    companion object {
        fun build(client: Client, endpoint: URI, retryFilters: JerseyRetryFilter, apiSecret: String, apiKey: String,
                  authenticationEndpoint: String) : Either<JerseyClientError, JerseyProxyClient> {
            val oauth2Client = Oauth2Client(apiKey, apiSecret, authenticationEndpoint)
            return oauth2Client.authenticate().fold({
                           Either.Left(it)
            },{
                Either.Right(JerseyProxyClient(client, endpoint, oauth2Client, retryFilters))
            })
        }

        fun build(client: Client, endpoint: URI, retryFilters: JerseyRetryFilter, ) : Either<JerseyClientError, JerseyProxyClient> {
            return Either.Right(JerseyProxyClient(client, endpoint, null, retryFilters))
        }

        class Oauth2Client(
            val apiKey: String,
            val apiSecret: String,
            val authenticationEndpoint: String
        ) : JerseyResponseHandler {

            fun executeAuth() : Response {
                return ClientBuilder.newClient(ClientConfig().register(JacksonJsonProvider()))
                        .target(authenticationEndpoint)
                        .request()
                        .accept(MediaType.APPLICATION_JSON)
                        .post(Entity.form(generateHeaders(apiKey, apiSecret)))
            }

            fun authenticate() : Either<JerseyClientError, OauthResponse> {
                return invoke(::executeAuth, object:JerseyRetryFilter{
                    override fun checkMessage(status: Response.Status, message: String): Boolean {
                        return false
                    }
                    override fun checkStatus(status: Response.Status): Boolean {
                        return false
                    }
                }, OauthResponse::class.java ).fold({
                    Either.Left(HttpError.OauthError(it.message, it))},{
                    Either.Right(it) })
            }

            private fun generateHeaders(apiKey: String, apiSecret: String) : MultivaluedMap<String, String> {
                val map = MultivaluedHashMap<String,String>()
                map.add("grant_type","client_credentials")
                map.add("client_id", apiKey)
                map.add("client_secret", apiSecret)
                return map
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        data class OauthResponse(
            @get:JsonGetter("token_type") @param:JsonSetter("token_type") val tokenType: String,
            @get:JsonGetter("access_token") @param:JsonSetter("access_token") val accessToken: String,
            @get:JsonGetter("application_name") @param:JsonSetter("application_name") val applicationName: String?,
            @get:JsonGetter("expires_in") @param:JsonSetter("expires_in") val expiresIn: Int,
            @get:JsonGetter("refresh_count") @param:JsonSetter("refresh_count")  val refreshCount: Int?,
            @get:JsonGetter("refresh_token_expires_in") @param:JsonSetter("refresh_token_expires_in") val refreshTokenExpiresIn: Int?,
            @get:JsonGetter("issued_at") @param:JsonSetter("issued_at") val issuedAt: Long = Instant.now().toEpochMilli()
        ) {
            fun isActive(): Boolean {
                return Instant.now().isAfter(Instant.ofEpochMilli(issuedAt + expiresIn.toLong()))
            }

        }
    }
}