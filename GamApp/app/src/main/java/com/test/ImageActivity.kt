package com.test

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.barservicegam.app.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.lib.Utils
import com.lib.toDp
import com.lib.toPx


class ImageActivity : AppCompatActivity() {
    lateinit var imgView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        imgView = findViewById(R.id.imgView)

        Glide.with(this)
            .load("https://i1-vnexpress.vnecdn.net/2021/08/16/233199211-491056362301128-4427-7710-3819-1629109676.jpg?w=680&h=0&q=100&dpr=2&fit=crop&s=zBUnzcfDTMaMmY5TqIPk9A")
            .into(object : CustomTarget<Drawable?>() {
                override fun onResourceReady(
                    resource: Drawable,
                    @Nullable transition: Transition<in Drawable?>?
                ) {
                    val bitmap = (resource as BitmapDrawable).bitmap
                    Log.d("vietnb", "chieu rong: " + bitmap.width)
                    Log.d("vietnb", "chieu cao: " + bitmap.height)

                    imgView.setImageBitmap(bitmap)
                    val width = Utils.getScreenWidth() - 32 //theo dp
                    val height:Float = (width * bitmap.height)/bitmap.width //kich thuoc that cua anh

                    val param = imgView.layoutParams
                    param.height = height.toInt().toPx() //gan len px phai day lai
                    imgView.layoutParams = param
                }

                override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
                override fun onLoadFailed(@Nullable errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    Toast.makeText(
                        this@ImageActivity,
                        "Failed to Download Image! Please try again later.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}