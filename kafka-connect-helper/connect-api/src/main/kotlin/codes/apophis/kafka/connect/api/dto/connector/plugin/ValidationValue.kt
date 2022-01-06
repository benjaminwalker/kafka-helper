package codes.apophis.kafka.connect.api.dto.connector.plugin

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ValidationValue(
    @JsonProperty("name") val name: String,
    @get:JsonProperty("recommended_values") @param:JsonProperty("recommended_values") val recommendedValues: List<String>,
    @JsonProperty("errors") val errors: List<String>,
    @JsonProperty("visible") val visible: Boolean
)