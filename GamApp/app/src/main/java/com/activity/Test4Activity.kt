package com.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentContainerView
import com.barservicegam.app.R
import com.fragmentcustom.ContentFragment
import com.fragmentcustom.RelativeFragment

class Test4Activity : AppCompatActivity() {
//    lateinit var relativeFragment: FrameLayout
//    lateinit var layoutParent: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test4)

        val navFragment = supportFragmentManager.findFragmentById(R.id.relativeFragment) as RelativeFragment
//        navFragment.refreshData()

//        relativeFragment = findViewById(R.id.relativeFragment)

//        val ahihi = relativeFragment as RelativeFragment
//        relativeFragment.refreshData()

//        layoutParent = findViewById(R.id.layoutParent)

//        val contentFragment = RelativeFragment.newInstance("Hello", "World")
//        contentFragment.refreshData()
//        //if (savedInstanceState == null) {
//        supportFragmentManager
//            .beginTransaction().add(layoutParent.id, contentFragment).commit()
//        //}
    }
}