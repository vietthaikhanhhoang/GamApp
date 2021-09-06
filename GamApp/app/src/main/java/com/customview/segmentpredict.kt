package com.customview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.barservicegam.app.R
import com.lib.toPx

class segmentpredict @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {
    lateinit var viewWin: View
    lateinit var viewDraw: View
    lateinit var viewLose: View

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.segmentpredict_view, this)
        viewWin = view.findViewById(R.id.viewWin)
        viewDraw = view.findViewById(R.id.viewDraw)
        viewLose = view.findViewById(R.id.viewLose)
    }

    fun rateSegmentPredict(win: Int, draw: Int, lose: Int){
        val lPWin = viewWin.layoutParams
        val lPDraw = viewDraw.layoutParams
        val lPLose = viewLose.layoutParams

        val layoutParam = this.layoutParams

        if(win == 0 && draw == 0 && lose == 0)
        {
            Log.d("vietnb", "no vao trong nay $width")
            viewWin.setBackgroundColor(R.color.com_facebook_likeboxcountview_border_color)
            lPWin.width = 5.toPx()/3
            viewWin.layoutParams = lPWin

            lPDraw.width = 5.toPx()/3
            viewDraw.layoutParams = lPDraw
        }
        else {
            if(win == 0 && draw == 0) {
                lPWin.width = 30.toPx()
                viewWin.layoutParams = lPWin

                lPDraw.width = 30.toPx()
                viewDraw.layoutParams = lPDraw
            } else if(win == 0 && lose == 0) {
                lPWin.width = 30.toPx()
                viewWin.layoutParams = lPWin

                lPLose.width = 30.toPx()
                viewLose.layoutParams = lPLose
            } else if(draw == 0 && lose == 0) {
                lPDraw.width = 30.toPx()
                viewDraw.layoutParams = lPDraw

                lPLose.width = 30.toPx()
                viewLose.layoutParams = lPLose
            }
        }
    }
}