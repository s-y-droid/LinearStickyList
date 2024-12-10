package com.example.libstickyheader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.libstickyheader.databinding.StickyHeaderListFragmentBinding
import com.example.libstickyheader.impl.CustomFrameLayout

class StickyHeaderListFragment : Fragment() {

    private lateinit var binding: StickyHeaderListFragmentBinding
    private val itemList = mutableListOf<StickyItem>()
    private var originalScrollY: Int = 0
    private var isBind = false
    private var isViewCreated = false
    private var bindStock: List<StickyHeaderListCellFragmentBase>? = null
    private var scrollbarOptions: IScrollBarOptions? = null
    private var scrollbarHeight: Float = 0f
    private var scrollbarFadeoutRunnable: ScrollbarFadeoutRunnable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = StickyHeaderListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private inner class StickyItem {
        lateinit var listPartsParent: CustomFrameLayout
        var listPartsContainer: FragmentContainerView? = null
        var listPartsView: StickyHeaderListCellFragmentBase? = null
        var stickyPartsParent: CustomFrameLayout? = null
        var stickyPartsContainer: FragmentContainerView? = null
        var stickyPartsView: StickyHeaderListCellFragmentBase? = null

        fun set(original: StickyHeaderListCellFragmentBase): StickyItem {
            val isStickyHeader = original.isStickyHeader()

            if (isStickyHeader) {
                listPartsParent =
                    CustomFrameLayout(requireContext()).also {
                        it.setOnLayoutCallback(::moveStickyArea)
                    }
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
                addFragmentToContainer(stickyPartsContainer!!, original)

            } else {
                listPartsView = original
                val orgContainer = makeContainer()
                listPartsContainer = orgContainer
                listPartsParent =
                    CustomFrameLayout(requireContext()).also { it.setOnLayoutCallback(::moveStickyArea) }
                listPartsParent.addView(orgContainer)
                binding.list.addView(listPartsParent)
                addFragmentToContainer(orgContainer, original)
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

        private fun addFragmentToContainer(container: FragmentContainerView, fragment: Fragment) {
            val transaction = childFragmentManager.beginTransaction()
            transaction.replace(container.id, fragment)
            transaction.commit()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
        bindStock?.let {
            bind(it)
            bindStock = null
        }
    }

    class DefaultScrollbarOptions : IScrollBarOptions

    fun bind(
        list: List<StickyHeaderListCellFragmentBase>,
        scrollbarOptions: IScrollBarOptions? = null,
        onCompleteBinding: ((scrollView: ScrollView) -> Unit)? = null
    ) {
        this.scrollbarOptions = scrollbarOptions ?: DefaultScrollbarOptions()

        if (isViewCreated) {
            itemList.clear()
            list.forEach { stickyHeaderItem ->
                itemList.add(StickyItem().set(stickyHeaderItem))
            }
            binding.scrollview.setScrollChangedCallback { y ->
                originalScrollY = y
                moveStickyArea()
            }

            binding.scrollview.post {
                isBind = true
                setupScrollbar()
                moveStickyArea()
                onCompleteBinding?.invoke(binding.scrollview)
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
        fun call(fragment: StickyHeaderListCellFragmentBase, top: Float, bottom: Float) {
            if (bottom <= 0f) {
                fragment.onDistanceFromDisplayArea(isOnScreen = false, distancePx = -bottom)
            } else if (top >= displayAreaHeight) {
                fragment.onDistanceFromDisplayArea(
                    isOnScreen = false,
                    distancePx = top - displayAreaHeight
                )
            } else {
                fragment.onDistanceFromDisplayArea(isOnScreen = true, distancePx = 0f)
            }
        }

        itemList.forEach { item ->
            item.stickyPartsContainer?.let {
                (it.getFragment<StickyHeaderListCellFragmentBase>()).let { fragment ->
                    if (fragment.isCallOnDistanceFromDisplayArea()) {
                        val top = item.stickyPartsParent?.translationY ?: 0f
                        val bottom = top + (item.stickyPartsParent?.height?.toFloat() ?: 0F)
                        call(fragment, top, bottom)
                    }
                }
            } ?: run {
                (item.listPartsContainer?.getFragment<StickyHeaderListCellFragmentBase>())?.let { fragment ->
                    if (fragment.isCallOnDistanceFromDisplayArea()) {
                        val top = item.listPartsParent.top.toFloat() - originalScrollY
                        val bottom = top + item.listPartsParent.height.toFloat()
                        call(fragment, top, bottom)
                    }
                }
            }
        }
    }

    private fun setupScrollbar() {
        val listHeight = binding.list.height.toFloat()
        val screenHeight = binding.stickyArea.height.toFloat()

        scrollbarOptions?.let { options ->
            if (!options.isShowScrollbar() || listHeight <= screenHeight) {
                binding.scrollbarArea.visibility = View.GONE

            } else {
                fun dpToPx(dp: Float) =
                    (requireContext().resources.displayMetrics.density * dp).toInt()

                binding.scrollbarArea.let { sbArea ->
                    sbArea.setBackgroundResource(scrollbarOptions!!.drawableResId())
                    sbArea.layoutParams.let {
                        it.width = dpToPx(options.widthDp())
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
                if (options.isFadeOut()) {
                    binding.scrollbarArea.let { v ->
                        v.alpha = 1f
                        v.animate()
                            .alpha(0f)
                            .setDuration(options.isFadeOutAlphaAnimationTimeMs())
                    }
                }
            }
        }
    }

    private fun showScrollbar() {
        scrollbarOptions?.let { options ->
            if (options.isFadeOut()) {
                binding.scrollbarArea.let { v ->
                    v.clearAnimation()
                    v.alpha = 1f
                    scrollbarFadeoutRunnable?.let { v.removeCallbacks(it) }
                    scrollbarFadeoutRunnable = ScrollbarFadeoutRunnable().also {
                        v.postDelayed(it, options.isFadeOutInactivityTimeMs())
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        binding.scrollbarArea.clearAnimation()
        scrollbarFadeoutRunnable?.let { binding.scrollbarArea.removeCallbacks(it) }
        scrollbarFadeoutRunnable = null
        originalScrollY = 0
        isBind = false
        isViewCreated = false
        scrollbarOptions = null
        itemList.clear()
        binding.list.removeAllViews()
        binding.stickyArea.removeAllViews()
    }
}