package codes.apophis.kafka.connect.api.dto.connector

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


/**
 * An instance of a worker that is "tasked"
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class Worker(
    @JsonProperty("state") val state: String,
    @get:JsonProperty("worker_id") @param:JsonProperty("worker_id") val workerId: String
)