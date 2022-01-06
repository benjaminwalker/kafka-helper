package codes.apophis.kafka.connect.api.dto.task

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * A Task gets assigned to n+ workers
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class Task(
    @JsonProperty("connector") val connector: String,
    @JsonProperty("task") val task: Int
)

