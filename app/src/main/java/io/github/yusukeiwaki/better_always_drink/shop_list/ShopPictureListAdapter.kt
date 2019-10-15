package io.github.yusukeiwaki.better_always_drink.shop_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import io.github.yusukeiwaki.better_always_drink.R
import io.github.yusukeiwaki.better_always_drink.databinding.ListItemShopPictureBinding

class ShopPictureListAdapter : ListAdapter<String, ShopPictureListViewHolder>(ShopPictureDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopPictureListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ListItemShopPictureBinding = DataBindingUtil.inflate(inflater, R.layout.list_item_shop_picture, parent, false)
        return ShopPictureListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopPictureListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
