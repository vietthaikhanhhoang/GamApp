package com.test

import android.os.Bundle
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.barservicegam.app.R
import com.lib.Utils
import com.lib.hideSoftKeyboard
import com.lib.toDp
import com.lib.toPx


class MenuActivity : AppCompatActivity() {
    lateinit var txtScreen: TextView

    lateinit var btnMenu: Button
    lateinit var imgBall: ImageView
    lateinit var btnPoint: Button

    lateinit var txtPointX: EditText
    lateinit var txtPointY: EditText

    var isShow:Boolean = false

    lateinit  var cstParentFragmentContent:ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        // root constraint layout click listener

        cstParentFragmentContent = findViewById(R.id.cstParentFragmentContent)
        cstParentFragmentContent.setOnClickListener {
            this.hideSoftKeyboard()
            this.txtPointX.clearFocus()
            this.txtPointY.clearFocus()
        }

        Utils.hiddenBottomBarAndStatusWhite(this)

        txtScreen = findViewById(R.id.txtScreen)
        val width = Utils.getScreenWidth()
        val height = Utils.getScreenHeight()
        val heightStatus = Utils.getStatusBarHeight(this)
        val heightBottomBar = Utils.getHeightBottomBar(this)
        txtScreen.text = "w:$width, h:$height, status:$heightStatus, bottom:$heightBottomBar"

        btnMenu = findViewById(R.id.btnMenu)
        imgBall = findViewById(R.id.imgBall)
        btnPoint = findViewById(R.id.btnPoint)
        txtPointX = findViewById(R.id.txtPointX)
        txtPointY = findViewById(R.id.txtPointY)

        btnMenu.setOnClickListener{
            tranY()
        }

        btnPoint.setOnClickListener{
            imgBall.clearAnimation()
            val x:Int = txtPointX.text.toString().toInt()
            val y:Int = txtPointY.text.toString().toInt()

            imgBall.x = x.toPx().toFloat()
            imgBall.y = y.toPx().toFloat()
        }
    }

    fun tranX(){
        if(!isShow) {
            isShow = true
            val animation = TranslateAnimation(
                0.0f, 400.0f,
                0.0f, 0.0f
            ) //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)

            animation.setFillAfter(true)

            animation.duration = 1000/2 // animation duration
            imgBall.startAnimation(animation);  // start animation
        } else {
            isShow = false
            val animation = TranslateAnimation(
                400.0f, 0.0f,
                0.0f, 0.0f
            ) //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)

            animation.setFillAfter(true)

            animation.duration = 1000/2 // animation duration
            imgBall.startAnimation(animation);  // start animation
        }
    }

    fun tranY() {
        if(!isShow) {
            isShow = true
            val animation = TranslateAnimation(
                0.0f, 0.0f,
                0.0f, -400.0f
            ) //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)

            animation.setFillAfter(true)

            animation.duration = 200 // animation duration
            imgBall.startAnimation(animation);  // start animation
        } else {
            isShow = false
            val animation = TranslateAnimation(
                .0f, 0.0f,
                -400.0f, 0.0f
            ) //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)

            animation.setFillAfter(true)

            animation.duration = 200 // animation duration
            imgBall.startAnimation(animation);  // start animation
        }
    }
}