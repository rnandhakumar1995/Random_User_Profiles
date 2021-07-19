package io.nandha.userprofiles.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(@PrimaryKey val email: String)
