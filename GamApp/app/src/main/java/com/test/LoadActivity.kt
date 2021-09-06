package com.test

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Color
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.barservicegam.app.R
import com.fragmentcustom.ContentFragment
import com.fragmentcustom.ContentFragment.ContentFragmentListener
import com.lib.Utils
import com.lib.hideSoftKeyboard
import com.lib.toPx


class LoadActivity : AppCompatActivity() {
    lateinit var cstParentFragmentContent:ConstraintLayout
    lateinit var cstComment: ConstraintLayout
    lateinit var txtEditText: EditText
    lateinit var txtComment: EditText
    lateinit var btnFocus:Button
    val ID_ContentFragment = 100

    fun addChildFragment(){

        //tao constraint con

        val childCt = ConstraintLayout(this)
        childCt.setBackgroundColor(Color.BLUE)
        childCt.id = ID_ContentFragment

        childCt.layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                200.toPx()
        )
        cstParentFragmentContent.addView(childCt)

        //apply toa do cho constraint con
        val constraintSet = ConstraintSet()
        constraintSet.clone(cstParentFragmentContent)
        constraintSet.connect(
                ID_ContentFragment,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0
        )
        constraintSet.connect(
                ID_ContentFragment,
                ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.LEFT,
                0
        )
        constraintSet.applyTo(cstParentFragmentContent)

//        //add thang framgent vao trong thang constraint con
//        val contentFragment = ContentFragment.newInstance("Hello", "World").ContentFragment(ContentFragment.ContentFragmentListener(){
//
//        })

        val contentFragment = ContentFragment.newInstance("Hello", "World")
        contentFragment.setContentFragmentListener(object : ContentFragmentListener {
            override fun showText(title: String?) {
                Log.d("vietnb", "ngot choet")
            }
        })

        //if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction().add(childCt.id, contentFragment).commit()
        //}
    }
    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val inputMethodManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load)
        Utils.hiddenBottomBarAndStatusWhite(this)

        //lay contraint cha
        cstParentFragmentContent = findViewById<ConstraintLayout>(R.id.cstParentFragmentContent)
        cstComment = findViewById<ConstraintLayout>(R.id.cstComment)
        cstComment.visibility = GONE

        txtEditText = findViewById<EditText>(R.id.txtEditText)
        txtComment = findViewById<EditText>(R.id.txtComment)
        btnFocus = findViewById<Button>(R.id.btnFocus)

        btnFocus.setOnClickListener{
            this.showSoftKeyboard(txtComment)
            cstComment.visibility = VISIBLE
            txtComment.isCursorVisible = false
            Log.d("vietnb", "goi len luon")
        }

        // root constraint layout click listener
        cstParentFragmentContent.setOnClickListener {
            this.hideSoftKeyboard()
            this.txtEditText.clearFocus()
            this.txtComment.clearFocus()
            cstComment.visibility = GONE
        }

//        Utils.hiddenStatusBarAndBottomBar(this)
//        Utils.hiddenBottomBarAndStatusBlack(this)
//
//        val btnShow = findViewById<Button>(R.id.btnShow)
//        btnShow.setOnClickListener{
//            Utils.hiddenBottomBarAndStatusWhite(this)
//        }
//
//        val btnHidden = findViewById<Button>(R.id.btnHidden)
//        btnHidden.setOnClickListener{
//            Utils.hiddenBottomBarAndStatusBlack(this)
//        }
//
//        /////Lay chieu rong chieu cao man hinh
//        Log.d("vietnb", "chieu rong: " + Utils.getScreenWidth().toString()) //1080px : 2.75density -> 392dp
//        Log.d("vietnb", "chieu cao: " + Utils.getScreenHeight().toString()) //2088px -> 759dp

        ////Check density
//        Log.d("vietnb", "density: " + Utils.density(this))//2.75
//        Log.d("vietnb", "pixel to dp: " + 1.toPx()) // 2

//        Log.d("vietnb", "height appBar: " + Utils.getHeightAppBar(this).toString()) //56
//
//        var displayMetrics=getResources().getDisplayMetrics()
//        var screenWidthInDp=displayMetrics.widthPixels/displayMetrics.density
//        var screenHeightInDp=displayMetrics.heightPixels/displayMetrics.density
//
//        Log.d("vietnb", "chieu rong: " + screenWidthInDp.toString())
//        Log.d("vietnb", "chieu cao: " + screenHeightInDp.toString())
//
//
//        screenSizeInDp.apply {
//            // screen width in dp
//            Log.d("vietnb", "\nWidth AA : $x dp")
//            Log.d("vietnb", "\nHeight BB : $y dp")
//        }
    }
    ///  Get Screen width & height including top & bottom navigation bar
}


