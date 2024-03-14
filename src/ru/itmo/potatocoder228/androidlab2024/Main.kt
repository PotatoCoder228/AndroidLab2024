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

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm


import database.HouseDB
import database.UserDB
import model.*
import java.util.Date
import dto.UserDTO
const val secret = "secret"
const val issuer = "http://localhost:8080/"
const val audience = "http://localhost:8080/page"
const val myRealm = "Access to 'page'"

fun main(args: Array<String>) {
    var houseDB = HouseDB();
    var userDB = UserDB();
    var testHouse = House(1,"ABOBBA",false, 1);

    embeddedServer(Netty, 8080) {
        install(CallLogging)
        install(ContentNegotiation){
          jackson()
        }
        install(Authentication) {
            jwt("HouseAuth") {
                realm = myRealm
                verifier(
                    JWT
                        .require(Algorithm.HMAC256(secret))
                        .withAudience(audience)
                        .withIssuer(issuer)
                        .build()
                )
                validate { credential ->
                    if (credential.payload.getClaim("login").asString() != "") {
                        JWTPrincipal(credential.payload)
                    } else {
                        null
                    }
                }
                challenge { defaultScheme, realm ->
                    call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
                }
            }
        }
        /*/ {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }*/
        /*routing {
            get("/") {
                call.respondText("Hello, world!", ContentType.Text.Html)
            }


            get("house/{id}"){
                call.respond(testHouse)
            } 
        }*/

        routing {
            post("/login") {
               
                val userDto = call.receive<UserDTO>()
                val login = userDto.login
                val password = userDto.password
                //call.respond(login)
                if (userDB.checkUser(User(login,password))) {
                    val token = JWT.create()
                        .withAudience(audience)
                        .withIssuer(issuer)
                        .withClaim("login", login)
                        .withExpiresAt(Date(System.currentTimeMillis() + 24 * 60 * 60000))
                        .sign(Algorithm.HMAC256(secret))

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
                if(userDB.save(User(login, password)))  call.respond(HttpStatusCode.OK) 
                else call.respond(HttpStatusCode.BadRequest)
            }

            authenticate("HouseAuth") {
                get("/token/info") {
                    val principal = call.principal<JWTPrincipal>()
                    val login = principal!!.payload.getClaim("login").asString()
                    val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                    call.respondText("Hi, $login! Token is expired at $expiresAt ms.")
                }
                get("/") {
                    call.respondText("Hello, world!", ContentType.Text.Html)
                }
    
    
                get("house/{id}"){
                    call.respond(testHouse)
                } 
            }
        }
    }.start(wait = true)
}