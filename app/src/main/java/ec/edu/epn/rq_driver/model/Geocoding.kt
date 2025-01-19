package ec.edu.epn.rq_driver.model

data class GeocodingResponse(
    val results: List<GeocodingResult>
)

data class GeocodingResult(
    val geometry: Geometry
)

data class Geometry(
    val location: Location
)

data class Location(
    val lat: Double,
    val lng: Double
)

