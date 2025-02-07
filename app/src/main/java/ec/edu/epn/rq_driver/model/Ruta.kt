package ec.edu.epn.rq_driver.model

import com.google.gson.annotations.SerializedName

data class Ruta (
  @SerializedName("startLat")
  val latitudInicial: Number,
  @SerializedName("finalLat")
  val latitudFinal: Number,
  @SerializedName("startLong")
  val longitudInicial: Number,
  @SerializedName("finalLong")
  val longitudFinal: Number,
  @SerializedName("departureTime")
  val horaPartida: String,
  @SerializedName("driverId")
  val conductorID: String,
  @SerializedName("routeId")
  val rutaID: String?,
  @SerializedName("routeName")
  val nombreRuta: String,
  @SerializedName("availableSeats")
  val asientosDisponibles: Number,
  @SerializedName("routeState")
  val estadoRuta: Boolean,
  @SerializedName("startPoint")
  val puntoInicial: String,
  @SerializedName("finalPoint")
  val puntoFinal: String,

)
