package io.github.yusukeiwaki.better_always_drink.shop_list.detail

import com.xwray.groupie.databinding.BindableItem
import io.github.yusukeiwaki.better_always_drink.R

import io.github.yusukeiwaki.better_always_drink.databinding.ListItemShopThumbnailBinding
import io.github.yusukeiwaki.better_always_drink.model.Shop

class ShopThumbnailItem(private val shop: Shop) : BindableItem<ListItemShopThumbnailBinding>() {
    override fun getLayout() = R.layout.list_item_shop_thumbnail

    override fun bind(viewBinding: ListItemShopThumbnailBinding, position: Int) {
        viewBinding.url = shop.thumbnailUrl
    }
}
