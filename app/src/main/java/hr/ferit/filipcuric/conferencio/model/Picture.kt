package hr.ferit.filipcuric.conferencio.model

data class Picture(
    val conferenceId: String = "",
    val imageUrl: String = "",
    val timestamp: Long = 0,
    var id: String = ""
)
