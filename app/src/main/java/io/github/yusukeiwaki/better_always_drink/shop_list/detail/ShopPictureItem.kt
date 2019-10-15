package io.github.yusukeiwaki.better_always_drink.shop_list.detail

import com.xwray.groupie.databinding.BindableItem
import io.github.yusukeiwaki.better_always_drink.R

import io.github.yusukeiwaki.better_always_drink.databinding.ListItemShopPictureBinding
import io.github.yusukeiwaki.better_always_drink.model.Shop

class ShopPictureItem(private val shop: Shop) : BindableItem<ListItemShopPictureBinding>() {
    override fun getLayout() = R.layout.list_item_shop_picture

    override fun bind(viewBinding: ListItemShopPictureBinding, position: Int) {
        if (position >= 1 && position <= shop.pictureUrls.size) {
            viewBinding.url = shop.pictureUrls[position - 1]
        }
    }
}
