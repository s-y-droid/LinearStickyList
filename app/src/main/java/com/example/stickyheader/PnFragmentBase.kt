package com.example.stickyheader

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.libstickyheader.IStickyHeaderViewParts
import com.example.libstickyheader.StickyHeaderFragment

abstract class PnFragmentBase : Fragment(), IStickyHeaderViewParts {
    override fun fragment() = this

    protected fun setFragmentResult(key: String, bundle: Bundle) {
        (parentFragment as? StickyHeaderFragment)?.parentFragmentManager?.setFragmentResult(
            key,
            bundle
        )
    }
}
