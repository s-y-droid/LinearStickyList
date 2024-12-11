package com.sydroid.android.linearstickylist.impl

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout

class CustomFrameLayout(context: Context) : FrameLayout(context) {

    private var listener: (() -> Unit)? = null

    init {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        )
    }

    fun setOnLayoutCallback(cb: (() -> Unit)?) {
        listener = cb
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        listener = null
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            listener?.invoke()
        }
    }
}
