package codes.apophis.kafka.connect.client.integration

import codes.apophis.kafka.connect.api.dto.connector.CreateConnectorRequest
import codes.apophis.kafka.connect.client.Logging
import jakarta.ws.rs.NotFoundException
import jakarta.ws.rs.WebApplicationException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ClientIntegrationTests() : KafkaConnectIntegration(), Logging {


    @Test
    @DisplayName("Testing Creation of File Sink Connector")
    fun testFileSinkCreate() {
        try {
            client.deleteConnector(dtoFixtures.DOCKER_FILE_SINK_NAME)
            logInfo("Deleting Connector, Ready to setup")
        } catch (ex: NotFoundException) {
            logInfo("Ready to setup")
        }

        val expected = client.createConnector(CreateConnectorRequest( dtoFixtures.DOCKER_FILE_SINK_NAME, dtoFixtures.DOCKER_FILE_SINK))
        val actual = client.getConnector(dtoFixtures.DOCKER_FILE_SINK_NAME)
        Assertions.assertEquals(expected.config, actual.config)

    }

    @Test
    @DisplayName("Testing the Update of a File Sink Connector")
    fun testFileSinkUpdate() {
        val updatedTasksMax = "2"

        try {
            logInfo("Checking for connector")
            client.getConnector(dtoFixtures.DOCKER_FILE_SINK_NAME)
        } catch (ex: NotFoundException) {
            logInfo("Creating connector")
            client.createConnector(CreateConnectorRequest(dtoFixtures.DOCKER_FILE_SINK_NAME,dtoFixtures.DOCKER_FILE_SINK))
            logInfo("Waiting for connector to start")
            Thread.sleep(5000)
        }
        val originalTaskMax = client.getConnector(dtoFixtures.DOCKER_FILE_SINK_NAME).config["tasks.max"]
        Assertions.assertTrue(originalTaskMax != updatedTasksMax)

        val updatedConfig = mutableMapOf<String,String>()
        updatedConfig.putAll(dtoFixtures.DOCKER_FILE_SINK)
        updatedConfig.put("tasks.max",updatedTasksMax)
        val actual = client.updateConnector(updatedConfig)
        logInfo("Waiting for connector state to stabilize")
        Assertions.assertEquals(updatedTasksMax, actual.config["tasks.max"])
        // delete this since we dirtied the config
        client.deleteConnector(dtoFixtures.DOCKER_FILE_SINK_NAME)
    }

    @Test
    @DisplayName("Testing the Pausing / Resuming of connector")
    fun testPauseResumeConnector() {
        try {
            logInfo("Checking for connector")
            client.getConnector(dtoFixtures.DOCKER_FILE_SINK_NAME)
        } catch (ex: WebApplicationException) {
            logInfo("Creating connector")
            client.createConnector(CreateConnectorRequest(dtoFixtures.DOCKER_FILE_SINK_NAME,dtoFixtures.DOCKER_FILE_SINK))
            logInfo("Waiting for connector to start")
            Thread.sleep(5000)
        }
        val currentState = client.getConnectorStatus(dtoFixtures.DOCKER_FILE_SINK_NAME).connector.state
        Assertions.assertEquals("RUNNING",currentState)
        client.pauseConnector(dtoFixtures.DOCKER_FILE_SINK_NAME)
        logInfo("Waiting for connector state to stabilize")
        Thread.sleep(5000)
        val pausedState = client.getConnectorStatus(dtoFixtures.DOCKER_FILE_SINK_NAME).connector.state
        Assertions.assertEquals("PAUSED", pausedState)
        client.resumeConnector(dtoFixtures.DOCKER_FILE_SINK_NAME)
    }

    @Test
    @DisplayName("Testing Connector Restart Endpoint")
    fun testRestartConnector() {
        try {
            logInfo("Checking for connector")
            client.getConnector(dtoFixtures.DOCKER_FILE_SINK_NAME)
        } catch (ex: WebApplicationException) {
            logInfo("Creating connector")
            client.createConnector(CreateConnectorRequest(dtoFixtures.DOCKER_FILE_SINK_NAME,dtoFixtures.DOCKER_FILE_SINK))
            logInfo("Waiting for connector to start")
            Thread.sleep(5000)
        }

        try {
            client.restartConnector(dtoFixtures.DOCKER_FILE_SINK_NAME)
            Assertions.assertTrue(true)
        } catch (ex: Exception) {
            Assertions.fail()
        }
    }

    @Test
    @DisplayName("Testing Get Deployed Configuration Endpoint")
    fun testGetDeployedConfig() {
        try {
            logInfo("Checking for connector")
            client.getConnector(dtoFixtures.DOCKER_FILE_SINK_NAME)
        } catch (ex: WebApplicationException) {
            logInfo("Creating connector")
            client.createConnector(CreateConnectorRequest(dtoFixtures.DOCKER_FILE_SINK_NAME,dtoFixtures.DOCKER_FILE_SINK))
            logInfo("Waiting for connector to start")
            Thread.sleep(5000)
        }

        val actual = client.getDeployedConfig(dtoFixtures.DOCKER_FILE_SINK_NAME)
        testMapCompare(dtoFixtures.DOCKER_FILE_SINK, actual)
    }


    private fun testMapCompare(expectedMap: Map<String,String>, actualMap: Map<String,String> ) {

        expectedMap.forEach{ (k, v) -> Assertions.assertTrue(actualMap.containsKey(k)); Assertions.assertEquals(v, actualMap[k]) }
    }



}