package com.main.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.barservicegam.app.R
import data.DataPreference

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val lblName = findViewById<TextView>(R.id.tvName)
        val dataPreference:DataPreference = DataPreference(this)
        if(dataPreference.getValueString("name") != null)
        {
            lblName.text = "Ten dang nhap: " + dataPreference.getValueString("name")
        }
    }
}