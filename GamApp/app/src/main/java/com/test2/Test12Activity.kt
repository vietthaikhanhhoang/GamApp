package com.test2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.barservicegam.app.R

class Test12Activity : AppCompatActivity() {
    lateinit var btnMan2: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test12)

        btnMan2 = findViewById(R.id.btnMan2)
        btnMan2.setOnClickListener{
            actionBack()
        }
    }

    fun actionBack(){
        this.onBackPressed()
    }
}