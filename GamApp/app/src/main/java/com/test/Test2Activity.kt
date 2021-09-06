package com.test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.FragmentManager
import com.barservicegam.app.R
import com.fragmentcustom.LoginFragment
import com.lib.eventbus.EventBusFire
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class Test2Activity : AppCompatActivity() {
    val ID_1 = 1
    val ID_2 = 2
    lateinit var constraintLayout:ConstraintLayout

    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onMessageEvent(event: EventBusFire) { /* Do something */
        Log.d("vietnb", "nhan eventbusfire:" + event.eventName + " || " + event.valueString)
    }

    override fun onStart() {
        super.onStart()
//        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
//        EventBus.getDefault().unregister(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test2)

        EventBus.getDefault().register(this)
        Log.d("vietnb", "dang ky")

        val constraintLayoutDynamic = ConstraintLayout(this)

        constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)

        val button = Button(this)
        button.text = "Button 1"
        button.id = ID_1
        val lp = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        button.layoutParams = lp
        constraintLayout.addView(button)
        button.setOnClickListener{
//            Toast.makeText(this, "Goi ham LoginFragment", Toast.LENGTH_SHORT)
            Log.d("vietnb", "goi ham loginfragment")
            val fm: FragmentManager = supportFragmentManager
            val popupDialogFrag = LoginFragment.newInstance("ho1", "hi2")
            popupDialogFrag.show(fm, "popup")
            popupDialogFrag.onResult = { sum, text ->
                // do something
                Log.d("vietnb", sum.toString() + " || " + text.toString())
            }
        }

        val button2 = Button(this)

        button2.text = "Button 2"
        button2.id = ID_2
        button2.setOnClickListener{
            Log.d("vietnb", "goi sang activity 3")
            var intent = Intent(this, TestActivity3::class.java)
            startActivity(intent)
        }
        constraintLayout.addView(button2)

        val constraintSet = ConstraintSet()
        //Copy all the previous constraints present in the constraint layout.
        constraintSet.clone(constraintLayout)

        constraintSet.connect(
            ID_1,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP,
            30
        )
        constraintSet.connect(
            ID_1,
            ConstraintSet.LEFT,
            ConstraintSet.PARENT_ID,
            ConstraintSet.LEFT,
            30
        )

        constraintSet.connect(ID_2, ConstraintSet.TOP, ID_1, ConstraintSet.BOTTOM)
        constraintSet.connect(ID_2, ConstraintSet.LEFT, ID_1, ConstraintSet.RIGHT)
        constraintSet.applyTo(constraintLayout)
    }
}