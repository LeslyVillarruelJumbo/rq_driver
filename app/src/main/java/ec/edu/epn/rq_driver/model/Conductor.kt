package ec.edu.epn.rq_driver.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Conductor(
  @SerializedName("firebaseId")
  val firesbaseId: String,
  @SerializedName("firstName")
  val nombre: String,
  @SerializedName("lastName")
  val apellido: String,
  @SerializedName("birthDate")
  val fechaNacimiento: Date,
  @SerializedName("idNumber")
  val cedula: String,
  @SerializedName("phone")
  val telefono: String,
  @SerializedName("email")
  val email: String,
  @SerializedName("carId")
  val autoId: String? = null,
  @SerializedName("travelNumber")
  val numViajes: Int,
  @SerializedName("photoURI")
  val fotoPerfil: String?
)
