package plugins

import database.interfaces.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import routes.*
/***
 * Конфигурация маршрутизации
 */
fun Application.configureRouting(userCollection: UserCollection, houseCollection: HouseCollection) {

    routing {
        get("/") {
            call.respondText("our super cool and unfinished http api!")
        }
        route("/api/auth") {
            authRouting(userCollection, houseCollection)
        }
        authenticate("HouseAuth") {
            route("/api/house") {
                houseRouting(userCollection, houseCollection)
            }
        }
    }
}