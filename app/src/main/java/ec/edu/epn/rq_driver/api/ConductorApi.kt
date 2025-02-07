package ec.edu.epn.rq_driver.api

import ec.edu.epn.rq_driver.model.Auto
import ec.edu.epn.rq_driver.model.Conductor
import ec.edu.epn.rq_driver.model.Ruta
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ConductorApi {

  @POST("/api/drivers")
  suspend fun createDriver(@Body driver: Conductor): Conductor

  @GET("/api/drivers")
  suspend fun getDrivers(): List<Conductor>

  @GET("/api/drivers/{id}/profile")
  suspend fun getDriverProfile(@Path("id") driverId: String): Conductor

  @GET("/api/drivers/{id}/routes")
  suspend fun getDriverRoutes(@Path("id") driverId: String): List<Ruta>

  @GET("/api/drivers/{id}/car")
  suspend fun getDriverCar(@Path("id") driverId: String): Auto

  @PUT("/api/drivers/{id}")
  suspend fun updateDriver(@Path("id") driverId: String, @Body driver: Conductor): Conductor

}
