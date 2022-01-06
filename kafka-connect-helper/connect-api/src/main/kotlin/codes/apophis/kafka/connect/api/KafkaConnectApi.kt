package codes.apophis.kafka.connect.api

import codes.apophis.kafka.connect.api.dto.ClusterConfiguration
import codes.apophis.kafka.connect.api.dto.ClusterStatus
import codes.apophis.kafka.connect.api.dto.connector.CreateConnectorRequest
import codes.apophis.kafka.connect.api.dto.connector.DeployedConnector
import codes.apophis.kafka.connect.api.dto.connector.Status
import codes.apophis.kafka.connect.api.dto.connector.plugin.Plugin
import codes.apophis.kafka.connect.api.dto.connector.plugin.ValidationResponse
import codes.apophis.kafka.connect.api.dto.task.DeployedTask
import codes.apophis.kafka.connect.api.dto.task.WorkerTask

interface KafkaConnectApi {

    // Cluster
    fun getCluster(): ClusterConfiguration

    fun getClusterStatus(): ClusterStatus

    // Connectors
    fun getConnectors(): List<String>

    fun getConnector(name: String?): DeployedConnector

    fun getDeployedConfig(name: String): Map<String, String>

    fun createConnector(createConnectorRequest: CreateConnectorRequest): DeployedConnector

    fun updateConnector(desiredConfig: Map<String, String>): DeployedConnector

    fun deleteConnector(name: String)

    fun getConnectorStatus(name: String): Status

    fun pauseConnector(name: String)

    fun resumeConnector(name: String)

    fun restartConnector(name: String)

    // Tasks
    fun getDeployedTasks(connectorName: String): List<DeployedTask>

    fun getWorkerTasks(connectorName: String, taskId: Int): WorkerTask

    fun restartTask(connectorName: String, taskId: Int)

    fun restartAllTasks()
//
//    // Topics
//
//    // TODO: Not implemented
//    // GET /connectors/(string:name)/topics
//
//    // TODO: Not implemented
//    // PUT /connectors/(string:name)/topics/reset
//
//    // Topics
//    // TODO: Not implemented
//    // GET /connectors/(string:name)/topics
//    // TODO: Not implemented
//    // PUT /connectors/(string:name)/topics/reset

    fun getPlugins(): List<Plugin>

    fun validatePlugin(name: String, config: Map<String, String>): ValidationResponse

    companion object {
            var basePath = "/"
            var connectorsPath = "connectors/"
            var connectorPluginPath = "connector-plugins/"
            var tasksPath = "/tasks/"
    }
}