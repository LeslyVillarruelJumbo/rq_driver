package ec.edu.epn.rq_driver.api

import ec.edu.epn.rq_driver.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://${BuildConfig.SERVER_URL}:${BuildConfig.SERVER_PORT}"
    private val retrofitBuilder: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiDeUsuarios: ApiDeUsuarios = retrofitBuilder.create(ApiDeUsuarios::class.java)

}