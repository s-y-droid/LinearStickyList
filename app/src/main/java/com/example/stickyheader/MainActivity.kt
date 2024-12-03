package com.example.stickyheader

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.libstickyheader.StickyHeaderFragment
import com.example.stickyheader.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getStickyHeaderFragment().bind(
            // Note: Specify here a list of all the Fragments that make up the screen.
            listOf(
                P1Fragment.newInstance(),
                P2Fragment.newInstance(),
                P3Fragment.newInstance(),
                P4Fragment.newInstance(),
                P1Fragment.newInstance(),
                P2Fragment.newInstance(),
                P3Fragment.newInstance(),
                P4Fragment.newInstance(),
                P1Fragment.newInstance(),
                P2Fragment.newInstance(),
                P3Fragment.newInstance(),
                P4Fragment.newInstance(),
            )
        )

        supportFragmentManager.setFragmentResultListener("P1BtnClicked", this) { _, _ ->
            Toast.makeText(this, "P1BtnClicked", Toast.LENGTH_LONG).show()
        }
    }

    private fun getStickyHeaderFragment() =
        binding.stickyHeaderFragment.getFragment<StickyHeaderFragment>()
}
