package hr.ferit.filipcuric.conferencio.model

data class ChatMessage(
    val isEventChat: Boolean = false,
    val eventId: String = "",
    val userId: String = "",
    val timeStamp: Long = 0,
    val message: String = "",
)
