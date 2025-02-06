package ec.edu.epn.rq_driver.api

import ec.edu.epn.rq_driver.model.Ruta
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RutaApi {

  @POST("/api/routes")
  suspend fun createRoute(@Body route: Ruta): Ruta

  @GET("/api/routes")
  suspend fun getRoutes(): List<Ruta>
}
