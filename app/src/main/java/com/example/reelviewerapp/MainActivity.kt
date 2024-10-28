package com.example.reelviewerapp

import androidx.viewpager2.widget.ViewPager2
import com.example.reelviewerapp.ReelPagerAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity



class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: ReelPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        adapter = ReelPagerAdapter(this)

        // Set the ViewPager2 orientation to vertical
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL

        viewPager.adapter = adapter
    }
}

