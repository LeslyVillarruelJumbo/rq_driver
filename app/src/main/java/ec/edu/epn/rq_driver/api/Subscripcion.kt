package ec.edu.epn.rq_driver.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Subscripcion {

  @POST("/api/subs")
  suspend fun createSubscription(@Body subscription: Subscripcion): Subscripcion

  @GET("/api/subs")
  suspend fun getSubscriptions(): List<Subscripcion>
}
