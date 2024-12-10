package com.example.libstickyheader

interface IScrollBarOptions {
    fun isShowScrollbar(): Boolean = true
    fun widthDp(): Float = 4.0f
    fun drawableResId(): Int = R.drawable.shape_defalut_scrollbar
    fun isFadeOut(): Boolean = true
    fun isFadeOutAlphaAnimationTimeMs(): Long = 600L
    fun isFadeOutInactivityTimeMs(): Long = 1200L
}