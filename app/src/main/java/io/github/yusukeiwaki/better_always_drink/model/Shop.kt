package io.github.yusukeiwaki.better_always_drink.model

import androidx.recyclerview.widget.DiffUtil

data class Shop(
    val uuid: String,
    val name: String,
    val description: String,
    val roughLocationDescription: String,
    val businessHoursDescription: String,
    val lat: Double,
    val lng: Double,
    val thumbnailUrl: String?,
    val pictureUrls: List<String>
) {
    class DiffUtilCallback: DiffUtil.ItemCallback<Shop>() {
        override fun areItemsTheSame(oldItem: Shop, newItem: Shop): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: Shop, newItem: Shop): Boolean {
            return oldItem == newItem
        }
    }
}
