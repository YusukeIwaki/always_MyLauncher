package io.github.yusukeiwaki.always_launcher.shop_list.detail

import androidx.recyclerview.widget.RecyclerView
import io.github.yusukeiwaki.always_launcher.databinding.ListItemShopPictureListItemBinding


class ShopDetailPictureListItemViewHolder(private val binding: ListItemShopPictureListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(url: String) {
        binding.url = url
    }
}
