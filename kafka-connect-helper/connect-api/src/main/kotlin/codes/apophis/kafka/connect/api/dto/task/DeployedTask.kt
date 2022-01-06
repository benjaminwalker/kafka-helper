package codes.apophis.kafka.connect.api.dto.task

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * A task is deployed with a config
 */

@JsonIgnoreProperties(ignoreUnknown = true)
data class DeployedTask(
    @JsonProperty("task") val task: Task,
    @JsonProperty("config") val connectorConfig: Map<String,String>
)