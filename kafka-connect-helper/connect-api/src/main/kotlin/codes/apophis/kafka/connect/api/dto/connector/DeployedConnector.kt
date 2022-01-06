package codes.apophis.kafka.connect.api.dto.connector

import codes.apophis.kafka.connect.api.dto.task.Task
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * A connector is deployed with a config and set of tasks
 */

@JsonIgnoreProperties(ignoreUnknown = true)
data class DeployedConnector(
    @JsonProperty("name") val name: String,
    @JsonProperty("type") val type: String,
    @JsonProperty("config") val config: Map<String,String>,
    @JsonProperty("tasks") val tasks: List<Task>
)