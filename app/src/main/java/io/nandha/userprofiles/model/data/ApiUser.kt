package io.nandha.userprofiles.model.data

data class ApiUser(
    val email: String,
    val name: Name,
    val dob: Dob,
    val picture: Picture,
    val phone: String,
    val cell: String,
    val location: Location,
)

data class Picture(val large: String, val thumbnail: String)
data class Dob(val date: String, val age: Int)
data class Name(val title: String, val first: String, val last: String)
data class Location(
    val city: String,
    val state: String,
    val country: String,
    val postcode: String,
    val coordinates: Coordinate,
    val street: Street
)

data class Coordinate(val latitude: String, val longitude: String)
data class Street(val number: Long, val name: String)