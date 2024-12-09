package com.example.stickyheader

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.libstickyheader.IStickyHeaderListCell
import com.example.libstickyheader.StickyHeaderListFragment

abstract class PnCellFragmentBase : Fragment(), IStickyHeaderListCell {
    override fun fragment() = this

    protected fun setFragmentResult(key: String, bundle: Bundle) {
        (parentFragment as? StickyHeaderListFragment)?.parentFragmentManager?.setFragmentResult(
            key,
            bundle
        )
    }
}
