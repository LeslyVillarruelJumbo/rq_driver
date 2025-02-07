package ec.edu.epn.rq_driver.api

import ec.edu.epn.rq_driver.model.Conductor
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

private const val BASE_ENDPOINT_URL = "/api/drivers"

interface ApiDeUsuarios {

    @GET("$BASE_ENDPOINT_URL/")
    suspend fun obtenerUsuarios(): List<Conductor>

    @GET("$BASE_ENDPOINT_URL/{email}")
    suspend fun obtenerUsuarioPorCorreo(@retrofit2.http.Path("email") email: String): Conductor?

    @GET("$BASE_ENDPOINT_URL/id")
    suspend fun obtenerUsuarioPorId(@Body id: String): Conductor?

    @POST("$BASE_ENDPOINT_URL/")
    suspend fun crearNuevoUsuario(@Body driver: Conductor): Response<Conductor>

}
