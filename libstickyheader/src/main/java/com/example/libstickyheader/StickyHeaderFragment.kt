package com.example.libstickyheader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.libstickyheader.databinding.StickyHeaderFragmentBinding
import com.example.libstickyheader.impl.CustomFrameLayout

class StickyHeaderFragment : Fragment() {

    private lateinit var binding: StickyHeaderFragmentBinding
    private val itemList = mutableListOf<StickyItem>()
    private var originalScrollY: Int = 0
    private var isBind = false
    private var isViewCreated = false
    private var bindStock: List<IStickyHeaderViewParts>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = StickyHeaderFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private inner class StickyItem {
        lateinit var listPartsParent: CustomFrameLayout
        lateinit var listPartsContainer: FragmentContainerView
        lateinit var listPartsView: IStickyHeaderViewParts
        var stickyPartsParent: CustomFrameLayout? = null
        var stickyPartsContainer: FragmentContainerView? = null
        var stickyPartsView: IStickyHeaderViewParts? = null

        fun set(original: IStickyHeaderViewParts): StickyItem {
            listPartsView = original
            val orgContainer = makeContainer()
            listPartsContainer = orgContainer
            listPartsParent =
                CustomFrameLayout(requireContext()).also { it.setOnLayoutCallback(::moveStickyArea) }
            listPartsParent.addView(orgContainer)
            binding.list.addView(listPartsParent)
            addFragmentToContainer(orgContainer, original.fragment())

            if (original.isStickyHeader()) {
                val stickyContainer = makeContainer()
                orgContainer.visibility = View.INVISIBLE
                stickyPartsContainer = stickyContainer
                stickyPartsParent =
                    CustomFrameLayout(requireContext()).also { it.setOnLayoutCallback(::moveStickyArea) }
                stickyPartsParent!!.addView(stickyContainer)
                binding.stickyArea.addView(stickyPartsParent)
                original.makeStickyInstance().also {
                    stickyPartsView = it
                    addFragmentToContainer(stickyContainer, it.fragment())
                }
            }
            return this
        }

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
            transaction.commitNow()
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

    fun bind(list: List<IStickyHeaderViewParts>) {
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
                itemHeight = (orgFragment.bottom - orgFragment.top).toFloat()
                state = State.FREE
            }
        }

        val infoList = mutableListOf<PerStickyItemInfo>()
        itemList.forEach {
            if (it.listPartsView.isStickyHeader()) {
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
    }

    override fun onDestroy() {
        super.onDestroy()
        originalScrollY = 0
        isBind = false
        isViewCreated = false
        itemList.clear()
        binding.list.removeAllViews()
        binding.stickyArea.removeAllViews()
    }
}