package hr.ferit.filipcuric.conferencio.model

data class Event(
    val conferenceId: String = "",
    val id: String? = "",
    val title: String = "",
    val dateTime: Long = 0,
    val location: String = "",
    val duration: String = "",
    val hostId: String = "",
    val type: String = "",
    val description: String = "",
)
