package com.example.stickyheader

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.stickyheader.databinding.P1FragmentBinding

class P1Fragment : PnStickyFragmentBase() {

    companion object {
        fun newInstance(isSticky: Boolean = false) = P1Fragment().apply {
            arguments = bundleOf("isSticky" to isSticky)
        }
    }

    private lateinit var binding: P1FragmentBinding

    override fun makeStickyInstance() = newInstance(isSticky = true)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = P1FragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btn.setOnClickListener {
            setFragmentResult("P1BtnClicked", bundleOf())
        }
    }
}
