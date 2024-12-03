package com.example.stickyheader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.stickyheader.databinding.P3FragmentBinding

class P3Fragment : PnStickyFragmentBase() {

    companion object {
        fun newInstance(isSticky: Boolean = false) = P3Fragment().apply {
            arguments = bundleOf("isSticky" to isSticky)
        }
    }

    private lateinit var binding: P3FragmentBinding

    override fun makeStickyInstance() = newInstance(isSticky = true)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = P3FragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}
