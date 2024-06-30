package hr.ferit.filipcuric.conferencio.model

data class Event(
    val conferenceId: String = "",
    var id: String? = "",
    val title: String = "",
    val dateTime: Long = 0,
    val location: String = "",
    val duration: Int = 0,
    val conferenceOwnerId: String = "",
    val hostId: String = "",
    val type: String = "",
    val description: String = "",
)
