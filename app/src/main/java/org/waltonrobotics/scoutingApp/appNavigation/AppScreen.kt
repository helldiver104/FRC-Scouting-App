package org.waltonrobotics.scoutingApp.appNavigation

sealed class AppScreen(val route: String) {
    object MainScreen : AppScreen("main_page")
    object ScoutingScreen : AppScreen("scouting_root")
    object PitScoutingForm : AppScreen("pit_form_page")
    object CycleTimeForm : AppScreen("cycle_time_page")
    object FAQScreen : AppScreen("faq_page")
    object ScheduleScreen : AppScreen("schedule_root")
    object AccountScreen : AppScreen("account_page")

    object LoginScreen : AppScreen("login")

    sealed class Scouting(route: String) : AppScreen(route) {
        object Start : Scouting("scout_nest_start")
        object Auton : Scouting("scout_nest_auton")
        object Teleop : Scouting("scout_nest_teleop")
        object ClosingComments : Scouting("scout_nest_comments")
    }

    sealed class Schedule(route: String) : AppScreen(route) {
        object ScheduleScreen : Schedule("sched_nest_main") // Unique
        object CompSchedule : Schedule("sched_nest_comp")
        object OurSchedule : Schedule("sched_nest_ours")
    }

    sealed class Pit(route: String) : AppScreen(route) {
        // FIXED: Inherit from Pit, not Schedule
        object PitScreen : Pit("pit_nest_main")
        object PitChecklist : Pit("pit_nest_checklist")
        object Sponsors : Pit("pit_nest_sponsors")
    }



}