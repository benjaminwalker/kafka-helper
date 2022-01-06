package codes.apophis.kafka.connect.api.dto.connector.plugin

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ValidationResponse(
    @JsonProperty("name") val name: String,
    @get:JsonProperty("error_count") @param:JsonProperty("error_count") val errorCount: Int,
    @JsonProperty("groups") val groups: List<String>,
    @JsonProperty("configs") val configs: List<Validation>
)