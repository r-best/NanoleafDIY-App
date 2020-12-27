package com.example.nanoleafdiy.utils

import android.content.Context
import android.util.AttributeSet
import kotlin.math.min

/** https://stackoverflow.com/questions/16506275/imageview-be-a-square-with-dynamic-width */
class SquareImageView : androidx.appcompat.widget.AppCompatImageView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height: Int = min(measuredHeight, measuredWidth)
        setMeasuredDimension(height, height)
    }
}
