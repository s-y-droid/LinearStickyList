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
import com.example.stickyheader.databinding.P5CellFragmentBinding
import com.sydroid.android.linearstickylist.LinearStickyListCellFragmentBase

class P5CellFragment : LinearStickyListCellFragmentBase() {

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
