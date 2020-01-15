package io.github.yusukeiwaki.always_launcher.shop_list

import androidx.recyclerview.widget.RecyclerView
import io.github.yusukeiwaki.always_launcher.databinding.ListItemShopBinding
import io.github.yusukeiwaki.always_launcher.model.Shop

class ShopListViewHolder(private val binding: ListItemShopBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(shop: Shop) {
        binding.shop = shop
    }
}
