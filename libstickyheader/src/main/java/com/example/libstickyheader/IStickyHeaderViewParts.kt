package com.example.libstickyheader

import androidx.fragment.app.Fragment

interface IStickyHeaderViewParts {
    fun isStickyHeader(): Boolean
    fun makeStickyInstance(): IStickyHeaderViewParts
    fun fragment() : Fragment
}
