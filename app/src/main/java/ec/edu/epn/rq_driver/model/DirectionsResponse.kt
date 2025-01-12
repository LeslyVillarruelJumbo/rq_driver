package ec.edu.epn.rq_driver.model

data class DirectionsResponse(
    val routes: List<Route>
)

data class Route(
    val legs: List<Leg>
)

data class Leg(
    val steps: List<Step>
)

data class Step(
    val end_location: Location,
    val start_location: Location,
    val polyline: Polyline
)

data class Location(
    val lat: Double,
    val lng: Double
)

data class Polyline(
    val points: String
)

