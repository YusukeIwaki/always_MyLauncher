package io.github.yusukeiwaki.always_launcher.shop_list.detail

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.yusukeiwaki.always_launcher.model.Shop

class ShopDetailPictureListView : RecyclerView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        adapter = ShopDetailPictureListItemAdapter()
    }

    fun setShop(shop: Shop) {
        (adapter as ShopDetailPictureListItemAdapter).submitList(shop.pictureUrls)
    }
}
