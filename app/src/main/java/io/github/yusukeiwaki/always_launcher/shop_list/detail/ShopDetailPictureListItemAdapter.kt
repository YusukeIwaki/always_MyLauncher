package io.github.yusukeiwaki.always_launcher.shop_list.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import io.github.yusukeiwaki.always_launcher.R
import io.github.yusukeiwaki.always_launcher.databinding.ListItemShopPictureListItemBinding

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
