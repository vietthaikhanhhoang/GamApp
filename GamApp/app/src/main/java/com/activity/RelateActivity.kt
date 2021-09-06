package com.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.barservicegam.app.R
import com.fragmentcustom.RelativeFragment

class RelateActivity : AppCompatActivity() {
    lateinit var layoutContainer: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_relate)

        layoutContainer = findViewById(R.id.layoutContainer)

        val relativeFragment = RelativeFragment.newInstance("", "")
        supportFragmentManager.beginTransaction().add(layoutContainer.id, relativeFragment).commit()
    }
}