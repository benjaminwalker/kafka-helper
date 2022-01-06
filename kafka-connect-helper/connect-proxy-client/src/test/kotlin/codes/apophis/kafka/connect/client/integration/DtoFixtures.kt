package codes.apophis.kafka.connect.client.integration

import com.google.common.io.Resources
import java.nio.charset.StandardCharsets

open class DtoFixtures {
    var DOCKER_FILE_SINK_NAME = "local-docker-file-sink"
    var DOCKER_FILE_SINK_TOPIC = "connect-test"

    var DOCKER_FILE_SINK: Map<String, String> = hashMapOf(
            "name" to DOCKER_FILE_SINK_NAME,
            "connector.class" to  "FileStreamSink",
            "tasks.max" to "1",
            "file" to "/tmp/test.sink.txt",
            "topics" to  DOCKER_FILE_SINK_TOPIC)

    fun read(filename: String) : String {
        return Resources.toString(Resources.getResource(filename), StandardCharsets.UTF_8)
    }
}