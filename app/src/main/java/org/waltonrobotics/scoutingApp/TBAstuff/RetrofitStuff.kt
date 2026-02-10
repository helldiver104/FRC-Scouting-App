package org.waltonrobotics.scoutingApp.TBAstuff

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.waltonrobotics.scoutingApp.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://www.thebluealliance.com/api/v3/"
    private const val TBA_API_KEY = BuildConfig.TBA_API_KEY

    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val requestWithHeaders = originalRequest.newBuilder()
            .header("X-TBA-Auth-Key", TBA_API_KEY)
            .build()
        chain.proceed(requestWithHeaders)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Attach our custom client
            .addConverterFactory(GsonConverterFactory.create()) // Handles JSON parsing
            .build()
    }

    val tbaApi: TbaApi by lazy {
        retrofit.create(TbaApi::class.java)
    }
}