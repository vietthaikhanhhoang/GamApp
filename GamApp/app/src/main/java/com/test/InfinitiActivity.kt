package com.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.barservicegam.app.R
import com.customadapter.SliderAdapter
import data.SlideItem

class InfinitiActivity : AppCompatActivity() {
    lateinit var viewPager: ViewPager2
    var sliderHander = Handler()

    lateinit var btnActive: Button
    lateinit var txtIndex: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infiniti)

        viewPager = findViewById(R.id.viewPager)
        btnActive = findViewById(R.id.btnActive)

        txtIndex = findViewById(R.id.txtIndex)

        var slideItems = ArrayList<SlideItem>()
        slideItems.add(SlideItem(R.drawable.img_chania))
        slideItems.add(SlideItem(R.drawable.img_flower))
        slideItems.add(SlideItem(R.drawable.img_flower2))
        slideItems.add(SlideItem(R.drawable.hot))
        slideItems.add(SlideItem(R.drawable.tin6))

        val sliderAdapter = SliderAdapter(slideItems, viewPager)
        viewPager.adapter = sliderAdapter

        viewPager.clipToPadding = false
        viewPager.clipChildren = false
        viewPager.offscreenPageLimit = 3
        viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))
        compositePageTransformer.addTransformer(ViewPager2.PageTransformer { page, position ->
            val r = 1 - Math.abs(position)
            page.scaleY = (0.85f + r + 0.15f)
        })

        viewPager.setPageTransformer(compositePageTransformer)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHander.removeCallbacks(sliderRunable)
//                sliderHander.postDelayed(sliderRunable, 3000)
            }
        })

        btnActive.setOnClickListener{
//            val index = txtIndex.text.toString().toInt()
//            viewPager.setCurrentItem(index)
//
//            Log.d("vietnb", "kiem tra mang ${slideItems.size}")

            val intent = Intent(btnActive.context, LoopRecycleActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
//        sliderHander.postDelayed(sliderRunable, 3000)
    }

    override fun onPause() {
        super.onPause()
//        sliderHander.removeCallbacks(sliderRunable)
    }

    val sliderRunable = object : Runnable {
        override fun run() {
//            viewPager.setCurrentItem(viewPager.currentItem + 1)
        }
    }
}