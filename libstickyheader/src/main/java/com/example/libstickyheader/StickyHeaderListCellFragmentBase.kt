package com.example.libstickyheader

import androidx.fragment.app.Fragment

abstract class StickyHeaderListCellFragmentBase : Fragment() {
    abstract fun isStickyHeader(): Boolean
    open fun isCallOnDistanceFromDisplayArea(): Boolean = true
    open fun onDistanceFromDisplayArea(isOnScreen: Boolean, distancePx: Float) {}
}
