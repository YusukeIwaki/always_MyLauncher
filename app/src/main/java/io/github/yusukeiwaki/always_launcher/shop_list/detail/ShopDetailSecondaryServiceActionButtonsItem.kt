package io.github.yusukeiwaki.always_launcher.shop_list.detail

import android.content.Intent
import android.net.Uri
import android.view.View
import com.xwray.groupie.databinding.BindableItem
import io.github.yusukeiwaki.always_launcher.BuildConfig
import io.github.yusukeiwaki.always_launcher.R
import io.github.yusukeiwaki.always_launcher.databinding.ListItemShopSecondaryServiceActionButtonsBinding
import io.github.yusukeiwaki.always_launcher.model.Shop

class ShopDetailSecondaryServiceActionButtonsItem(private val shop: Shop) : BindableItem<ListItemShopSecondaryServiceActionButtonsBinding>() {

    private val onBrowseButtonClick = object: View.OnClickListener {
        override fun onClick(view: View?) {
            val url = "https://always.fan/original/${BuildConfig.SECONDARY_SERVICE_TYPE}/user-subscription/${BuildConfig.SECONDARY_SERVICE_UUID}?p=${shop.uuid}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            view?.context?.startActivity(intent)
        }
    }

    override fun getLayout() = R.layout.list_item_shop_secondary_service_action_buttons

    override fun bind(viewBinding: ListItemShopSecondaryServiceActionButtonsBinding, position: Int) {
        viewBinding.browseButton.setOnClickListener(onBrowseButtonClick)
    }
}
