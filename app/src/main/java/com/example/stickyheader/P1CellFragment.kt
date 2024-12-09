package com.example.stickyheader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.stickyheader.databinding.P1CellFragmentBinding

class P1CellFragment : PnCellFragmentBase() {

    companion object {
        fun newInstance() = P1CellFragment()
    }

    private lateinit var binding: P1CellFragmentBinding

    override fun isStickyHeader() = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = P1CellFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btn.setOnClickListener {
            setFragmentResult("P1BtnClicked", bundleOf())
        }

        var c = 0
        binding.btn2.setOnClickListener {
            c++
            binding.view.layoutParams?.let {
                it.height = if (c % 2 == 0) 100 else 300
                binding.view.layoutParams = it
            }
        }
    }
}
