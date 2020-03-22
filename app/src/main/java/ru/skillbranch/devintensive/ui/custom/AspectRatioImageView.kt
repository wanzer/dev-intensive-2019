package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.util.AttributeSet
import ru.skillbranch.devintensive.R

class AspectRatioImageView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAtt: Int = 0): androidx.appcompat.widget.AppCompatImageView(context, attributeSet, defStyleAtt) {

    private var aspectRatio = DEFAULT_ASPECT_RATIO

    init {
        if(attributeSet != null){
            val a = context.obtainStyledAttributes(attributeSet, R.styleable.AspectRatioImageView)
            aspectRatio = a.getFloat(R.styleable.AspectRatioImageView_aspectRatio, DEFAULT_ASPECT_RATIO)
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val newHeight = (measuredHeight/aspectRatio).toInt()
        setMeasuredDimension(measuredWidth, newHeight)
    }

    companion object {
        private const val DEFAULT_ASPECT_RATIO = 1.78f
    }
}