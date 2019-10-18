package io.github.yusukeiwaki.better_always_drink.shop_list.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.yusukeiwaki.better_always_drink.R
import io.github.yusukeiwaki.better_always_drink.databinding.ListItemShopBinding
import io.github.yusukeiwaki.better_always_drink.databinding.ListItemShopPictureListItemBinding
import io.github.yusukeiwaki.better_always_drink.shop_list.ShopListViewHolder


class ShopDetailPictureListItemAdapter: ListAdapter<String, ShopDetailPictureListItemViewHolder>(StringItemDiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopDetailPictureListItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ListItemShopPictureListItemBinding>(inflater, R.layout.list_item_shop_picture_list_item, parent, false)
        return ShopDetailPictureListItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopDetailPictureListItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
