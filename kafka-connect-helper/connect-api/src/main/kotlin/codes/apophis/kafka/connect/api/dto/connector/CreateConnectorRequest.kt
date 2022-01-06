package codes.apophis.kafka.connect.api.dto.connector

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class CreateConnectorRequest(
    @JsonProperty("name") val name: String,
    @get:JsonProperty("config") @param:JsonProperty("config")val requestedConfig: Map<String, String>
)
