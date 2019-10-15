package io.github.yusukeiwaki.better_always_drink.shop_list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.github.yusukeiwaki.better_always_drink.databinding.ListItemShopBinding
import io.github.yusukeiwaki.better_always_drink.model.Shop

class ShopListViewHolder(private val binding: ListItemShopBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(shop: Shop) {
        binding.shop = shop
    }
}
