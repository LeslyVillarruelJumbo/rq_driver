package ec.edu.epn.rq_driver.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://192.168.100.130:3000"

    val rutaApi: RutaApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RutaApi::class.java)
    }
    //val conductorApi: ApiDeUsuarios = retrofitBuilder.create(ApiDeUsuarios::class.java)

    val conductorApi: ApiDeUsuarios by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiDeUsuarios::class.java)
    }

  val api: UsuarioApi by lazy {
    Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(UsuarioApi::class.java)
  }
}