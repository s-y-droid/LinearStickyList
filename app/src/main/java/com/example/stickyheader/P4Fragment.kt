package com.example.stickyheader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stickyheader.databinding.P4FragmentBinding

class P4Fragment : PnNormalFragmentBase() {

    companion object {
        fun newInstance() = P4Fragment()
    }

    private lateinit var binding: P4FragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = P4FragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}
