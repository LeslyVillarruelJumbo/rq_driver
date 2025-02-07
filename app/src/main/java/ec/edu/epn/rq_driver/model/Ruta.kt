package ec.edu.epn.rq_driver.model

data class Ruta (
  val departureTime: String,
  val driverId: String,
  val routeState: Boolean,
  val startLong: Number,
  val finalLong: Number,
  val startLat: Number,
  val finalLat: Number,
  val routeName: String,
  val startPoint: String,
  val finalPoint: String,
  val availableSeats: Number
)
