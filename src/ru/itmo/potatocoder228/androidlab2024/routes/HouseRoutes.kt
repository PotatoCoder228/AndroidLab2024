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
        call.respondText("Hi, $login! Token is expired at $expiresAt ms.")
    }
    get("/hello") {
        call.respondText("Hello, world!", ContentType.Text.Html)
    }


    get{
        val principal = call.principal<JWTPrincipal>()
        val login = principal!!.payload.getClaim("login").asString()
        val user = userCollection.findByLogin(login)?: return@get call.respond(hashMapOf("error" to "No such user"));
        val house = houseCollection.findByUserId(user.id);
        call.respond(house);

    } 

}