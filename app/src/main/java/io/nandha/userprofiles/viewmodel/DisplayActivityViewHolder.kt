package io.nandha.userprofiles.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.nandha.userprofiles.model.Api
import io.nandha.userprofiles.model.db.CacheDb
import io.nandha.userprofiles.model.data.User
import io.nandha.userprofiles.model.data.WeatherResponse
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.launch

class DisplayActivityViewHolder(application: Application) : AndroidViewModel(application) {
    private val cacheDb by lazy { CacheDb.getInstance(application.applicationContext) }
    val channel = ConflatedBroadcastChannel<WeatherResponse>()

    fun getUser(user: String): User {
        return cacheDb.cacheDao().getUser(user)
    }

    fun loadWeather(api: Api, coordinate: String) {
        val coordinates = coordinate.split(",")
        viewModelScope.launch {
            val weatherResponse = api.loadWeather(coordinates[0], coordinates[1])
            channel.send(weatherResponse)
        }
    }
}