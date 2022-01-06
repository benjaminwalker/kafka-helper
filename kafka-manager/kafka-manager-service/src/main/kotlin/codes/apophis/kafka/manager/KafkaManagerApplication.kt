package codes.apophis.kafka.manager

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class KafkaManagerApplication {

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(KafkaManagerApplication::class.java, *args)
        }
    }
}

