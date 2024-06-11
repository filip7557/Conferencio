package hr.ferit.filipcuric.conferencio.model

data class User(
    var id: String? = null,
    var imageUrl: String = "https://firebasestorage.googleapis.com/v0/b/conferencio-57027.appspot.com/o/profile_pictures%2Fdefault_profile.jpg?alt=media&token=f955d74f-47c1-4199-aba9-fb3366070b67",
    val fullname: String = "",
    val company: String = "",
    val position: String = "",
    val email: String = "",
)
