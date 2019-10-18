package io.github.yusukeiwaki.better_always_drink.shop_list.detail

import androidx.recyclerview.widget.RecyclerView
import io.github.yusukeiwaki.better_always_drink.databinding.ListItemShopPictureListItemBinding


class ShopDetailPictureListItemViewHolder(private val binding: ListItemShopPictureListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(url: String) {
        binding.url = url
    }
}