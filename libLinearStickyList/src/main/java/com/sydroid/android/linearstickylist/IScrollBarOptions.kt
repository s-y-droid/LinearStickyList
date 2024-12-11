package com.sydroid.android.linearstickylist

interface IScrollBarOptions {
    fun isShowScrollbar(): Boolean = true
    fun widthDp(): Float = 4.0f
    fun drawableResId(): Int = R.drawable.linear_sticky_list_defalut_scrollbar
    fun isFadeOut(): Boolean = true
    fun isFadeOutAlphaAnimationTimeMs(): Long = 600L
    fun isFadeOutInactivityTimeMs(): Long = 1200L
}