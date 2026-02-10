package org.waltonrobotics.scoutingApp.sheetsStuff

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MatchScoutingFormSheetsAPI {
    @POST("https://script.google.com/macros/s/AKfycbyovAajOP9a9AXRbArYFKsEjEqr78VGbKlyZr9B97Y_a5rqE7zQumEelX6-D7LpESVZ/exec")
    suspend fun submitMatchData(@Body data: MatchDataDTO): Response<Unit>
}