package codes.apophis.kafka.connect.api.dto.connector.plugin

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
data class Plugin(
    @get:JsonProperty("class_name") @param:JsonProperty("class_name")  val className: String,
    @JsonProperty("type") val type: String,
    @JsonProperty("version") val version: String
)