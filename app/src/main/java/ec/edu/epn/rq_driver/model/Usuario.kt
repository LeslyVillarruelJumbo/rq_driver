package ec.edu.epn.rq_user.model

import java.time.LocalDate

data class Usuario(
  val firesbaseId: String,
  val nombre: String,
  val apellido: String,
  val fechaNacimiento: LocalDate,
  val cedula: String,
  val telefono: String,
  val email: String,
)