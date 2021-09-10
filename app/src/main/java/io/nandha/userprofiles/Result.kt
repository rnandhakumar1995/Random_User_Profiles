package io.nandha.userprofiles

import io.nandha.userprofiles.model.data.WeatherResponse

sealed class Result {
    class Success(val data: WeatherResponse) : Result()
    class Error(val msg: String) : Result()
}
