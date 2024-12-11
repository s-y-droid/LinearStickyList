package com.sydroid.android.linearstickylist

import androidx.fragment.app.Fragment

abstract class LinearStickyListCellFragmentBase : Fragment() {
    abstract fun isStickyHeader(): Boolean
    open fun isCallOnDistanceFromDisplayArea(): Boolean = true
    open fun onDistanceFromDisplayArea(isOnScreen: Boolean, distancePx: Float) {}
}
