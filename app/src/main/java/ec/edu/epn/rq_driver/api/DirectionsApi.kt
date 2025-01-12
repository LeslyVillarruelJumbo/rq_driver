package ec.edu.epn.rq_driver.api

import ec.edu.epn.rq_driver.model.DirectionsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionsApi {
    @GET("directions/json")
    fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String
    ): Call<DirectionsResponse>
}
