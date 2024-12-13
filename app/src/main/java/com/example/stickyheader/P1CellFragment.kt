package com.example.stickyheader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.stickyheader.databinding.P1CellFragmentBinding
import com.sydroid.android.linearstickylist.LinearStickyListCellFragmentBase

// Note #3
// The cell fragment inherits from LinearStickyListCellFragmentBase, which requires some overrides:
//
// 1. (Required) fun isStickyHeader(): Boolean
//    ... Specify true if you want this Cell Fragment to act as a StickyHeader.
//
// 2. (Optional) fun onDistanceFromDisplayArea(isOnScreen: Boolean, distancePx: Float)
//    ... The following information is called back:
//
//        isOnScreen : Whether the cell is visible on screen or not
//        distancePx :　If isOnScreen is false, the distance away from the screen (unit: Px).
//
//        a callback will be made to tell you how far it is from the screen.
//        Its purpose is to stop the animation of off-screen Cell or to free memory, etc.
//        Please override if you need it.

class P1CellFragment : LinearStickyListCellFragmentBase() {

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
                // Note #4
                // If you want to pass event to the parent, use the setFragmentResult() method of LinearStickyListCellFragmentBase.
                // This is recommended as using callbacks will not maintain references when the Cell Fragment is regenerated.
                // (In this sample, the event "P1BtnClicked" is received by MainActivity.)
                setFragmentResult("P1BtnClicked", bundleOf("idx" to idx + 1))
            }
        }
    }
}
