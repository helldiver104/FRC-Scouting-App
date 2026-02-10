package org.waltonrobotics.scoutingApp.TBAstuff

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path

interface TbaApi {
    @GET("event/{eventKey}/matches")
    suspend fun getMatches(
        @Path("eventKey") eventKey: String
    ): List<TBAMatchResponse>
}

data class TBAMatchResponse(
    @SerializedName("match_number") val matchNumber: Int,
    @SerializedName("comp_level") val compLevel: String,
    @SerializedName("set_number") val setNumber: Int,
    val alliances: Alliances
)

data class Alliances(
    val red: AllianceDetail, val blue: AllianceDetail
)

data class AllianceDetail(
    @SerializedName("team_keys") val teamKeys: List<String>?
)