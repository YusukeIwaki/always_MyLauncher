package io.github.yusukeiwaki.always_launcher.api

data class ListProvidersResponse(
    val defaultLat: Double,
    val defaultLng: Double,
    val menus: List<Menu>,
    val places: List<Place>
) {

    data class Provider(
        val uuid: String,
        val name: String,
        val description: String,

        // ざっくりの場所
        val area: String,

        // 住所
        val postalCode: String,
        val prefecture: String,
        val city: String,

        // 電話番号
        val phone: String,

        // 営業時間
        val businessHours: String,

        // 緯度経度
        val location: Location,

        val pictures: List<Picture>
    ) {
        data class Location(val lat: Double, val lon: Double)
    }

    data class Picture(
        val id: Long,
        val pictureUrl: Url
    ) {
        data class Url(val largeUrl: String, val smallUrl: String)
    }

    data class Menu(
        val id: Long,
        val title: String,
        val pbProvider: Provider,
        val pictures: List<Picture>
    )

    data class Place(
        val uuid: String,
        val name: String,
        val lat: Double,
        val lng: Double,
        val zoom: Int
    )
}
