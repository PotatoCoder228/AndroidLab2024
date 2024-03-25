package plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.logging.toLogString
import io.ktor.utils.io.ByteChannel
import io.ktor.utils.io.copyTo
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel



val RequestTracePlugin = createApplicationPlugin(name = "RequestTracePlugin") {
    println("logging is installed!")
    onCallReceive  { call ->
        
    }
    onCall  { call ->
        var params = call.receiveText();
        println("Processing request: ${call.request.toLogString()}")

        if(params.isNotEmpty()) println("Params:\n${call.receiveText()}")
        println("")
    }
    onCallRespond { call, body ->
        println("Sending response, status: ${call.response.status()} : \n${body}")
        println("")
    }
  
}
//настройка сериализации
fun Application.configureLogging() {
    install(RequestTracePlugin);
}