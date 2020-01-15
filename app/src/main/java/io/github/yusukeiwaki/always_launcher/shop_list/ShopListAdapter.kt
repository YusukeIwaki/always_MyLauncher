package io.github.yusukeiwaki.always_launcher.shop_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import io.github.yusukeiwaki.always_launcher.R
import io.github.yusukeiwaki.always_launcher.databinding.ListItemShopBinding
import io.github.yusukeiwaki.always_launcher.model.Shop

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
