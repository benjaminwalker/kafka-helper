package codes.apophis.kafka.connect.client

import codes.apophis.kafka.connect.api.KafkaConnectApi
import codes.apophis.kafka.connect.api.dto.ClusterConfiguration
import codes.apophis.kafka.connect.api.dto.ClusterStatus
import codes.apophis.kafka.connect.api.dto.connector.CreateConnectorRequest
import codes.apophis.kafka.connect.api.dto.connector.DeployedConnector
import codes.apophis.kafka.connect.api.dto.connector.Status
import codes.apophis.kafka.connect.api.dto.connector.plugin.Plugin
import codes.apophis.kafka.connect.api.dto.connector.plugin.ValidationResponse
import codes.apophis.kafka.connect.api.dto.task.DeployedTask
import codes.apophis.kafka.connect.api.dto.task.WorkerTask
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider
import jakarta.ws.rs.client.Client
import jakarta.ws.rs.client.ClientBuilder
import jakarta.ws.rs.client.Entity
import jakarta.ws.rs.core.GenericType
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

import org.glassfish.jersey.client.ClientConfig

import java.net.URI
import java.util.concurrent.atomic.AtomicReference

class ConnectClient(
    client: Client,
    endpoint: URI,
    oauth2Client: JerseyProxyClient.Companion.Oauth2Client?,
    retryFilters: JerseyRetryFilter,
) : KafkaConnectApi, JerseyProxyClient(client, endpoint, oauth2Client, retryFilters), Logging {


    override fun getCluster(): ClusterConfiguration {
        invoke({ client.target(endpoint).path(KafkaConnectApi.basePath).request().accept(MediaType.APPLICATION_JSON).get() },
            retryFilters, ClusterConfiguration::class.java).fold({throw it.cause!!},{ return it} )
    }

    override fun getClusterStatus(): ClusterStatus {
        TODO("Not yet implemented")
    }

    override fun getConnectors(): List<String> {
        invoke({ client.target(endpoint)
            .path(KafkaConnectApi.basePath)
            .path(KafkaConnectApi.connectorsPath)
            .request()
            .accept(MediaType.APPLICATION_JSON)
            .get()}, retryFilters).fold({ throw it.cause!!}, {
                if(checkResponse(it)) {
                    return it.readEntity(object :GenericType<List<String>>(){})
                } else {
                    throw handleError(it)
                }
        })
    }

    override fun getConnector(name: String?): DeployedConnector {
        invoke({ client.target(endpoint)
            .path(KafkaConnectApi.basePath)
            .path(KafkaConnectApi.connectorsPath)
            .path(name)
            .request()
            .accept(MediaType.APPLICATION_JSON).get()},
            retryFilters,
            DeployedConnector::class.java).fold(
            { throw it.cause!! },{ return it }
            )
    }


    override fun getDeployedConfig(name: String): Map<String, String> {
        invoke({ client.target(endpoint)
            .path(KafkaConnectApi.basePath)
            .path(KafkaConnectApi.connectorsPath)
            .path(name)
            .path("/config")
            .request()
            .accept(MediaType.APPLICATION_JSON)
            .get()
        },retryFilters)
            .fold({ throw it.cause!!},
                {
                    if(checkResponse(it)) {
                        return it.readEntity(object :GenericType<Map<String,String>>(){})
                    } else {
                        throw handleError(it)
                    }
                })
    }

    override fun createConnector(createConnectorRequest: CreateConnectorRequest): DeployedConnector {
        invoke({ client.target(endpoint)
            .path(KafkaConnectApi.basePath)
            .path(KafkaConnectApi.connectorsPath)
            .request()
            .accept(MediaType.APPLICATION_JSON)
            .post(Entity.entity(createConnectorRequest,MediaType.APPLICATION_JSON_TYPE))
               },retryFilters, DeployedConnector::class.java)
            .fold({ throw it.cause!!},
                { return it })
    }

    override fun updateConnector(desiredConfig: Map<String, String>): DeployedConnector {
        invoke({ client.target(endpoint)
            .path(KafkaConnectApi.basePath)
            .path(KafkaConnectApi.connectorsPath)
            .path(desiredConfig["name"])
            .path("/config")
            .request()
            .accept(MediaType.APPLICATION_JSON)
            .put(Entity.entity(desiredConfig,MediaType.APPLICATION_JSON_TYPE))
        },retryFilters,DeployedConnector::class.java)
            .fold({ throw it.cause!!},
                { return it })
    }

    override fun deleteConnector(name: String) {
        invoke({ client.target(endpoint)
            .path(KafkaConnectApi.basePath)
            .path(KafkaConnectApi.connectorsPath)
            .path(name)
            .request()
            .delete()
               },retryFilters)
    }

    override fun getConnectorStatus(name: String): Status {
         invoke({ client.target(endpoint)
            .path(KafkaConnectApi.basePath)
            .path(KafkaConnectApi.connectorsPath).path(name).path("/status")
            .request().accept(MediaType.APPLICATION_JSON).get()},
            retryFilters,Status::class.java).fold({ throw it.cause!! },{ return it})
    }

    override fun pauseConnector(name: String) {
        manageConnectorStatus(name, "pause")
    }

    override fun resumeConnector(name: String) {
        manageConnectorStatus(name, "resume")
    }

    override fun restartConnector(name: String) {
        manageConnectorStatus(name, "restart")
    }

    private fun manageConnectorStatus(name: String, action: String) {
        invoke({client.target(endpoint)
            .path(KafkaConnectApi.basePath)
            .path(KafkaConnectApi.connectorsPath)
            .path(name)
            .path("/$action")
            .request().put(Entity.json(""))}, retryFilters)
    }


    override fun getDeployedTasks(connectorName: String): List<DeployedTask> {
        invoke({ client.target(endpoint)
            .path(KafkaConnectApi.basePath)
            .path(connectorName)
            .path(KafkaConnectApi.tasksPath)
            .request()
            .accept(MediaType.APPLICATION_JSON).get()
               },retryFilters).fold({ throw it.cause!!},{
                if(checkResponse(it)) {
                    return it.readEntity(object: GenericType<List<DeployedTask>>(){})
                } else {
                    throw handleError(it)
                }
        })
    }

    override fun getWorkerTasks(connectorName: String, taskId: Int): WorkerTask {
        invoke({ client.target(endpoint).path(KafkaConnectApi.basePath)
            .path(KafkaConnectApi.connectorsPath)
            .path(connectorName)
            .path(KafkaConnectApi.tasksPath)
            .path(taskId.toString())
            .path("/status")
            .request()
            .accept(MediaType.APPLICATION_JSON)
            .get()}, retryFilters, WorkerTask::class.java)
            .fold({ throw it.cause!!},
            {return it})
    }


    override fun restartTask(connectorName: String, taskId: Int) {
        invoke({ client.target(endpoint).path(KafkaConnectApi.basePath)
            .path(KafkaConnectApi.connectorsPath)
            .path(connectorName)
            .path(KafkaConnectApi.tasksPath)
            .path(taskId.toString())
            .path("/restart")
            .request().put(Entity.json(""))}, retryFilters)
    }

    override fun restartAllTasks() {
        getConnectors().map { connector ->
            getDeployedTasks(connector).map { restartTask(connector, it.task.task) }
        }
    }

    override fun getPlugins(): List<Plugin> {
         invoke({ client.target(endpoint)
            .path(KafkaConnectApi.basePath)
            .path(KafkaConnectApi.connectorPluginPath)
            .request().accept(MediaType.APPLICATION_JSON)
            .get()},retryFilters).fold({ throw it.cause!!}, {
                if(checkResponse(it)) {
                    return it.readEntity(object: GenericType<List<Plugin>>(){})
                } else {
                    throw handleError(it)
                }
            })
    }


    override fun validatePlugin(name: String, config: Map<String, String>): ValidationResponse {
        invoke({client.target(endpoint)
            .path(KafkaConnectApi.basePath)
            .path(KafkaConnectApi.connectorPluginPath)
            .path(name)
            .path("config/validate/")
            .request().accept(MediaType.APPLICATION_JSON)
            .put(Entity.entity(config, MediaType.APPLICATION_JSON_TYPE))}, retryFilters, ValidationResponse::class.java)
            .fold({throw it.cause!!},{ return it})
    }

    companion object {
        class Builder(configuration: ConnectClusterConfiguration) {
            private val endpoint: URI = configuration.endPoint

            fun build() : ConnectClient {
                return ConnectClient(
                    endpoint = endpoint,
                    client = ClientBuilder.newClient(
                        ClientConfig()
                            .register(JacksonJsonProvider())
//                            .register((ClientRequestFilter) requestContext ->{})
                    ),
                    oauth2Client = null,
                    retryFilters = KafkaConnectRetryFilters(listOf(
                        hashMapOf(Response.Status.CONFLICT to "{\"error_code\":409,\"message\":\"Cannot complete request momentarily due to stale configuration (typically caused by a concurrent config change)\"}")
                    ))

                )
            }

        }
        /*
                        .register((ClientRequestFilter) requestContext -> {
                    getHeaders().forEach((key, value) -> requestContext.getHeaders().add(key, value.get(0)));
                })
         */

        /*
        private MultivaluedMap<String,Object> getHeaders() throws WebApplicationException {
        try {
            if (this.oauthTokenResponse == null || !this.oauthTokenResponse.isActive()) {
                this.oauthTokenResponse = oauth2Client.authenticate();
            }
            MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + this.oauthTokenResponse.getAccessToken());
            headers.add("x-api-key", oauth2Client.getApiKey());
            return headers;
        } catch (WebApplicationException ex) {
            throw ex;
        }
    }
         */

        class KafkaConnectRetryFilters(
            private val filters : List<Map<Response.Status, String>>,
            private val clientErrorRetry: AtomicReference<Boolean> = AtomicReference(false)


        ) : JerseyRetryFilter, Logging {
            override fun checkStatus(status: Response.Status): Boolean {
                logDebug("Checking Filter status: $status")
                return filters.flatMap { it.keys }.toSet().contains(status)
            }

            override fun checkMessage(status: Response.Status, message: String): Boolean {
                filters.forEach{
                    run {
                        if(!it[status].isNullOrEmpty() && it[status]!! == message) {
                            logDebug("Filter Match Found: $status $message")
                            clientErrorRetry.set(true)
                        }
                    }
                }
                return clientErrorRetry.get()
            }

        }
    }
}