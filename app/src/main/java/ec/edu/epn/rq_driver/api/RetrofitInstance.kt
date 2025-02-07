package ec.edu.epn.rq_driver.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
  private const val BASE_URL = "http://192.168.100.19:8000"

  val api: UsuarioApi by lazy {
    Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(UsuarioApi::class.java)
  }
}