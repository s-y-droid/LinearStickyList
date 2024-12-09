package com.example.stickyheader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stickyheader.databinding.P3CellFragmentBinding

class P3CellFragment : PnCellFragmentBase() {

    companion object {
        fun newInstance() = P3CellFragment()
    }

    private lateinit var binding: P3CellFragmentBinding

    override fun isStickyHeader() = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = P3CellFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}
