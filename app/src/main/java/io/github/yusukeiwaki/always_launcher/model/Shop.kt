package io.github.yusukeiwaki.always_launcher.model

import androidx.recyclerview.widget.DiffUtil
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class Shop(
    val uuid: String,
    val name: String,
    val description: String,
    val roughLocationDescription: String,
    val businessHoursDescription: String,
    val lat: Double,
    val lng: Double,
    val thumbnailUrl: String?,
    val pictureUrls: List<String>,
    val hasSecondaryService: Boolean? = null
) : ClusterItem {
    override fun getSnippet(): String? = null

    override fun getTitle(): String? = name

    override fun getPosition(): LatLng = LatLng(lat, lng)

    fun nearestServiceAreaIn(serviceAreas: List<ServiceArea>): ServiceArea? {
        return serviceAreas.minBy { area ->
            (lat - area.lat) * (lat - area.lat) + (lng - area.lng) * (lng - area.lng)
        }
    }

    class DiffUtilCallback: DiffUtil.ItemCallback<Shop>() {
        override fun areItemsTheSame(oldItem: Shop, newItem: Shop): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: Shop, newItem: Shop): Boolean {
            return oldItem == newItem
        }
    }
}
