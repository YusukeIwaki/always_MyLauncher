package io.github.yusukeiwaki.better_always_drink.shop_list.detail

import android.content.Intent
import android.net.Uri
import android.view.View
import com.xwray.groupie.databinding.BindableItem
import io.github.yusukeiwaki.better_always_drink.R
import io.github.yusukeiwaki.better_always_drink.databinding.ListItemShopActionButtonsBinding

import io.github.yusukeiwaki.better_always_drink.databinding.ListItemShopDescriptionBinding
import io.github.yusukeiwaki.better_always_drink.model.Shop

class ShopDetailActionButtonsItem(private val shop: Shop) : BindableItem<ListItemShopActionButtonsBinding>() {
    private val onBrowseButtonClick = object: View.OnClickListener {
        override fun onClick(view: View?) {
            val url = "https://always.fan/original/drink/user-subscription/751acbbe?p=${shop.uuid}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            view?.context?.startActivity(intent)
        }
    }

    override fun getLayout() = R.layout.list_item_shop_action_buttons

    override fun bind(viewBinding: ListItemShopActionButtonsBinding, position: Int) {
        viewBinding.browseButton.setOnClickListener(onBrowseButtonClick)
    }
}
