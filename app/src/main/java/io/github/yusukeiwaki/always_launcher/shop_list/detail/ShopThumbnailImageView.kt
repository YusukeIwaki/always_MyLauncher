package io.github.yusukeiwaki.always_launcher.shop_list.detail

import android.content.Context
import android.util.AttributeSet

import androidx.appcompat.widget.AppCompatImageView
import com.squareup.picasso.Picasso


class ShopThumbnailImageView : AppCompatImageView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setImageUrl(imageUrl: String?) {
        Picasso.get().load(imageUrl).into(this)
    }

    // 幅(match_parent)に合わせた正方形にする
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, width)
    }
}
