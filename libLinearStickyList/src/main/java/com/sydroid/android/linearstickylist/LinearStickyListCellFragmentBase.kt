package com.sydroid.android.linearstickylist

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

abstract class LinearStickyListCellFragmentBase : Fragment() {
    abstract fun isStickyHeader(): Boolean
    open fun onDistanceFromDisplayArea(isOnScreen: Boolean, distancePx: Float) {}

    protected fun setFragmentResult(key: String, bundle: Bundle? = null) {
        getParent().parentFragmentManager.setFragmentResult(key, bundle ?: bundleOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            getParent().cellFragmentIsRestored(this, arguments?.getInt("StickyHeaderList_Idx") ?: 0)
        }
    }

    private fun getParent() =
        parentFragment as? LinearStickyListFragment
            ?: throw Exception("LinearStickyListCellFragmentBase.getParent() critical error.")

}
