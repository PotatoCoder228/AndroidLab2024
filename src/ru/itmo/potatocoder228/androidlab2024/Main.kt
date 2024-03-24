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
import database.*
import plugins.*
import config.*

fun main() {
    val env = applicationEngineEnvironment {
        envConfig()
    }
    embeddedServer(Netty, env).start(true)
}

fun ApplicationEngineEnvironmentBuilder.envConfig() {
    module {
        module()
    }
    connector {
        port = Config.port
        host = Config.host
    }
}

fun Application.module() {
    var houseDB = HouseDB();
    var userDB = UserDB();
    configureDoubleReceive()
    configureJwtAuth()
    configureRouting(userDB,houseDB)
    configureLogging()
    
    
    configureSerialization()
}