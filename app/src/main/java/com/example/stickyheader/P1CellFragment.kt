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

        listOf(
            binding.btn1, binding.btn2, binding.btn3, binding.btn4, binding.btn5,
            binding.btn6, binding.btn7, binding.btn8, binding.btn9, binding.btn10
        ).forEachIndexed { idx, v ->
            v.setOnClickListener {
                setFragmentResult("P1BtnClicked", bundleOf("idx" to idx + 1))
            }
        }
    }
}
