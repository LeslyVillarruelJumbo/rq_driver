package ec.edu.epn.rq_driver.api

import ec.edu.epn.rq_driver.model.Auto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AutoApi {

  @POST("/api/cars")
  suspend fun createCar(@Body car: Auto): Auto

  @GET("/api/cars")
  suspend fun getCars(): List<Auto>
}
