/**
 *    Copyright 2024 s-y-droid
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.example.stickyheader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.example.stickyheader.databinding.P2CellFragmentBinding
import com.sydroid.android.linearstickylist.LinearStickyListCellFragmentBase

class P2CellFragment : LinearStickyListCellFragmentBase() {

    companion object {
        fun newInstance(patternNo: Int = 0) = P2CellFragment().apply {
            arguments = bundleOf("patternNo" to patternNo)
        }
    }

    private lateinit var binding: P2CellFragmentBinding

    override fun isStickyHeader() = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = P2CellFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ptn = arguments?.getInt("patternNo") ?: 0

        fun setResource(img: ImageView, resId: Int) {
            img.setImageDrawable(ContextCompat.getDrawable(requireActivity(), resId))
        }
        if (ptn == 0) {
            setResource(binding.img1, R.drawable.arm_wrestling_man)
            setResource(binding.img2, R.drawable.baseball_hit_woman)
            setResource(binding.img3, R.drawable.pet_dog_dance_woman)
            setResource(binding.img4, R.drawable.rikujou_hurdle_man2)
            setResource(binding.img5, R.drawable.skateboard_long_girl)
            setResource(binding.img6, R.drawable.skydiving_couple)
        } else {
            setResource(binding.img1, R.drawable.sports_dodgeball_boy_girl)
            setResource(binding.img2, R.drawable.sports_soccer_pass_man)
            setResource(binding.img3, R.drawable.sports_sumo_shio)
            setResource(binding.img4, R.drawable.sports_volleyball_man_recieve)
            setResource(binding.img5, R.drawable.rock_climbing_woman)
            setResource(binding.img6, R.drawable.shintaisou_man)
        }

        binding.btn.text = "More"
        binding.btn.setOnClickListener {
            if (binding.btn.text == "More") {
                binding.btn.text = "Close"
                binding.expandableLayout.expand()
            } else {
                binding.btn.text = "More"
                binding.expandableLayout.collapse()
            }
        }
    }
}
