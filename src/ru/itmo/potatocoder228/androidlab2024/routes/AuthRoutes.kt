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
import exceptions.*
fun Route.authRouting(userCollection: UserCollection, houseCollection: HouseCollection) {
    post("/login") {
               
        val userDto = call.receive<UserDTO>()
        val login = userDto.login
        val password = userDto.password
        //call.respond(login)
        userCollection.checkUser(User(login,password)) || throw IncorrectLoginOrPasswordException()
        val token = generateToken(login);
        call.response.status(HttpStatusCode.OK)
        call.respond(hashMapOf("token" to token));
    }

    post("/register") {
       
        val userDto = call.receive<UserDTO>()
        val login = userDto.login
        val password = userDto.password
        //call.respond(login)
        !userCollection.checkLogin(login) || throw AlreadyRegisteredException()
        userCollection.save(User(login, password))
        val user = userCollection.findByLogin(login);
        houseCollection.save(House(1,login,false, user.id))
        call.response.status(HttpStatusCode.OK)
        call.respondText("user ${login} registered");
    
    }

}