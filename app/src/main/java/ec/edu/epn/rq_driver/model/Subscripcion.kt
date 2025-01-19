package ec.edu.epn.rq_user.model

import java.util.Date

data class Subscripcion (
  val usuarioId: String,
  val rutaId: String,
  val fechaSub: Date,
  val lugarRecogida: String,
)