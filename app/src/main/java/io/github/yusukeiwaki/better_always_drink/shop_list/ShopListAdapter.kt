package io.github.yusukeiwaki.better_always_drink.shop_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import io.github.yusukeiwaki.better_always_drink.R
import io.github.yusukeiwaki.better_always_drink.databinding.ListItemShopBinding
import io.github.yusukeiwaki.better_always_drink.model.Shop

class ShopListAdapter : ListAdapter<Shop, ShopListViewHolder>(Shop.DiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ListItemShopBinding>(inflater, R.layout.list_item_shop, parent, false)
        return ShopListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
