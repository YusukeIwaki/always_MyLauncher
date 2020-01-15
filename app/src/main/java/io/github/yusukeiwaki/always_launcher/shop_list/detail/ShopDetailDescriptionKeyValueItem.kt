package io.github.yusukeiwaki.always_launcher.shop_list.detail

import com.xwray.groupie.databinding.BindableItem
import io.github.yusukeiwaki.always_launcher.R

import io.github.yusukeiwaki.always_launcher.databinding.ListItemShopDescriptionKeyValueBinding
import io.github.yusukeiwaki.always_launcher.model.Shop

class ShopDetailDescriptionKeyValueItem(private val shop: Shop, private val key: Key) : BindableItem<ListItemShopDescriptionKeyValueBinding>() {
    enum class Key {
        BusinessHours
    }
    override fun getLayout() = R.layout.list_item_shop_description_key_value

    override fun bind(viewBinding: ListItemShopDescriptionKeyValueBinding, position: Int) {
        when (key) {
            Key.BusinessHours -> {
                viewBinding.title = "営業時間"
                viewBinding.description = shop.businessHoursDescription
            }
        }
    }
}
