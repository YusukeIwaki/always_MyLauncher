package io.github.yusukeiwaki.always_launcher.shop_list.detail

import com.xwray.groupie.databinding.BindableItem
import io.github.yusukeiwaki.always_launcher.R

import io.github.yusukeiwaki.always_launcher.databinding.ListItemShopThumbnailBinding
import io.github.yusukeiwaki.always_launcher.model.Shop

class ShopThumbnailItem(private val shop: Shop) : BindableItem<ListItemShopThumbnailBinding>() {
    override fun getLayout() = R.layout.list_item_shop_thumbnail

    override fun bind(viewBinding: ListItemShopThumbnailBinding, position: Int) {
        viewBinding.url = shop.thumbnailUrl
    }
}
