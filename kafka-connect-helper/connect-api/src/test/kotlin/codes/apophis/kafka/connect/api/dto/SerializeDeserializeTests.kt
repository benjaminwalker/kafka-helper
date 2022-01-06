package codes.apophis.kafka.connect.api.dto

import codes.apophis.kafka.connect.api.Fixtures
import com.fasterxml.jackson.core.PrettyPrinter
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SerializeDeserializeTests {
    val mapper = ObjectMapper()
    val fixtures = Fixtures()

    @Test
    @DisplayName("Test ClusterConfiguration Serialize / Deserialize")
    fun testClusterConfiguration() {
        val actual = mapper.writeValueAsString(fixtures.clusterConfiguration)
        Assertions.assertEquals(fixtures.read("fixtures/clusterConfiguration.json"),actual)
    }

    @Test
    @DisplayName("Test ClusterStatus Serialize / Deserialize")
    fun testClusterStatus() {
        val actual = mapper.writeValueAsString(fixtures.clusterStatus)
        Assertions.assertEquals(fixtures.read("fixtures/clusterStatus.json"),actual)
    }

    @Test
    @DisplayName("Test DeployedTask Serialize / Deserialize")
    fun testDeployedTask() {
        val actual = mapper.writeValueAsString(fixtures.deployedTask)
        Assertions.assertEquals(fixtures.read("fixtures/task/deployedTask.json"),actual)
    }

    @Test
    @DisplayName("Test Task Serialize / Deserialize")
    fun testTask() {
        val actual = mapper.writeValueAsString(fixtures.task0)
        Assertions.assertEquals(fixtures.read("fixtures/task/task.json"),actual)
    }

    @Test
    @DisplayName("Test WorkerTask Serialize / Deserialize")
    fun testWorkerTask() {
        val actual = mapper.writeValueAsString(fixtures.workerTask)
        Assertions.assertEquals(fixtures.read("fixtures/task/workerTask.json"),actual)
    }

    @Test
    @DisplayName("Test CreateConnectorRequest Serialize / Deserialize")
    fun testCreateConnectorRequest() {
        val actual = mapper.writeValueAsString(fixtures.createConnectorRequest)
        Assertions.assertEquals(fixtures.read("fixtures/connector/createConnectorRequest.json"),actual)
    }

    @Test
    @DisplayName("Test DeployedConnector Serialize / Deserialize")
    fun testDeployedConnector() {
        val actual = mapper.writeValueAsString(fixtures.deployedConnector)
        Assertions.assertEquals(fixtures.read("fixtures/connector/deployedConnector.json"),actual)
    }

    @Test
    @DisplayName("Test Status Serialize / Deserialize")
    fun testStatus() {
        val actual = mapper.writeValueAsString(fixtures.status)
        Assertions.assertEquals(fixtures.read("fixtures/connector/status.json"),actual)
    }

    @Test
    @DisplayName("Test Worker Serialize / Deserialize")
    fun testWorker() {
        val actual = mapper.writeValueAsString(fixtures.worker)
        Assertions.assertEquals(fixtures.read("fixtures/connector/worker.json"),actual)
    }

    @Test
    @DisplayName("Test Plugin Serialize / Deserialize")
    fun testPlugin() {
        val actual = mapper.writeValueAsString(fixtures.plugin)
        Assertions.assertEquals(fixtures.read("fixtures/connector/plugin/plugin.json"),actual)
    }

    @Test
    @DisplayName("Test Validation Definition Serialize / Deserialize")
    fun testValidationDefinition() {
        val actual = mapper.writeValueAsString(fixtures.validationDefinition)
        Assertions.assertEquals(fixtures.read("fixtures/connector/plugin/validationDefinition.json"),actual)
    }

    @Test
    @DisplayName("Test Validation Value Serialize / Deserialize")
    fun testValidationValue() {
        val actual = mapper.writeValueAsString(fixtures.validationValue)
        Assertions.assertEquals(fixtures.read("fixtures/connector/plugin/validationValue.json"),actual)
    }

    @Test
    @DisplayName("Test Validation Serialize / Deserialize")
    fun testValidation() {
        val actual = mapper.writeValueAsString(fixtures.validation)
        Assertions.assertEquals(fixtures.read("fixtures/connector/plugin/validation.json"),actual)
    }

    @Test
    @DisplayName("Test ValidationResponse Serialize / Deserialize")
    fun testValidationResponse() {
        val actual = mapper.writeValueAsString(fixtures.validationResponse)
        Assertions.assertEquals(fixtures.read("fixtures/connector/plugin/validationResponse.json"),actual)
    }

}