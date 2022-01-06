package codes.apophis.kafka.connect.api.dto.connector

import codes.apophis.kafka.connect.api.dto.task.WorkerTask
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * The overall status of a connector, has a Worker and the set of Tasks assigned to the worker
 */

@JsonIgnoreProperties(ignoreUnknown = true)
data class Status(
    @JsonProperty("name") val name: String,
    @JsonProperty("type") val type: String,
    @JsonProperty("connector") val connector: Worker,
    @JsonProperty("tasks") val tasks: List<WorkerTask>
)