package ec.edu.epn.rq_driver.api

import ec.edu.epn.rq_driver.model.Usuario
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsuarioApi {

  @POST("/api/users")
  suspend fun createUser(@Body user: Usuario): Usuario

  @GET("/api/users")
  suspend fun getUsers(): List<Usuario>

  @GET("/api/users/{id}/profile")
  suspend fun getUserProfile(@Path("id") userId: String): Usuario

  @PUT("/api/users/{id}")
  suspend fun updateUser(@Path("id") userId: String, @Body user: Usuario): Usuario
}
