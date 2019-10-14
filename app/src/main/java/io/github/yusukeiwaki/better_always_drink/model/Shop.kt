package io.github.yusukeiwaki.better_always_drink.model

data class Shop(
    val uuid: String,
    val name: String,
    val description: String,
    val roughLocationDescription: String,
    val businessHoursDescription: String,
    val lat: Double,
    val lng: Double,
    val pictureUrls: List<String>
)
