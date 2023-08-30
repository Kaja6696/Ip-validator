import khttp.get

val apiKey = "49c766f0c8249f6b969e2a09746fff1c"
val ipAddress = "192.168.0.110" // IP adresa, pro kterou chcete získat geolokaci

val response = get("http://api.ipstack.com/$ipAddress?access_key=$apiKey")

if (response.statusCode == 200) {
    val data = response.jsonObject
    val country = data.getString("country_name")
    val city = data.getString("city")
    val latitude = data.getDouble("latitude")
    val longitude = data.getDouble("longitude")

    println("Country: $country")
    println("City: $city")
    println("Latitude: $latitude")
    println("Longitude: $longitude")
} else {
    println("Error: ${response.statusCode}")
}
