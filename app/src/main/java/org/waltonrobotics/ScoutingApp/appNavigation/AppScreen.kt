package org.waltonrobotics.ScoutingApp.appNavigation

sealed class AppScreen(val route: String) {
    object MainScreen : AppScreen("MainScreen")
    object ScoutingScreen : AppScreen("ScoutingScreen")
    object PitScoutingForm : AppScreen("PitScoutingForm")
    object CycleTimeForm : AppScreen("CycleTimeForm")
    object FAQScreen : AppScreen("FAQScreen")
    object ScheduleScreen : AppScreen("ScheduleScreen")
    object AccountScreen : AppScreen("AccountScreen")

    // SCOUTING SCREENS
    sealed class Scouting(route: String) : AppScreen(route) {
        object Start : Scouting("ScoutingScreenStart")
        object Auton : Scouting("ScoutingScreenAuton")
        object Teleop : Scouting("ScoutingScreenTeleop")
        object ClosingComments : Scouting("ScoutingScreenClosingComments")
    }
    // SCHEDULE SCREENS
    sealed class Schedule(route: String) : AppScreen(route) {
        object ScheduleScreen : Schedule("ScheduleScreen")
        object CompSchedule : Schedule("CompSchedule")
        object OurSchedule : Schedule("OurSchedule")
    }

    // PIT STUFF (idk i have some time so why not?)
    sealed class Pit(route: String) : AppScreen(route) {
        object PitScreen : Schedule("PitScreen")
        object PitChecklist : Schedule("PitChecklist")
        object Sponsors : Schedule("Sponsors")
    }

}