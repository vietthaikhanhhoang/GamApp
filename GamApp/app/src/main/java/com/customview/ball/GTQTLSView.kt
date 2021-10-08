package com.customview.ball

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.barservicegam.app.R
import com.customadapter.ListNewsAdapter

class GTQTLSView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {
    lateinit var txtGT: TextView
    lateinit var txtQT: TextView
    lateinit var txtLS: TextView
    
    lateinit var layoutQT: LinearLayout

    interface GTQTLSViewListener {
        fun click_GTQTLSViewListener(position: Int)
    }

    private var listener: GTQTLSView.GTQTLSViewListener? = null

    fun setGTQTLSViewListener(listener: GTQTLSView.GTQTLSViewListener?) {
        this.listener = listener
    }

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.gtqtlsview, this)
        txtGT = view.findViewById(R.id.txtGT)
        txtQT = view.findViewById(R.id.txtQT)
        txtLS = view.findViewById(R.id.txtLS)

        layoutQT = view.findViewById(R.id.layoutQT)

        val typefaceTitle = Typeface.createFromAsset(txtGT.getContext().assets, "fonts/sfuidisplaysemibold.ttf")
        txtGT.setTypeface(typefaceTitle)
        txtGT.setTextColor(txtGT.getResources().getColor(R.color.titlenewscolor, null))

        txtQT.setTypeface(typefaceTitle)
        txtQT.setTextColor(txtQT.getResources().getColor(R.color.titlenewscolor, null))

        txtLS.setTypeface(typefaceTitle)
        txtLS.setTextColor(txtLS.getResources().getColor(R.color.titlenewscolor, null))

        layoutQT.setOnClickListener{
            if(listener != null) {
                this.listener!!.click_GTQTLSViewListener(1)
            }
        }
    }
}