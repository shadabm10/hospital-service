package com.rootscare.customviews.neusaedittext

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet

class MyNeusaMediumEditTextview : androidx.appcompat.widget.AppCompatEditText {
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )
    {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?) : super(context) {
        init()
    }

    fun init() {
        val tf = Typeface.createFromAsset(context.assets, "font/neusa_medium.otf")
        setTypeface(tf, 1)
    }


}