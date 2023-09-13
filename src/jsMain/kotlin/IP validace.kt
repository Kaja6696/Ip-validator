import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    val server = embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            jackson {
            }
        }

        install(StatusPages) {
            exception<Throwable> { cause ->
                call.respond(HttpStatusCode.InternalServerError, cause.localizedMessage)
            }
        }

        routing {
            get("/v1/geoip") {
                val ip = call.request.queryParameters["ip"] ?: throw IllegalArgumentException("IP parameter is missing")
                if (!isValidIP(ip)) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid IP address format")
                } else {
                    val geoLocation = getGeoLocation(ip)
                    call.respond(geoLocation)
                }
            }

            get("/v1/reputation") {
                val ip = call.request.queryParameters["ip"] ?: throw IllegalArgumentException("IP parameter is missing")
                if (!isValidIP(ip)) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid IP address format")
                } else {
                    val reputationScore = calculateReputationScore(ip)
                    call.respond(mapOf("reputation_score" to reputationScore))
                }
            }

            get("/v1/health") {
                val healthStatus = checkHealth()
                call.respond(healthStatus)
            }
        }
    }
    server.start(wait = true)
}

fun isValidIP(ip: String): Boolean {
    val ipAddressRegex = """^([0-9]{1,3}\.){3}[0-9]{1,3}$""".toRegex()
    return ipAddressRegex.matches(ip)
}

fun getGeoLocation(ip: String): Map<String, String> {
    return mapOf(
        "country" to "Czech Republic",
        "city" to "Prague",
        "latitude" to "50.0755",
        "longitude" to "14.4378"
    )
}

fun calculateReputationScore(ip: String): Int {
    var score = 100

    if (isProxy(ip)) {
        score -= 50
    }

    if (isBlacklisted(ip)) {
        score -= 20
    }

    if (!isCzSk(ip)) {
        score -= 10
    }

    return score
}

fun isProxy(ip: String): Boolean {
    return false
}

fun isBlacklisted(ip: String): Boolean {
    return false
}

fun isCzSk(ip: String): Boolean {
    return true
}

fun checkHealth(): Map<String, String> {
    return mapOf("status" to "OK")
}

