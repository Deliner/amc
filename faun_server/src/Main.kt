package com.deliner

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.http.cio.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.routing.get
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet

const val HOST = "192.168.1.72"
const val PORT = 2378


val markerSet = HashSet<Marker>()

fun main() {
    val server = embeddedServer(Netty, port = PORT, host = HOST) {
        routing {
            get("/add_marker_{data}") {
                println(call.parameters.toString())
                val birdName = call.parameters["data"]!!.split("_")[0]
                val place = call.parameters["data"]!!.split("_")[1]
                markerSet.add(Marker(birdName, place))
                call.respond(HttpStatusCode.OK)
                println(markerSet)
                log("Added maker $birdName $place\n")
            }

            get("/get_markers") {
                val text = if (markerSet.isEmpty()) "" else markerSet.joinToString("\n")
                call.respondText(text)
                log("Request on markers list")
            }
        }
    }
    server.start(wait = true)
}


data class Marker(val birdName: String, val place: String) {
    override fun toString(): String {
        return "${birdName}_$place"
    }
}

fun log(tag: String) {
    val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    val date = Date()
    println("${dateFormat.format(date)}    $tag")
}
















