package io.github.yusukeiwaki.always_launcher.shop_list.detail

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import com.xwray.groupie.databinding.BindableItem
import io.github.yusukeiwaki.always_launcher.BuildConfig
import io.github.yusukeiwaki.always_launcher.R
import io.github.yusukeiwaki.always_launcher.model.Shop
import io.github.yusukeiwaki.always_launcher.shop_list.AlwaysPreference
import io.github.yusukeiwaki.always_launcher.databinding.ListItemShopActionButtonsBinding

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
            val url = "https://always.fan/original/${BuildConfig.SERVICE_TYPE}/user-subscription/${BuildConfig.SERVICE_UUID}?p=${shop.uuid}"
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
