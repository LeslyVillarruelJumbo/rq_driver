package ec.edu.epn.rq_driver.api

import ec.edu.epn.rq_driver.model.Auto
import ec.edu.epn.rq_driver.model.Conductor
import ec.edu.epn.rq_driver.model.Ruta
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

private const val BASE_ENDPOINT_URL = "/api/drivers"

interface ConductorApi {

    @POST("$BASE_ENDPOINT_URL/")
    suspend fun crearNuevoUsuario(@Body driver: Conductor): Response<Conductor>

    @GET("$BASE_ENDPOINT_URL/")
    suspend fun obtenerUsuarios(): List<Conductor>

    @GET("$BASE_ENDPOINT_URL/{email}")
    suspend fun obtenerUsuarioPorCorreo(@Path("email") email: String): Conductor?

    @PUT("$BASE_ENDPOINT_URL/{id}")
    suspend fun actualizarConductor(@Path("id") id: String, @Body driver: Conductor): Response<Conductor>

    @GET("$BASE_ENDPOINT_URL/{id}")
    suspend fun obtenerUsuarioPorId(@Body id: String): Conductor?

    @GET("$BASE_ENDPOINT_URL/{id}/profile")
    suspend fun obtenerPerfilDelConductor(@Path("id") id: String): Conductor?

    @GET("$BASE_ENDPOINT_URL/{id}/routes")
    suspend fun obtenerLasRutasDelConductor(@Path("id") id: String): List<Ruta>

    @GET("$BASE_ENDPOINT_URL/{id}/car")
    suspend fun obtenerAutoDelConductor(@Path("id") id: String): Auto?

}
