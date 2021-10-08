package com.customview
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.barservicegam.app.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lib.Utils
import com.main.app.MainActivity

class BottomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {
    lateinit var imgNews: ImageView
    lateinit var imgVideo: ImageView
    lateinit var imgBall: ImageView
    lateinit var imgExplore: ImageView
    lateinit var imgSetting: ImageView

    lateinit var txtNews: TextView
    lateinit var txtVideo: TextView
    lateinit var txtBall: TextView
    lateinit var txtExplore: TextView
    lateinit var txtSetting: TextView

    lateinit var layoutNews: LinearLayout
    lateinit var layoutVideo: LinearLayout
    lateinit var layoutBall: LinearLayout
    lateinit var layoutExplore: LinearLayout
    lateinit var layoutSetting: LinearLayout

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.bottomview, this)
        imgNews = view.findViewById(R.id.imgNews)
        imgVideo = view.findViewById(R.id.imgVideo)
        imgBall = view.findViewById(R.id.imgBall)
        imgExplore = view.findViewById(R.id.imgExplore)
        imgSetting = view.findViewById(R.id.imgSetting)

        txtNews = view.findViewById(R.id.txtNews)
        txtVideo = view.findViewById(R.id.txtVideo)
        txtBall = view.findViewById(R.id.txtBall)
        txtExplore = view.findViewById(R.id.txtExplore)
        txtSetting = view.findViewById(R.id.txtSetting)

        layoutNews = view.findViewById(R.id.layoutNews)
        layoutVideo = view.findViewById(R.id.layoutVideo)
        layoutBall = view.findViewById(R.id.layoutBall)
        layoutExplore = view.findViewById(R.id.layoutExplore)
        layoutSetting = view.findViewById(R.id.layoutSetting)

        layoutNews.setOnClickListener {
            val topActivity = Utils.getActivity(context)
            if(topActivity is MainActivity) {
                topActivity.selectItemIdBottom(0)
            }
        }

        layoutVideo.setOnClickListener {
            val topActivity = Utils.getActivity(context)
            if(topActivity is MainActivity) {
                topActivity.selectItemIdBottom(1)
            }
        }

        layoutBall.setOnClickListener {
            val topActivity = Utils.getActivity(context)
            if(topActivity is MainActivity) {
                topActivity.selectItemIdBottom(2)
            }
        }

        layoutExplore.setOnClickListener {
            val topActivity = Utils.getActivity(context)
            if(topActivity is MainActivity) {
                topActivity.selectItemIdBottom(3)
            }
        }

        layoutSetting.setOnClickListener {
            val topActivity = Utils.getActivity(context)
            if(topActivity is MainActivity) {
                topActivity.selectItemIdBottom(4)
            }
        }
    }

    fun setViewTab(tabId: Int) {
        txtNews.setTextColor(resources.getColor(R.color.titlenewscolor, null))
        txtVideo.setTextColor(resources.getColor(R.color.titlenewscolor, null))
        txtBall.setTextColor(resources.getColor(R.color.titlenewscolor, null))
        txtExplore.setTextColor(resources.getColor(R.color.titlenewscolor, null))
        txtSetting.setTextColor(resources.getColor(R.color.titlenewscolor, null))

        imgNews.setImageResource(R.drawable.newsgray)
        imgVideo.setImageResource(R.drawable.videogray)
        imgBall.setImageResource(R.drawable.iconfootballgreen)
        imgExplore.setImageResource(R.drawable.explore_gray)
        imgSetting.setImageResource(R.drawable.profilegray)

        if(tabId == 0) {
            txtNews.setTextColor(resources.getColor(R.color.mainredcolor, null))
            imgNews.setImageResource(R.drawable.newsred)
        } else if(tabId == 1) {
            txtVideo.setTextColor(resources.getColor(R.color.mainredcolor, null))
            imgVideo.setImageResource(R.drawable.videored)
        } else if(tabId == 2) {
            txtBall.setTextColor(resources.getColor(R.color.mainredcolor, null))
            imgBall.setImageResource(R.drawable.iconfootballred)
        } else if(tabId == 3) {
            txtExplore.setTextColor(resources.getColor(R.color.mainredcolor, null))
            imgExplore.setImageResource(R.drawable.explorered)
        } else if(tabId == 4) {
            txtSetting.setTextColor(resources.getColor(R.color.mainredcolor, null))
            imgSetting.setImageResource(R.drawable.profilered)
        }
    }
}