package com.example.weatherapp.model

data class WeatherResponse(
    val weather: List<WeatherInfo>,
    val main: MainInfo,
    val wind: WindInfo,
    val dt: Long,
    val name: String
)

data class WeatherInfo(
    val main: String,
    val description: String,
    val icon: String
)

data class MainInfo(
    val temp: Float,
    val temp_min: Float,
    val temp_max: Float,
    val humidity: Int
)

data class WindInfo(
    val speed: Float
)
