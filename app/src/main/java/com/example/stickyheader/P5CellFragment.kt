package com.example.stickyheader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stickyheader.databinding.P5CellFragmentBinding

class P5CellFragment : PnCellFragmentBase() {

    companion object {
        fun newInstance() = P5CellFragment()
    }

    private lateinit var binding: P5CellFragmentBinding

    override fun isStickyHeader() = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = P5CellFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}
