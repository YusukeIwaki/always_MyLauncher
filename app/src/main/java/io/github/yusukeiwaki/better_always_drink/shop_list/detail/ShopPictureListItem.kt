package io.github.yusukeiwaki.better_always_drink.shop_list.detail

import com.xwray.groupie.databinding.BindableItem
import io.github.yusukeiwaki.better_always_drink.R

import io.github.yusukeiwaki.better_always_drink.databinding.ListItemShopPictureListBinding
import io.github.yusukeiwaki.better_always_drink.model.Shop

class ShopPictureListItem(private val shop: Shop) : BindableItem<ListItemShopPictureListBinding>() {
    override fun getLayout() = R.layout.list_item_shop_picture_list

    override fun bind(viewBinding: ListItemShopPictureListBinding, position: Int) {
        viewBinding.shop = shop
    }
}
