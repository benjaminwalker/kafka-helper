package codes.apophis.kafka.connect.client

sealed class JerseyClientError(message: String? = null, cause: Throwable? = null) :
    Throwable(message, cause) {
    override fun toString(): String =
        // use direct message or cause message or error class name
        message ?: cause?.message ?: "${this::class.simpleName} (message unavailable)"
}

sealed class HttpError(message: String? = null, cause: Throwable? = null) : JerseyClientError(message, cause) {
    class OauthError(message: String? = null, cause: Throwable? = null) : HttpError(message, cause)
    class ClientError(message: String? = null, cause: Throwable? = null) : HttpError(message, cause)
}

sealed class ConfigError(message: String? = null, cause: Throwable? = null) : JerseyClientError(message, cause) {
    class ConfigurationLoadError(message: String? = null, cause: Throwable? = null) : ConfigError(message, cause)
}

