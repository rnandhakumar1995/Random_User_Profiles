package io.nandha.userprofiles.model.data

data class Weather(val description: String)

data class WeatherResponse(val weather: List<Weather>)
