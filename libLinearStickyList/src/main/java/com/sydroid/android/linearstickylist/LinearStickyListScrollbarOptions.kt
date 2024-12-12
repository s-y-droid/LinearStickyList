package com.sydroid.android.linearstickylist

data class LinearStickyListScrollbarOptions(
    val isShowScrollbar : Boolean = true,
    val widthDp : Float = 4.0f,
    val drawableResId : Int = R.drawable.linear_sticky_list_defalut_scrollbar,
    val isFadeOut : Boolean = true,
    val isFadeOutAlphaAnimationTimeMs : Long = 600L,
    val isFadeOutInactivityTimeMs : Long = 1200L
)