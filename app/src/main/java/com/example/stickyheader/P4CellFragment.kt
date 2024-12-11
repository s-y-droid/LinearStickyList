package com.example.stickyheader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.stickyheader.databinding.P4CellFragmentBinding

class P4CellFragment : PnCellFragmentBase() {

    companion object {
        fun newInstance() = P4CellFragment()
    }

    private lateinit var binding: P4CellFragmentBinding

    override fun isStickyHeader() = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = P4CellFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun onP3BtnClicked() {
        binding.txt.text = "Cell P4 was displayed due to an event in cell P3"
        binding.expandableLayout.expand()
    }
}
