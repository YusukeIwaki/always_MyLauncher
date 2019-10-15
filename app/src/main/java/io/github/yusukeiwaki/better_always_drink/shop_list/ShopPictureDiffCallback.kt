package io.github.yusukeiwaki.better_always_drink.shop_list

import androidx.recyclerview.widget.DiffUtil

class ShopPictureDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}
