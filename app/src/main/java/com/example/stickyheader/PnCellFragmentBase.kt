package com.example.stickyheader

import android.os.Bundle
import com.example.libstickyheader.StickyHeaderListCellFragmentBase
import com.example.libstickyheader.StickyHeaderListFragment

abstract class PnCellFragmentBase : StickyHeaderListCellFragmentBase() {
    protected fun setFragmentResult(key: String, bundle: Bundle) {
        (parentFragment as? StickyHeaderListFragment)?.parentFragmentManager?.setFragmentResult(
            key,
            bundle
        )
    }
}
