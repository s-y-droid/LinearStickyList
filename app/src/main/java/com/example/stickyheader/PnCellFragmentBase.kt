package com.example.stickyheader

import android.os.Bundle
import androidx.core.os.bundleOf
import com.sydroid.android.linearstickylist.LinearStickyListCellFragmentBase
import com.sydroid.android.linearstickylist.LinearStickyListFragment

abstract class PnCellFragmentBase : LinearStickyListCellFragmentBase() {
    protected fun setFragmentResult(key: String, bundle: Bundle? = null) {
        (parentFragment as? LinearStickyListFragment)?.parentFragmentManager?.setFragmentResult(
            key,
            bundle ?: bundleOf()
        )
    }
}
