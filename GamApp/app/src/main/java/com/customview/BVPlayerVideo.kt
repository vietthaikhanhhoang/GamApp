package com.customview

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.barservicegam.app.R
import okhttp3.internal.trimSubstring

class BVPlayerVideo @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var exoVideo: ExoVideo
    lateinit var youtubeVideo: YoutubeVideo

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.bvplayervideo, this)
        exoVideo = view.findViewById(R.id.exoVideo)
        youtubeVideo = view.findViewById(R.id.youtubeVideo)
//
        exoVideo.visibility = View.INVISIBLE
        youtubeVideo.visibility = View.INVISIBLE
    }

    fun loadVideo(url: String) {
        var strUrl = url
        //strUrl = "https://www.youtube.com/embed/hJGPKqx17vw"
        //strUrl = "https://suckhoedoisong.qltns.mediacdn.vn/324455921873985536/2021/9/16/DI1sN80iSL4-1631762018024900674403.mp4"
        //strUrl = "https://cdn.24h.com.vn/upload/3-2021/videoclip/2021-07-23/1626976327-layvosinhtu.m3u8"

        if(strUrl.contains(".mp4") || strUrl.contains(".m3u8")) {
            //exoplayer2
            exoVideo.visibility = View.VISIBLE
            youtubeVideo.visibility = View.INVISIBLE

            exoVideo.loadVideo(strUrl)
            youtubeVideo.pauseVideo()
            //Log.d("vietnb", "play loadVideo exoplayer2 url $strUrl")
        } else {
            //Log.d("vietnb", "play loadVideo youtube url $strUrl")
//            //youtube
            exoVideo.visibility = View.INVISIBLE
            youtubeVideo.visibility = View.VISIBLE

            val start = strUrl.lastIndexOf("/")
            if(start > 0) {
                val youtubeID = strUrl.trimSubstring(start + 1)

                //Log.d("vietnb", "chuoi youtube: $youtubeID")
                youtubeVideo.loadVideo(youtubeID)
                exoVideo.pauseVideo()
            }
        }
    }

    fun pauseVideo(){
        Handler(Looper.getMainLooper()).post {
            //code that runs in main
            if(exoVideo.visibility == View.VISIBLE) {
                exoVideo.pauseVideo()
            } else if(exoVideo.visibility == View.VISIBLE) {
                youtubeVideo.pauseVideo()
            }
        }
    }

    fun playVideo(){
        Handler(Looper.getMainLooper()).post {
            if(exoVideo.visibility == View.VISIBLE) {
                exoVideo.playVideo()
                youtubeVideo.pauseVideo()
            } else if(exoVideo.visibility == View.VISIBLE) {
                exoVideo.pauseVideo()
                youtubeVideo.playVideo()
            }
        }
    }
}