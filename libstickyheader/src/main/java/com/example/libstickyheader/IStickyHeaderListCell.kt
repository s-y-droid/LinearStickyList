package com.example.libstickyheader

import androidx.fragment.app.Fragment

interface IStickyHeaderListCell {
    fun isStickyHeader(): Boolean
    fun fragment() : Fragment
}
