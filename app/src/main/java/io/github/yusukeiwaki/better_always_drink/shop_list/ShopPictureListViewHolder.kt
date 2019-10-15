package io.github.yusukeiwaki.better_always_drink.shop_list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.github.yusukeiwaki.better_always_drink.databinding.ListItemShopPictureBinding

class ShopPictureListViewHolder(private val binding: ListItemShopPictureBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(url: String?) {
        binding.url = url
    }
}
