package plugins

import io.ktor.server.plugins.contentnegotiation.*

import io.ktor.serialization.jackson.*
import io.ktor.server.application.*

//настройка сериализации
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        jackson()
    }
}