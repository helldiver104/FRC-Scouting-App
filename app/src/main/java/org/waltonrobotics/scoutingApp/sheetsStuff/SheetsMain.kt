package org.waltonrobotics.scoutingApp.sheetsStuff

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MatchScoutingFormSheetsAPI {
    @POST("https://script.google.com/macros/s/AKfycbxkzi1XfKIhIi0DAArP2l8l2iCvMcBuELkVqjRjeKkfkhstSrD8JM4_JV19Jhw9twX-/exec")
    suspend fun submitMatchData(@Body data: MatchDataDTO): Response<Unit>

}


interface WaltScheduleSheetsAPI {
    @GET("https://script.google.com/macros/s/AKfycbxkzi1XfKIhIi0DAArP2l8l2iCvMcBuELkVqjRjeKkfkhstSrD8JM4_JV19Jhw9twX-/exec")
    suspend fun getAssignments(): List<Map<String, String>>
}