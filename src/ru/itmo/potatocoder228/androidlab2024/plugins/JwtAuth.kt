package plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm


import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.request.* 
import config.*
import java.util.Date
import database.interfaces.*
//настройка сериализации
fun Application.configureJwtAuth(userService : UserCollection) {
    install(Authentication) {
        jwt("HouseAuth") {
            realm = Config.myRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(Config.secret))
                    .withAudience(Config.audience)
                    .withIssuer(Config.issuer)
                    .build()
            )
            validate { credential ->
                val login = credential.payload.getClaim("login").asString();

                if (userService.checkLogin(login)) {
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
}

fun generateToken(login: String): String{
    return JWT.create()
                .withAudience(Config.audience)
                .withIssuer(Config.issuer)
                .withClaim("login", login)
                .withExpiresAt(Date(System.currentTimeMillis() + 24 * 60 * 60000))
                .sign(Algorithm.HMAC256(Config.secret))
}