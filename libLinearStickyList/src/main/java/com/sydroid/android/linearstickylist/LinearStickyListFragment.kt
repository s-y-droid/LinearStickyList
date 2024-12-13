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

package com.sydroid.android.linearstickylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.sydroid.android.linearstickylist.databinding.LinearStickyListFragmentBinding
import com.sydroid.android.linearstickylist.impl.CustomFrameLayout

class LinearStickyListFragment : Fragment() {

    private lateinit var binding: LinearStickyListFragmentBinding
    private val itemList = mutableListOf<StickyItem>()
    private var originalScrollY: Int = 0
    private var isBind = false
    private var isViewCreated = false
    private var bindStock: List<LinearStickyListCellFragmentBase>? = null
    private var scrollbarOptions: LinearStickyListScrollbarOptions? = null
    private var scrollbarHeight: Float = 0f
    private var scrollbarFadeoutRunnable: ScrollbarFadeoutRunnable? = null
    private var currentTopNormalCellIdx: Int? = null

    private inner class StickyItem {
        lateinit var listPartsParent: CustomFrameLayout
        var listPartsContainer: FragmentContainerView? = null
        var listPartsView: LinearStickyListCellFragmentBase? = null
        var stickyPartsParent: CustomFrameLayout? = null
        var stickyPartsContainer: FragmentContainerView? = null
        var stickyPartsView: LinearStickyListCellFragmentBase? = null

        fun set(original: LinearStickyListCellFragmentBase, idx: Int): StickyItem {
            val isStickyHeader = original.isStickyHeader()

            if (isStickyHeader) {
                listPartsParent = CustomFrameLayout(requireContext())
                binding.list.addView(listPartsParent)

                stickyPartsParent =
                    CustomFrameLayout(requireContext()).also { fr ->
                        fr.setOnLayoutCallback {
                            listPartsParent.layoutParams.let {
                                it.height = fr.bottom - fr.top
                                listPartsParent.layoutParams = it
                            }
                        }
                    }
                stickyPartsContainer = makeContainer()
                stickyPartsParent!!.addView(stickyPartsContainer)
                binding.stickyArea.addView(stickyPartsParent)
                stickyPartsView = original
                addFragmentToContainer(stickyPartsContainer!!, original, idx)

            } else {
                listPartsView = original
                val orgContainer = makeContainer()
                listPartsContainer = orgContainer
                listPartsParent =
                    CustomFrameLayout(requireContext()).also { it.setOnLayoutCallback(::moveStickyArea) }
                listPartsParent.addView(orgContainer)
                binding.list.addView(listPartsParent)
                addFragmentToContainer(orgContainer, original, idx)
            }
            return this
        }

        fun isStickyHeader() = listPartsView == null

        private fun makeContainer(): FragmentContainerView {
            val container = FragmentContainerView(requireContext())
            container.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            container.id = View.generateViewId()
            return container
        }

        private fun addFragmentToContainer(
            container: FragmentContainerView,
            fragment: LinearStickyListCellFragmentBase,
            idx: Int
        ) {
            fragment.arguments?.putInt("StickyHeaderList_Idx", idx) ?: run {
                fragment.arguments = bundleOf("StickyHeaderList_Idx" to idx)
            }
            childFragmentManager.beginTransaction().remove(fragment).commit()
            childFragmentManager.executePendingTransactions()
            val transaction = childFragmentManager.beginTransaction()
            transaction.replace(container.id, fragment)
            transaction.commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LinearStickyListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let {
            restore(it)
        } ?: run {
            isViewCreated = true
            bindStock?.let {
                setup(it)
                bindStock = null
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isBind", isBind)
        outState.putBoolean("isViewCreated", isViewCreated)
        (scrollbarOptions ?: LinearStickyListScrollbarOptions()).let {
            outState.putBoolean("scrollbarIsShow", it.isShowScrollbar)
            outState.putFloat("scrollbarWidthDp", it.widthDp)
            outState.putInt("scrollbarResId", it.drawableResId)
            outState.putBoolean("scrollbarIsFadeOut", it.isFadeOut)
            outState.putLong("scrollbarFadeOutAnimTime", it.fadeOutAlphaAnimationTimeMs)
            outState.putLong("scrollbarFadeOutInactivityTime", it.fadeOutInactivityTimeMs)
        }
        currentTopNormalCellIdx?.let { outState.putInt("currentTopNormalCellIdx", it) }
    }

    private fun restore(savedInstanceState: Bundle?) {
        isBind = savedInstanceState?.getBoolean("isBind") ?: false
        isViewCreated = savedInstanceState?.getBoolean("isViewCreated") ?: false
        scrollbarOptions = savedInstanceState?.let {
            LinearStickyListScrollbarOptions(
                isShowScrollbar = it.getBoolean("scrollbarIsShow"),
                widthDp = it.getFloat("scrollbarWidthDp"),
                drawableResId = it.getInt("scrollbarResId"),
                isFadeOut = it.getBoolean("scrollbarIsFadeOut"),
                fadeOutAlphaAnimationTimeMs = it.getLong("scrollbarFadeOutAnimTime"),
                fadeOutInactivityTimeMs = it.getLong("scrollbarFadeOutInactivityTime"),
            )
        } ?: LinearStickyListScrollbarOptions()
        currentTopNormalCellIdx = savedInstanceState?.getInt("currentTopNormalCellIdx")

        setup(
            restoredCells.toList().sortedBy { it.first }.map { it.second },
            scrollbarOptions
        )
        restoredCells.clear()

        currentTopNormalCellIdx?.let {
            val restoredTransY: Int = itemList[it].listPartsParent.top
            binding.scrollview.scrollTo(0, restoredTransY)
        }
    }

    private val restoredCells = mutableMapOf<Int, LinearStickyListCellFragmentBase>()
    fun cellFragmentIsRestored(cell: LinearStickyListCellFragmentBase, idx: Int) {
        restoredCells[idx] = cell
    }

    fun setup(
        list: List<LinearStickyListCellFragmentBase>,
        scrollbarOptions: LinearStickyListScrollbarOptions? = null
    ) {
        this.scrollbarOptions = scrollbarOptions ?: LinearStickyListScrollbarOptions()

        if (isViewCreated) {
            itemList.clear()
            list.forEachIndexed { idx, stickyHeaderItem ->
                itemList.add(StickyItem().set(stickyHeaderItem, idx))
            }
            binding.scrollview.setScrollChangedCallback { y ->
                originalScrollY = y
                moveStickyArea()
            }

            binding.scrollview.post {
                isBind = true
                setupScrollbar()
                moveStickyArea()
            }
        } else {
            bindStock = list
        }
    }

    private enum class State { FREE, STICKY, OVER }

    private fun moveStickyArea() {
        if (!isBind) return

        class PerStickyItemInfo() {
            lateinit var orgFragment: CustomFrameLayout
            lateinit var stickyFragment: CustomFrameLayout
            var state: State = State.FREE
            var orgY: Float = 0f
            var itemHeight: Float = 0f

            constructor(item: StickyItem) : this() {
                orgFragment = item.listPartsParent
                stickyFragment = item.stickyPartsParent!!
                orgY = (orgFragment.top - originalScrollY).toFloat()
                itemHeight =
                    stickyFragment.height.toFloat()
                state = State.FREE
            }
        }

        val infoList = mutableListOf<PerStickyItemInfo>()
        itemList.forEach {
            if (it.isStickyHeader()) {
                infoList.add(PerStickyItemInfo(it))
            }
        }

        var stickyHeight = 0f
        var oldItem: PerStickyItemInfo? = null
        infoList.forEach {
            if (it.orgY < stickyHeight) {
                oldItem?.let {
                    it.state = State.OVER
                }
                it.state = State.STICKY
                stickyHeight = it.itemHeight
                oldItem = it
            }
        }

        var stickyTransY = 0f
        infoList.reversed().forEach {
            when (it.state) {
                State.FREE -> {
                    it.stickyFragment.translationY = it.orgY
                }

                State.STICKY -> {
                    stickyTransY = if (it.orgY < 0f) 0f else it.orgY
                    it.stickyFragment.translationY = stickyTransY
                }

                State.OVER -> {
                    stickyTransY -= it.itemHeight
                    it.stickyFragment.translationY = stickyTransY
                }
            }
        }

        callOnDistanceFromDisplayArea()
        moveScrollBar()
    }

    private fun callOnDistanceFromDisplayArea() {
        val displayAreaHeight = binding.scrollview.height.toFloat()
        var currentTopNormalCellIdx: Int? = null

        fun call(
            fragment: LinearStickyListCellFragmentBase,
            top: Float,
            bottom: Float,
            isSticky: Boolean,
            idx: Int
        ) {
            if (bottom <= 0f) {
                fragment.onDistanceFromDisplayArea(isOnScreen = false, distancePx = -bottom)
            } else if (top >= displayAreaHeight) {
                fragment.onDistanceFromDisplayArea(
                    isOnScreen = false,
                    distancePx = top - displayAreaHeight
                )
            } else {
                if (!isSticky && currentTopNormalCellIdx == null) currentTopNormalCellIdx = idx
                fragment.onDistanceFromDisplayArea(isOnScreen = true, distancePx = 0f)
            }
        }

        itemList.forEachIndexed { idx, item ->
            item.stickyPartsContainer?.let {
                (it.getFragment<LinearStickyListCellFragmentBase>()).let { fragment ->
                    val top = item.stickyPartsParent?.translationY ?: 0f
                    val bottom = top + (item.stickyPartsParent?.height?.toFloat() ?: 0F)
                    call(fragment, top, bottom, true, idx)
                }
            } ?: run {
                (item.listPartsContainer?.getFragment<LinearStickyListCellFragmentBase>())?.let { fragment ->
                    val top = item.listPartsParent.top.toFloat() - originalScrollY
                    val bottom = top + item.listPartsParent.height.toFloat()
                    call(fragment, top, bottom, false, idx)
                }
            }
        }

        this.currentTopNormalCellIdx = currentTopNormalCellIdx
    }

    private fun setupScrollbar() {
        val listHeight = binding.list.height.toFloat()
        val screenHeight = binding.stickyArea.height.toFloat()

        scrollbarOptions?.let { options ->
            if (!options.isShowScrollbar || listHeight <= screenHeight) {
                binding.scrollbarArea.visibility = View.GONE

            } else {
                fun dpToPx(dp: Float) =
                    (requireContext().resources.displayMetrics.density * dp).toInt()

                binding.scrollbarArea.let { sbArea ->
                    sbArea.setBackgroundResource(scrollbarOptions!!.drawableResId)
                    sbArea.layoutParams.let {
                        it.width = dpToPx(options.widthDp)
                        scrollbarHeight =
                            (screenHeight * screenHeight) / listHeight
                        it.height = scrollbarHeight.toInt()
                        sbArea.layoutParams = it
                    }
                }
                moveScrollBar()
                binding.scrollbarArea.visibility = View.VISIBLE
            }
        } ?: run { binding.scrollbarArea.visibility = View.GONE }
    }

    private fun moveScrollBar() {
        val listHeight = binding.list.height.toFloat()
        val screenHeight = binding.stickyArea.height.toFloat()

        val newScrollBarHeight =
            (screenHeight * screenHeight) / listHeight
        if (newScrollBarHeight.toInt() != scrollbarHeight.toInt()) {
            binding.scrollbarArea.layoutParams.let {
                scrollbarHeight = newScrollBarHeight
                it.height = newScrollBarHeight.toInt()
                binding.scrollbarArea.layoutParams = it
            }
        }
        binding.scrollbarArea.translationY = (screenHeight * originalScrollY.toFloat()) / listHeight
        showScrollbar()
    }


    private inner class ScrollbarFadeoutRunnable : Runnable {
        override fun run() {
            fadeOutScrollbar()
            scrollbarFadeoutRunnable = null
        }

        private fun fadeOutScrollbar() {
            scrollbarOptions?.let { options ->
                if (options.isFadeOut) {
                    binding.scrollbarArea.let { v ->
                        v.alpha = 1f
                        v.animate()
                            .alpha(0f)
                            .setDuration(options.fadeOutAlphaAnimationTimeMs)
                    }
                }
            }
        }
    }

    private fun showScrollbar() {
        scrollbarOptions?.let { options ->
            if (options.isFadeOut) {
                binding.scrollbarArea.let { v ->
                    v.clearAnimation()
                    v.alpha = 1f
                    scrollbarFadeoutRunnable?.let { v.removeCallbacks(it) }
                    scrollbarFadeoutRunnable = ScrollbarFadeoutRunnable().also {
                        v.postDelayed(it, options.fadeOutInactivityTimeMs)
                    }
                }
            }
        }
    }

    fun getFragmentByIdx(idx: Int): LinearStickyListCellFragmentBase =
        if (0 <= idx && idx < itemList.size) {
            itemList[idx].let {
                if (it.isStickyHeader()) {
                    it.stickyPartsContainer?.getFragment<LinearStickyListCellFragmentBase>()
                        ?: throw (Exception("getFragmentByIdx($idx) Sticky cell not found."))
                } else {
                    it.listPartsContainer?.getFragment<LinearStickyListCellFragmentBase>()
                        ?: throw (Exception("getFragmentByIdx($idx) Normal cell not found."))
                }
            }
        } else throw (Exception("getFragmentByIdx($idx) is out of range /itemSize:${itemList.size}"))

    override fun onDestroy() {
        binding.scrollbarArea.clearAnimation()
        scrollbarFadeoutRunnable?.let { binding.scrollbarArea.removeCallbacks(it) }
        scrollbarFadeoutRunnable = null
        originalScrollY = 0
        super.onDestroy()
    }
}
