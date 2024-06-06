package hr.ferit.filipcuric.conferencio.model

data class User(
    var id: String? = null,
    val fullname: String = "",
    val company: String = "",
    val position: String = "",
    val email: String = "",
)
