package com.example.stickyheader

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sydroid.android.linearstickylist.LinearStickyListFragment
import com.example.stickyheader.databinding.ActivityMainBinding
import com.sydroid.android.linearstickylist.LinearStickyListScrollbarOptions

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState ?: run {
            // Note #1
            // Place the LinearStickyListFragment and call the setup() method.
            // In the setup() method, specify the Fragments for all cells that make up the LinearStickyListFragment.
            // (This Fragment is referred to as the "Cell Fragment" here.)
            //
            // Do not hold onto Cell Fragment in the caller, because it will be recreated.
            // See Note #2 for information on how to safely retrieve the current Cell Fragment.
            getLinearStickyListFragment().setup(
                list = listOf(
                    P1CellFragment.newInstance(),
                    P2CellFragment.newInstance(patternNo = 0),
                    P3CellFragment.newInstance(),
                    P4CellFragment.newInstance(),
                    P2CellFragment.newInstance(patternNo = 1),
                    P1CellFragment.newInstance(),
                    P5CellFragment.newInstance(),
                ),
                // Note #5 (Optional)
                // Specify this if you want to change the design or functionality of the scrollbar.
                // The default scrollbar has a design similar to ScrollView.
                // The parameters are as follows:
                //
                // val isShowScrollbar : Boolean = true
                //     ... true if scrollbars are displayed on screen
                //
                // val widthDp : Float = 4.0f
                //     ... Only valid when isShowScrollbar=true.
                //         Scrollbar width (unit: Dp)
                //
                // val drawableResId : Int = R.drawable.linear_sticky_list_defalut_scrollbar,
                //     ... Only valid when isShowScrollbar=true.
                //         The resource ID of the drawable to apply to the scrollbar.
                //
                // val isFadeOut : Boolean = true,
                //     ... Only valid when isShowScrollbar=true.
                //         Whether to hide the scrollbars if no scrolling occurs for a while.
                //
                // val fadeOutAlphaAnimationTimeMs : Long = 600L,
                //     ... Only valid when isFadeOut=true.
                //         Time for the scrollbar to fade out (Unit: ms)
                //
                // val fadeOutInactivityTimeMs : Long = 1200L
                //     ... Only valid when isFadeOut=true.
                //         The amount of inactivity time required for the scrollbar to fade out. (Unit: ms)
                scrollbarOptions = LinearStickyListScrollbarOptions()
            )
        }

        supportFragmentManager.setFragmentResultListener("P1BtnClicked", this) { _, bundle ->
            Toast.makeText(this, "P1BtnClicked. #${bundle.getInt("idx")}", Toast.LENGTH_SHORT)
                .show()
        }

        supportFragmentManager.setFragmentResultListener("P3BtnClicked", this) { _, _ ->
            Toast.makeText(
                this,
                "P3BtnClicked. Sends an event to the P4 cell.",
                Toast.LENGTH_SHORT
            ).show()

            (getCellFragment(3) as? P4CellFragment)?.onP3BtnClicked()
        }
    }

    // Note #2
    //　To safely get the current Cell Fragment, use the getFragmentByIdx() method of LinearStickyListFragment.
    private fun getCellFragment(idx: Int) =
        getLinearStickyListFragment().getFragmentByIdx(idx)

    private fun getLinearStickyListFragment() =
        binding.stickyHeaderFragment.getFragment<LinearStickyListFragment>()
}
