package com.test

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.barservicegam.app.R
import com.fragmentcustom.CommentFragment
import com.lib.screenSizeInDp
import com.lib.toPx


class KeyboardActivity : AppCompatActivity() {
    val ID_ContentFragment = 11
    val ID_ContentFragment112 = 12
    lateinit var btnComment: Button

    lateinit var layoutConstraint:ConstraintLayout

    fun hideKeyboard(context: Context) {
        try {
            (context as Activity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            if ((context as Activity).currentFocus != null && (context as Activity).currentFocus!!
                    .windowToken != null
            ) {
                (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    (context as Activity).currentFocus!!.windowToken, 0
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showKeyboard(context: Context) {
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    fun addFragment(){
        val childCt = ConstraintLayout(this)
        childCt.setBackgroundColor(Color.BLUE)
        childCt.id = ID_ContentFragment

        childCt.layoutParams = ConstraintLayout.LayoutParams(
            200.toPx(),
            200.toPx()
        )

//        val commentFragment = CommentFragment.newInstance("Hello", "World")
//        supportFragmentManager
//            .beginTransaction().add(childCt.id, commentFragment).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keyboard)
        layoutConstraint = findViewById(R.id.layoutConstraint)

        val childCt = ConstraintLayout(this)
        childCt.setBackgroundColor(Color.BLUE)
        childCt.id = ID_ContentFragment

        childCt.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            200.toPx()
        )
        layoutConstraint.addView(childCt)

        val commentFragment = CommentFragment.newInstance("", "")
        supportFragmentManager.beginTransaction().add(childCt.id, commentFragment).commit()
        var constraintSet = ConstraintSet()
        constraintSet.clone(layoutConstraint)
        constraintSet.connect(childCt.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 1600)
        constraintSet.connect(childCt.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        constraintSet.applyTo(layoutConstraint)


        btnComment = findViewById(R.id.btnComment)
        btnComment.setOnClickListener{
            Log.d("vietnb", "heheheheehehehe")
            commentFragment.showSoftKeyboard(this)
        }

    }
}