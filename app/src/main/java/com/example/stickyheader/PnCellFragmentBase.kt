package com.example.stickyheader

import android.os.Bundle
import androidx.core.os.bundleOf
import com.example.libstickyheader.StickyHeaderListCellFragmentBase
import com.example.libstickyheader.StickyHeaderListFragment

abstract class PnCellFragmentBase : StickyHeaderListCellFragmentBase() {
    protected fun setFragmentResult(key: String, bundle: Bundle? = null) {
        (parentFragment as? StickyHeaderListFragment)?.parentFragmentManager?.setFragmentResult(
            key,
            bundle ?: bundleOf()
        )
    }
}
