package com.test2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.barservicegam.app.R

class Test11Activity : AppCompatActivity() {
    lateinit var btnMan1: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test11)

        btnMan1 = findViewById(R.id.btnMan1)
        btnMan1.setOnClickListener{
            val intent = Intent(btnMan1.context, Test12Activity::class.java)
            startActivity(intent)
        }

    }
}