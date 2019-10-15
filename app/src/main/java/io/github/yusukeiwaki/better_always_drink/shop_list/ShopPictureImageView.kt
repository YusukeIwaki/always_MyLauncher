package io.github.yusukeiwaki.better_always_drink.shop_list

import android.content.Context
import android.util.AttributeSet

import androidx.appcompat.widget.AppCompatImageView
import com.squareup.picasso.Picasso

class ShopPictureImageView : AppCompatImageView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setImageUrl(imageUrl: String?) {
        Picasso.get().load(imageUrl).into(this)
    }
}
