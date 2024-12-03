package com.example.stickyheader

abstract class PnNormalFragmentBase : PnFragmentBase() {
    override fun isStickyHeader() = false
    override fun makeStickyInstance() = throw Exception()
}
