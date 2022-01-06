package codes.apophis.kafka.connect.client

import jakarta.ws.rs.core.Response

interface JerseyRetryFilter {

    fun checkStatus(status: Response.Status) : Boolean

    fun checkMessage(status: Response.Status, message: String) : Boolean
}