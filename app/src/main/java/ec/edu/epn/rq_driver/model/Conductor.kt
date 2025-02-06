package ec.edu.epn.rq_driver.model

import java.util.Date

data class Conductor(
  val firesbaseId: String,
  val nombre: String,
  val apellido: String,
  val fechaNacimiento: Date,
  val cedula: String,
  val telefono: String,
  val email: String,
  val autoId: String? = null,
  val numViajes: Int,
)
