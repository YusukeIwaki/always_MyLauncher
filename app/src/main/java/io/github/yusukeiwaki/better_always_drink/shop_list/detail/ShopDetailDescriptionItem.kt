package io.github.yusukeiwaki.better_always_drink.shop_list.detail

import com.xwray.groupie.databinding.BindableItem
import io.github.yusukeiwaki.better_always_drink.R

import io.github.yusukeiwaki.better_always_drink.databinding.ListItemShopDescriptionBinding
import io.github.yusukeiwaki.better_always_drink.model.Shop

class ShopDetailDescriptionItem(private val shop: Shop) : BindableItem<ListItemShopDescriptionBinding>() {
    override fun getLayout() = R.layout.list_item_shop_description

    override fun bind(viewBinding: ListItemShopDescriptionBinding, position: Int) {
        viewBinding.text = shop.description
    }
}
