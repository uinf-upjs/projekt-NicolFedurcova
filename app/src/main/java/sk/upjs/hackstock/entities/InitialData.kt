package sk.upjs.hackstock.entities

data class InitialData(
    val users: List<User>,
    val shares: List<Share>,
    val activities: List<Activity>,
    val questions: List<Question>
)