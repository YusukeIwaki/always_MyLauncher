package io.github.yusukeiwaki.always_launcher.shop_list.detail

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

    // 高さ(match_parent)に合わせた正方形にする
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(height, height)
    }
}
