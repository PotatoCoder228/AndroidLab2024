package plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.doublereceive.*
//настройка сериализации
fun Application.configureDoubleReceive() {
    install(DoubleReceive)
}