package io.nandha.userprofiles.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey val email: String,
    val name: String,
    val address: String,
    val coordinate: String,
    val cell: String,
    val phone: String,
    val thumbnail: String,
    val picture: String,
    val dob: String,
    val age: Int
)

fun List<ApiUser>.mapToUser(): List<User> {
    val result = mutableListOf<User>()
    for (user in this) {
        result.add(user.run {
            User(
                email,
                "${name.title}.${name.first} ${name.last}",
                "${location.street.number}, ${location.street.name}, ${location.city}, ${location.state}, ${location.country} - ${location.postcode}",
                "${location.coordinates.latitude},${location.coordinates.longitude}",
                cell, phone, picture.thumbnail, picture.large, dob.date, dob.age
            )
        })
    }
    return result
}