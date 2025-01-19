package ec.edu.epn.rq_driver.model

data class Ruta (
  val puntoInicio: String,
  val puntoFinal: String,
  val horaPartida: String,
  val conductorID: String,
  val estadoRuta: Boolean,
)
