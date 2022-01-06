package codes.apophis.kafka.connect.client.integration

import codes.apophis.kafka.connect.client.ConnectClient
import codes.apophis.kafka.connect.client.Logging
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import java.io.File
import java.time.Duration
import codes.apophis.kafka.connect.client.ConnectClusterConfiguration.Companion.ConfigBuilder

abstract class KafkaConnectIntegration : Logging {
    var client: ConnectClient = generateClient()
    val dtoFixtures = DtoFixtures()

    @BeforeEach
    fun beforeEach() {
        client = generateClient()
    }

    private fun generateClient() : ConnectClient {
        ConfigBuilder().setConnectServerUri("http://localhost:8083").build().fold({
            throw it
        },{
            return ConnectClient.Companion.Builder(
                it).build()
        })
    }

    companion object {

        private val instance: KDockerComposeContainer by lazy { defineDockerCompose()}

        class KDockerComposeContainer(file: File) : DockerComposeContainer<KDockerComposeContainer>(file)

        private fun defineDockerCompose() = KDockerComposeContainer(File("src/test/resources/containers/docker-compose.yml"))
            .withExposedService("rest-proxy", 8082, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)))
            .withExposedService("connect", 8083, Wait.forLogMessage(".*Kafka Connect started.*\\n", 1))
            .withExposedService("schema-registry", 8081, Wait.forListeningPort())
            .withExposedService("broker", 9092, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)))
            .withExposedService("zookeeper", 2181, Wait.forListeningPort())
            .withLocalCompose(true)

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            instance.start()
        }

        @AfterAll
        @JvmStatic
        internal fun afterAll() {
            instance.stop()
        }

    }
}