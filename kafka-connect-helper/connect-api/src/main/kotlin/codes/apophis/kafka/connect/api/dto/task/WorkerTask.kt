package codes.apophis.kafka.connect.api.dto.task

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


/**
 * An instance of a task assigned to a worker, has a state
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class WorkerTask(
    @JsonProperty("state") val state: String,
    @JsonProperty("id") val id: Int,
    @get:JsonProperty("worker_id") @param:JsonProperty("worker_id") val workerId: String,
)
