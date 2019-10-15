package io.github.yusukeiwaki.better_always_drink.shop_list

import android.content.Context
import android.util.AttributeSet
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ShopPictureListView : RecyclerView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
    }

    fun setPictureUrls(pictureUrls: List<String>?) {
        if (pictureUrls == null) return

        adapter = ShopPictureListAdapter().also {
            it.submitList(pictureUrls)
        }
    }
}
