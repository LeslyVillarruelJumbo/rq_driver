package ec.edu.epn.rq_driver.api

import ec.edu.epn.rq_driver.model.GeocodingResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingService {
    @GET("maps/api/geocode/json")
    suspend fun getCoordinates(
        @Query("address") address: String,
        @Query("key") apiKey: String
    ): GeocodingResponse
}