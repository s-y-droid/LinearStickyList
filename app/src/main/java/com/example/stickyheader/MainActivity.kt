package com.example.stickyheader

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sydroid.android.linearstickylist.LinearStickyListFragment
import com.example.stickyheader.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cellList = listOf(
            P1CellFragment.newInstance(),
            P2CellFragment.newInstance(patternNo = 0),
            P3CellFragment.newInstance(),
            P4CellFragment.newInstance(),
            P2CellFragment.newInstance(patternNo = 1),
            P1CellFragment.newInstance(),
            P5CellFragment.newInstance(),
        )
        getLinearStickyListFragment().bind(cellList)

        supportFragmentManager.setFragmentResultListener("P1BtnClicked", this) { _, bundle ->
            Toast.makeText(this, "P1BtnClicked. #${bundle.getInt("idx")}", Toast.LENGTH_SHORT)
                .show()
        }

        supportFragmentManager.setFragmentResultListener("P3BtnClicked", this) { _, _ ->
            Toast.makeText(this, "P3BtnClicked. Sends an event to the P4 cell.", Toast.LENGTH_SHORT)
                .show()
            (cellList[3] as? P4CellFragment)?.onP3BtnClicked()
        }
    }

    private fun getLinearStickyListFragment() =
        binding.stickyHeaderFragment.getFragment<LinearStickyListFragment>()
}
