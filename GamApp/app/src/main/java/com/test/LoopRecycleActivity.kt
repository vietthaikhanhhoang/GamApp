package com.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.barservicegam.app.R
import com.fragmentcustom.PredictSlideBallFragment
import org.json.JSONArray

class LoopRecycleActivity : AppCompatActivity() {
    lateinit var layoutPredictSlideBall: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loop_recycle)

        layoutPredictSlideBall = findViewById(R.id.layoutPredictSlideBall)

        val predictSlideBallFragment = PredictSlideBallFragment.newInstance("", "")
        supportFragmentManager.beginTransaction().add(R.id.layoutPredictSlideBall, predictSlideBallFragment).commit()
    }
}