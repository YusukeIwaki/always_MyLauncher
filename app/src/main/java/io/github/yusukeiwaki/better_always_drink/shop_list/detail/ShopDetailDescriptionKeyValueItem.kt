package io.github.yusukeiwaki.better_always_drink.shop_list.detail

import com.xwray.groupie.databinding.BindableItem
import io.github.yusukeiwaki.better_always_drink.R

import io.github.yusukeiwaki.better_always_drink.databinding.ListItemShopDescriptionKeyValueBinding
import io.github.yusukeiwaki.better_always_drink.model.Shop

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
