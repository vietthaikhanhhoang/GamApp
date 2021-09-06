package com.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.barservicegam.app.R
import com.lib.DatabaseHelper

class DatabaseActivity : AppCompatActivity() {
    internal var dbHelper = DatabaseHelper(this)

    lateinit var btnAddProduct: Button
    lateinit var btnViewAllProduct: Button
    lateinit var btnUpdateProduct: Button
    lateinit var btnDeleteProduct: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)

        btnAddProduct = findViewById(R.id.btnAddProduct)
        btnAddProduct.setOnClickListener{
            try {
                dbHelper.addProduct("Tủ lạnh","Chất lượng Panasonic",
                    1)
                Log.d("vietnb", "addProduct successful")
            }catch (e: Exception){
                e.printStackTrace()
                Log.d("vietnb", "addProduct fail")
            }
        }

        btnViewAllProduct = findViewById(R.id.btnViewAllProduct)
        btnViewAllProduct.setOnClickListener{
            val res = dbHelper.allProduct
            Log.d("vietnb", "tong: ${res.count}")

            while (res.moveToNext()) {
                val ID = res.getString(0)
                val NAME = res.getString(1)
                val DESC = res.getString(2)
                val TYPE = res.getString(3)

                Log.d("vietnb", "ID: ${ID} , NAME: ${NAME}, DESC: ${DESC}, TYPE: ${TYPE}")
            }
        }

        btnUpdateProduct = findViewById(R.id.btnUpdateProduct)
        btnUpdateProduct.setOnClickListener{
            Log.d("vietnb", "update data")
            val isUpdate = dbHelper.updateProduct("1", "Xe tăng", "Chất lượng Đức", 2)
            if (isUpdate == true)
                Log.d("vietnb", "updateProduct successful")
            else
                Log.d("vietnb", "updateProduct fail")
        }

        btnDeleteProduct = findViewById(R.id.btnDeleteProduct)
        btnDeleteProduct.setOnClickListener{
            try {
                Log.d("vietnb", "xoa data")
                val isUpdate = dbHelper.deleteProduct("1")
            }catch (e: Exception){
                e.printStackTrace()
                Log.d("vietnb", "updateProduct fail")
            }
        }
    }
}