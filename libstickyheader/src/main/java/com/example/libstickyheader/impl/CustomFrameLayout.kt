package com.example.libstickyheader.impl

import android.content.Context
import android.widget.FrameLayout

class CustomFrameLayout(context: Context) : FrameLayout(context) {

    private var listener: (() -> Unit)? = null

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
