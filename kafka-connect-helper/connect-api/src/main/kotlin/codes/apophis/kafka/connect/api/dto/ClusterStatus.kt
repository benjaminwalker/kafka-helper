package codes.apophis.kafka.connect.api.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ClusterStatus(
    @JsonProperty("config") val clusterConfig: ClusterConfiguration,
    @JsonProperty("status") val clusterStatus: String
)
