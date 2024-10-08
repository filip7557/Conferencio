package hr.ferit.filipcuric.conferencio.model

data class Conference(
    var id: String? = null,
    val imageUrl: String = "https://firebasestorage.googleapis.com/v0/b/conferencio-57027.appspot.com/o/conference_banners%2Fdefault_banner.jpg?alt=media&token=8cb443b7-cf23-4aae-885b-520808c31cdd",
    val title: String = "",
    val startDateTime: Long = 0,
    val endDateTime: Long = 0,
    val ownerId: String = "",
)
