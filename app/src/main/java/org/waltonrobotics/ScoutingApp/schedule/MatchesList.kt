package org.waltonrobotics.ScoutingApp.schedule

class MatchesList {
    private var matchesList: MutableList<Match> = mutableListOf()

    fun addMatch(item: Match) {
        matchesList.add(item)
    }

    fun removeMatch(index: Int) {
        matchesList.removeAt(index)
    }

    fun returnLength(): Int {
        return matchesList.size
    }

    fun getList(): List<Match>{
        return matchesList
    }

}