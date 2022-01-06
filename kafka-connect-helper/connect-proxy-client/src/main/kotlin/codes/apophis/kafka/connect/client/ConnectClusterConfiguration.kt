package codes.apophis.kafka.connect.client

import arrow.core.Either
import arrow.core.computations.either
import com.google.common.io.Resources
import jakarta.ws.rs.core.UriBuilder
import java.io.FileNotFoundException
import java.net.URI
import java.util.*

/**
 * Currently only has an endpoint URI for the kafka connect cluster as a property
 */
data class ConnectClusterConfiguration(
    val endPoint: URI
) {
    companion object {
        /**
         * Can use the ConfigBuilder to load a particular environment properties file
         * in the format of (%s.kafka.connect.properties) where %s is whatever you named it
         * OR
         * Pass a full resource location in from the class path
         * OR
         * TODO (pass in file path to load outside of classpath
         * OR
         * Just set the endpoint URI for the kafka connect cluster directly
         *
         */
        class ConfigBuilder() : Logging {
            var props: Properties = Properties()
            var endPoint: URI? = null

            fun fromEnvironmentConfig(environment: String) : Either<ConfigError, ConfigBuilder> =
                loadPropertiesFileByEnv(environment).fold({ Either.Left(it) },{
                    this.props = it
                    Either.Right(this) })

            fun fromFile(fileLocation: String) : Either<ConfigError, ConfigBuilder> =
                loadPropertiesFile(fileLocation).fold({ Either.Left(it) },{
                    this.props = it
                    Either.Right(this) })


            fun setConnectServerUri(uri: String) : ConfigBuilder  {
                this.endPoint = UriBuilder.fromUri(uri).build()
                return this
            }

            fun build() : Either<ConfigError, ConnectClusterConfiguration> {
                return either.runCatching {
                    if(endPoint == null) {
                        endPoint = setConnectServerUri(props.getProperty("CONNECT_CLUSTER_URI")).endPoint
                    }
                    Either.Right(ConnectClusterConfiguration(endPoint!!))
                }.getOrElse {
                    Either.Left(ConfigError.ConfigurationLoadError(it.message, it))
                }
            }


            /**
             * Methods to leverage Google IO Resources to load properties file from classpath
             */

            private fun loadPropertiesFileByEnv(env: String?): Either<ConfigError, Properties> {
                val fileLoc = String.format("config/%s.kafka.connect.properties", env)
                return loadPropertiesFile(fileLoc)
            }

            private fun loadPropertiesFile(fileLocation: String) : Either<ConfigError, Properties> {
                return either.runCatching {
                    val properties = Properties()
                    logDebug("Loading Config from $fileLocation")
                    properties.load(Resources.getResource(fileLocation).openStream())
                    Either.Right(properties)
                }.getOrElse {
                    when(it.cause) {
                        is FileNotFoundException -> {
                            logError("Properties file %s not found in classpath: $fileLocation")
                        }
                        else -> {
                            logError("No Resource Properties File Found")
                        }
                    }
                    Either.Left(ConfigError.ConfigurationLoadError(it.message, it))
                }
            }
        }

    }
}
