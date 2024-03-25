package routes;

import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.request.* 
import io.ktor.server.engine.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*

//import io.ktor.serialization.kotlinx.json.*
import io.ktor.serialization.jackson.*

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.application.Application

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm


import database.interfaces.*
import model.*
import java.util.Date
import dto.UserDTO
import plugins.generateToken
fun Route.houseRouting(userCollection: UserCollection, houseCollection: HouseCollection) {
    get("/token/info") {
        val principal = call.principal<JWTPrincipal>()
        val login = principal!!.payload.getClaim("login").asString()
        val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
        call.response.status(HttpStatusCode.OK)
        call.respondText("Hi, $login! Token is expired at $expiresAt ms.")
    }
    get("/hello") {
        call.respondText("Hello, world!", ContentType.Text.Html)
    }

    //TODO common code for auth and getting house
    get{
        val user = userCollection.findByLogin(call.getUserLogin());
        
        val house = houseCollection.findByUserId(user.id);
        call.response.status(HttpStatusCode.OK)
        call.respond(house);

    }

    post{
        val user = userCollection.findByLogin(call.getUserLogin());
        var house = houseCollection.findByUserId(user.id);
        
        var newHouse = call.receive<HouseDTO>()
        house.setFromDTO(newHouse)
        houseCollection.update(house)
        call.response.status(HttpStatusCode.OK)
        call.respondText("house ${house.id} updated");

    }
    

}

fun ApplicationCall.getUserLogin(): String {
    val principal = principal<JWTPrincipal>()
    return principal!!.payload.getClaim("login").asString()
}