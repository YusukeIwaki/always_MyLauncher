package io.github.yusukeiwaki.always_launcher.shop_list.detail

import com.xwray.groupie.databinding.BindableItem
import io.github.yusukeiwaki.always_launcher.R

import io.github.yusukeiwaki.always_launcher.model.Shop
import io.github.yusukeiwaki.always_launcher.databinding.ListItemShopDescriptionBinding

class ShopDetailDescriptionItem(private val shop: Shop) : BindableItem<ListItemShopDescriptionBinding>() {
    override fun getLayout() = R.layout.list_item_shop_description

    override fun bind(viewBinding: ListItemShopDescriptionBinding, position: Int) {
        viewBinding.text = shop.description
    }
}
