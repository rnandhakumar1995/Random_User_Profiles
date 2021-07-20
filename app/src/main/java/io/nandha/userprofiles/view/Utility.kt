package io.nandha.userprofiles.view

import android.view.View
import io.nandha.userprofiles.model.data.ApiUser
import io.nandha.userprofiles.model.data.User

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

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