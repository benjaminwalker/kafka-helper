package codes.apophis.kafka.connect.api.dto.connector.plugin

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
data class ValidationDefinition(
    @JsonProperty("type") val type: String,
    @JsonProperty("required") val required: Boolean,
    @get:JsonProperty("default_value") @param:JsonProperty("default_value") val defaultValue: String,
    @JsonProperty("importance") val importance: String,
    @JsonProperty("documentation") val documentation: String,
    @JsonProperty("group") val group: String,
    @JsonProperty("width") val width: String,
    @get:JsonProperty("display_name") @param:JsonProperty("display_name") val displayName: String,
    @JsonProperty("dependents") val dependents: List<String>,
    @JsonProperty("order") val order: Int
)
