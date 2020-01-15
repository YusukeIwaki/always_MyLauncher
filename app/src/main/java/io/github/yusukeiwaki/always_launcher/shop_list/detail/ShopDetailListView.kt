package io.github.yusukeiwaki.always_launcher.shop_list.detail

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import io.github.yusukeiwaki.always_launcher.model.Shop

class ShopDetailListView : RecyclerView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
    }

    fun setShop(shop: Shop) {
        adapter = GroupAdapter<GroupieViewHolder>().also { groupAdapter ->
            groupAdapter.add(ShopThumbnailItem(shop))
            if (shop.pictureUrls.isNotEmpty()) {
                groupAdapter.add(ShopPictureListItem(shop))
            }
            groupAdapter.add(ShopDetailDescriptionItem(shop))
            groupAdapter.add(ShopDetailDescriptionKeyValueItem(shop, ShopDetailDescriptionKeyValueItem.Key.BusinessHours))
            groupAdapter.add(ShopDetailActionButtonsItem(shop))
        }
    }
}
