package org.waltonrobotics.scoutingApp.sheetsStuff

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object MatchScoutingRetrofitClient {
    private val scoutingOkHttpClient = OkHttpClient.Builder()
        .followRedirects(true)
        .followSslRedirects(true)
        .connectTimeout(15, TimeUnit.SECONDS)
        .build()

    val scoutingApi: MatchScoutingFormSheetsAPI by lazy {
        Retrofit.Builder()
            .baseUrl("https://script.google.com/macros/s/")
            .client(scoutingOkHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create()) // Handles plain text/HTML
            .addConverterFactory(GsonConverterFactory.create())    // Handles your MatchScoutingState JSON
            .build()
            .create(MatchScoutingFormSheetsAPI::class.java)
    }
}