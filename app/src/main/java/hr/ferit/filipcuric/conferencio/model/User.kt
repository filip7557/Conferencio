package hr.ferit.filipcuric.conferencio.model

data class User(
    var id: String? = null,
    var imageUrl: String = "https://static.vecteezy.com/system/resources/previews/005/544/718/non_2x/profile-icon-design-free-vector.jpg",
    val fullname: String = "",
    val company: String = "",
    val position: String = "",
    val email: String = "",
)
