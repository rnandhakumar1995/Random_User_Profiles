package io.nandha.userprofiles.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.nandha.userprofiles.Result
import io.nandha.userprofiles.model.Api
import io.nandha.userprofiles.model.db.CacheDb
import io.nandha.userprofiles.model.data.User

class DisplayActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val cacheDb by lazy { CacheDb.getInstance(application.applicationContext) }

    fun getUser(user: String): User {
        return cacheDb.cacheDao().getUser(user)
    }

    suspend fun loadWeather(api: Api, coordinate: String): Result {
        val coordinates = coordinate.split(",")
        val weatherResponse = api.loadWeather(coordinates[0], coordinates[1])
        if (weatherResponse.isSuccessful) {
            val body = weatherResponse.body()
            return if (body != null) Result.Success(body) else Result.Error("Empty response")
        }
        return Result.Error(weatherResponse.message())
    }
}