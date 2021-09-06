package com.test

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.barservicegam.app.R
import com.lib.eventbus.EventBusFire
import org.greenrobot.eventbus.EventBus

class TestActivity3 : AppCompatActivity() {
    lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test3)

        btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener{

            EventBus.getDefault().post(EventBusFire("startEvent", valueString = "hello world"))
            Log.d("vietnb", "post eventbusfire")

            this.onBackPressed()
        }
    }
}