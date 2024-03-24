package plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.*
import io.ktor.server.routing.*
//настройка страницы с ошибками
fun Application.configureErrorHandling() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.response.status(HttpStatusCode.BadRequest)
            call.respond(hashMapOf("error" to cause.message))
        }
    }
}