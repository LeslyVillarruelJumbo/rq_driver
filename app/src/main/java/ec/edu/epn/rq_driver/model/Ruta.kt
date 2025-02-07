package ec.edu.epn.rq_driver.model

data class Ruta (
  val latitudInicial: Number,
  val latitudFinal: Number,
  val longitudInicial: Number,
  val longitudFinal: Number,
  val horaPartida: String,
  val conductorID: String,
  val estadoRuta: Boolean,
  val nombreRuta: String,
  val puntoInicial: String,
  val puntoFinal: String,
  val asientosDisponibles: Number,
)
