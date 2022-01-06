package codes.apophis.kafka.connect.api.dto.connector.plugin

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Validation(
    @JsonProperty("definition") val definition: ValidationDefinition,
    @JsonProperty("value") val value: ValidationValue
)
