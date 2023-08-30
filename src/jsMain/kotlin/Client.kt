import web.dom.document
import react.create
import react.dom.client.createRoot

fun isValidIP(ip: String): Boolean {
    val ipAddressRegex = """^([0-9]{1,3}\.){3}[0-9]{1,3}$""".toRegex()
    return ipAddressRegex.matches(ip)
}
}