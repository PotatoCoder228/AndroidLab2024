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
fun Route.authRouting(userCollection: UserCollection, houseCollection: HouseCollection) {
    post("/login") {
               
        val userDto = call.receive<UserDTO>()
        val login = userDto.login
        val password = userDto.password
        //call.respond(login)
        if (userCollection.checkUser(User(login,password))) {
         
            val token = generateToken(login);

            call.respond(hashMapOf("token" to token));
            /*call.respondText(
                //Json.encodeToString(hashMapOf("token" to token)),
                ContentType.Application.Json
            )*/
        } else {
            call.response.status(HttpStatusCode.Unauthorized)
            call.respond(hashMapOf("error" to "Wrong login or password"))
            /*call.respondText(
                //Json.encodeToString(hashMapOf("error" to "Wrong login or password")),
                ContentType.Application.Json
            )*/
        }
    }

    post("/register") {
       
        val userDto = call.receive<UserDTO>()
        val login = userDto.login
        val password = userDto.password
        //call.respond(login)
        if(userCollection.save(User(login, password)))  {
            val user = userCollection.findByLogin(login);

            houseCollection.save(House(1,login,false, user!!.id))
            call.respond(HttpStatusCode.OK) 
        }
        else call.respond(HttpStatusCode.BadRequest)
    }

}