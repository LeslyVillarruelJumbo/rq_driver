package ec.edu.epn.rq_driver.api

import ec.edu.epn.rq_driver.model.Usuario
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Subscripcion {

  @POST("/api/subs")
  suspend fun createSubscription(@Body subscription: Subscripcion): Subscripcion

  @GET("/api/subs")
  suspend fun getSubscriptions(): List<Subscripcion>

  @GET("/api/subs/route/{routeId}")
  suspend fun getUsersByRoute(@Path("routeId") routeId: String): List<Usuario>
}
