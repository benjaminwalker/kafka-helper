package codes.apophis.kafka.connect.client

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

interface Logging {
    fun <T : Logging> T.logger(): Logger = LogManager.getLogger(javaClass)

    fun logInfo(s: String) {
        logger().info(s)
    }

    fun log(s: String) {
        logger().info(s)
    }

    fun logDebug(s: String) {
        logger().warn(s)
    }

    fun logWarn(s: String) {
        logger().warn(s)
    }

    fun logError(s: String) {
        logger().warn(s)
    }
}