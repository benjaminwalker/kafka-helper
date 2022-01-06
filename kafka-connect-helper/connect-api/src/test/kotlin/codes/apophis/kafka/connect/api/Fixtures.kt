package codes.apophis.kafka.connect.api

import codes.apophis.kafka.connect.api.dto.ClusterConfiguration
import codes.apophis.kafka.connect.api.dto.ClusterStatus
import codes.apophis.kafka.connect.api.dto.connector.CreateConnectorRequest
import codes.apophis.kafka.connect.api.dto.connector.DeployedConnector
import codes.apophis.kafka.connect.api.dto.connector.Status
import codes.apophis.kafka.connect.api.dto.connector.Worker
import codes.apophis.kafka.connect.api.dto.connector.plugin.*
import codes.apophis.kafka.connect.api.dto.task.DeployedTask
import codes.apophis.kafka.connect.api.dto.task.Task
import codes.apophis.kafka.connect.api.dto.task.WorkerTask
import java.nio.charset.StandardCharsets
import com.google.common.io.Resources

class Fixtures {
    val connectorName = "testConnectorName"
    val clusterConfiguration = ClusterConfiguration("1.0.0","zywxvu","abcdef")
    val clusterStatus = ClusterStatus(clusterConfiguration,"GREEN")
    val task0 = Task(connectorName,0)
    val task1 = Task(connectorName,1)
    val connectorConfiguration = mapOf("a" to "b")
    val deployedTask = DeployedTask(task0,connectorConfiguration)
    val workerTask = WorkerTask("RUNNING",0,"192.168.1.1")
    val createConnectorRequest = CreateConnectorRequest(connectorName,connectorConfiguration)
    val deployedConnector = DeployedConnector(connectorName,"s3Sink",connectorConfiguration, listOf(task0,task1))
    val worker = Worker("RUNNING","0")
    val status = Status(connectorName,"s3Sink",worker, listOf(workerTask))
    val plugin = Plugin("codes.apophis.SomeClass","s3Sink","1.0.0")
    val validationDefinition = ValidationDefinition("s3Sink",false,"defaultValue","high","something","aGroup","slim","S3Sink",
        listOf("none"),0)
    val validationValue = ValidationValue("aValue", listOf("something","nothing"), listOf("someError"),true)
    val validation = Validation(validationDefinition,validationValue)
    val validationResponse = ValidationResponse("aName", 0, listOf("aGroup"), listOf(validation))

    fun read(filename: String) : String {
        return Resources.toString(Resources.getResource(filename), StandardCharsets.UTF_8).replace(" ","")
    }

}