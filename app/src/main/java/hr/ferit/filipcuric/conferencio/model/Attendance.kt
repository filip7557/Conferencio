package hr.ferit.filipcuric.conferencio.model

data class Attendance(
    val event: Boolean = false,
    val userId: String = "",
    val conferenceId: String = "",
)
