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
    return this.map {
        User(
            it.email,
            "${it.name.title}.${it.name.first} ${it.name.last}",
            "${it.location.street.number}, ${it.location.street.name}, ${it.location.city}, ${it.location.state}, ${it.location.country} - ${it.location.postcode}",
            "${it.location.coordinates.latitude},${it.location.coordinates.longitude}",
            it.cell, it.phone, it.picture.thumbnail, it.picture.large, it.dob.date, it.dob.age
        )
    }
}