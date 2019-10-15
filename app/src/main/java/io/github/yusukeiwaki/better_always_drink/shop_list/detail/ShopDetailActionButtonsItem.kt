package io.github.yusukeiwaki.better_always_drink.shop_list.detail

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import com.xwray.groupie.databinding.BindableItem
import io.github.yusukeiwaki.better_always_drink.R
import io.github.yusukeiwaki.better_always_drink.databinding.ListItemShopActionButtonsBinding

import io.github.yusukeiwaki.better_always_drink.databinding.ListItemShopDescriptionBinding
import io.github.yusukeiwaki.better_always_drink.model.Shop
import io.github.yusukeiwaki.better_always_drink.shop_list.AlwaysPreference

class ShopDetailActionButtonsItem(private val shop: Shop) : BindableItem<ListItemShopActionButtonsBinding>() {

    private val onAlwaysButtonClick = object: View.OnClickListener {
        override fun onClick(view: View?) {
            view?.let {
                AlwaysPreference(it.context).putValue(shop.uuid)
                Toast.makeText(it.context, "${shop.name} が次回起動時からすぐ表示されます", Toast.LENGTH_LONG).show()
            }
        }
    }

    private val onBrowseButtonClick = object: View.OnClickListener {
        override fun onClick(view: View?) {
            val url = "https://always.fan/original/drink/user-subscription/751acbbe?p=${shop.uuid}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            view?.context?.startActivity(intent)
        }
    }

    override fun getLayout() = R.layout.list_item_shop_action_buttons

    override fun bind(viewBinding: ListItemShopActionButtonsBinding, position: Int) {
        viewBinding.alwaysButton.setOnClickListener(onAlwaysButtonClick)
        viewBinding.browseButton.setOnClickListener(onBrowseButtonClick)
    }
}
