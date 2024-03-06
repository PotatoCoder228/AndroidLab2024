import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*

//import io.ktor.serialization.kotlinx.json.*
import io.ktor.serialization.jackson.*
import database.HouseDB
import model.House
fun main(args: Array<String>) {
    var houseDB = HouseDB();

    var testHouse = House(1,"ABOBBA",false, 1);

    embeddedServer(Netty, 8080) {
        install(CallLogging)
        install(ContentNegotiation){
          jackson()
        }
        /*/ {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }*/
        routing {
            get("/") {
                call.respondText("Hello, world!", ContentType.Text.Html)
            }


            get("house/{id}"){
                call.respond(testHouse)
            } 
        }
    }.start(wait = true)
}