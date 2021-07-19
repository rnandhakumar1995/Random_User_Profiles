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
