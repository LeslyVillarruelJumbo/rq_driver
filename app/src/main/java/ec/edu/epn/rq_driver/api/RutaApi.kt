package ec.edu.epn.rq_driver.api

import ec.edu.epn.rq_driver.model.Ruta
import ec.edu.epn.rq_driver.model.Usuario
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RutaApi {

    @GET("api/routes")
    suspend fun obtenerRutas(@Query("driverId") driverId: String): List<Ruta>

    @POST("api/routes")
    suspend fun crearRuta(@Body ruta: Ruta): Ruta

    @GET("api/route/{routeId}")
    suspend fun usuariosRuta(@Path("routeId") routeId: String): List<Usuario>

}
