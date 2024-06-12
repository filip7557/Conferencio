package hr.ferit.filipcuric.conferencio.mock

import hr.ferit.filipcuric.conferencio.model.Conference
import hr.ferit.filipcuric.conferencio.model.User

fun getConferences() = listOf(
    Conference(
        id = "1",
        imageUrl = "https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F784276859%2F1352392056833%2F1%2Foriginal.20240606-111051?w=940&auto=format%2Ccompress&q=75&sharp=10&s=1aa5f4e7837ee73bd158c319936fd26d",
        title = "NowInMobile Meetup",
        startDateTime = 1718895600000,
        endDateTime = 1719000000000,
        ownerId = "SNbVzHkToCRgnDmZWfmddQZeAsy2"
    ),
    Conference(
        id = "2",
        imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSTgRO4qcSMER2GGFGeNqnqHPink32-c_H7Ng&s",
        title = "Dovik@FERIT",
        startDateTime = 1718913600000,
        endDateTime = 1719345600000,
        ownerId = "qgTZb0vTpddc7eI88l4PZNPqwn22"
    ),
    Conference(
        id = "3",
        imageUrl = "https://img.evbuc.com/https%3A%2F%2Fcdn.evbuc.com%2Fimages%2F784276859%2F1352392056833%2F1%2Foriginal.20240606-111051?w=940&auto=format%2Ccompress&q=75&sharp=10&s=1aa5f4e7837ee73bd158c319936fd26d",
        title = "NowInMobile Meetup",
        startDateTime = 1716616800000,
        endDateTime = 1717048800000,
        ownerId = "SNbVzHkToCRgnDmZWfmddQZeAsy2"
    ),
)

fun getUser() = User(
    fullname = "Filip Ćurić",
    company = "FERIT",
    position = "Student",
)
