package com.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.barservicegam.app.R
import com.lib.toDp
import com.lib.toPx

class PositionActivity : AppCompatActivity() {

    lateinit var viewDemo: View
    lateinit var btnClick: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_position)

        viewDemo = findViewById(R.id.viewDemo)
        btnClick = findViewById(R.id.btnClick)

        btnClick.setOnClickListener{

            Log.d("vietnb", "log doan 1")

            val layoutParam = viewDemo.layoutParams
            layoutParam.width = 150.toPx()
            viewDemo.layoutParams = layoutParam

            Log.d("vietnb", "log doan 2")

            layoutParam.width = 200.toPx()
            viewDemo.layoutParams = layoutParam

            Log.d("vietnb", "log doan 3")

            viewDemo.setBackgroundColor(R.color.com_facebook_button_background_color_pressed);
        }

    }
}