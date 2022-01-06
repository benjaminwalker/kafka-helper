package codes.apophis.kafka.connect.api.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ClusterConfiguration(
    @JsonProperty("version") val version: String,
    @JsonProperty("commit") val commit: String,
    @JsonProperty("clusterId") val clusterId: String
)

