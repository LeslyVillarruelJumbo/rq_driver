package ec.edu.epn.rq_driver.api

import ec.edu.epn.rq_driver.model.Ruta
import retrofit2.http.Body
import retrofit2.http.POST

interface RutaApi {
   @POST("/registrar")
    suspend fun registrarRuta(@Body ruta: Ruta )
}