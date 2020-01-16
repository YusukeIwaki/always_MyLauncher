package io.github.yusukeiwaki.always_launcher.shop_list.detail

import com.xwray.groupie.databinding.BindableItem
import io.github.yusukeiwaki.always_launcher.R

import io.github.yusukeiwaki.always_launcher.databinding.ListItemShopPictureListBinding
import io.github.yusukeiwaki.always_launcher.model.Shop

class ShopPictureListItem(private val shop: Shop) : BindableItem<ListItemShopPictureListBinding>() {
    override fun getLayout() = R.layout.list_item_shop_picture_list

    override fun bind(viewBinding: ListItemShopPictureListBinding, position: Int) {
        viewBinding.shop = shop
    }
}
