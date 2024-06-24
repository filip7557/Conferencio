package hr.ferit.filipcuric.conferencio.model

data class Comment(
    val isEventComment: Boolean = false,
    val eventId: String = "",
    val userId: String = "",
    val timeStamp: Long = 0,
)
