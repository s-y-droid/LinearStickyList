package com.example.libstickyheader.impl

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView

class StickyScrollView(
    context: Context,
    attrs: AttributeSet? = null
) : ScrollView(context, attrs) {

    init {
        isVerticalScrollBarEnabled = false
    }

    private var listener: ((y: Int) -> Unit)? = null

    fun setScrollChangedCallback(cb: ((y: Int) -> Unit)?) {
        listener = cb
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        listener = null
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        listener?.invoke(t)
    }
}