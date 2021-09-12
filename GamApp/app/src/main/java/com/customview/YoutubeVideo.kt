package com.customview

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.barservicegam.app.R
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.lib.OrientationUtils
import com.lib.Utils

class YoutubeVideo : AppCompatActivity() {
    lateinit var youtubePlayerView: YouTubePlayerView
    lateinit var youtubePlayer: YouTubePlayer
    lateinit var onInitializedListener: YouTubePlayer.OnInitializedListener
    lateinit var layoutParent: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        OrientationUtils.lockOrientationPortrait(this)
        Utils.hiddenStatusBarAndBottomBar(this)
        setContentView(R.layout.activity_demo_youtube_video)
        layoutParent = findViewById(R.id.layoutParent)
        onInitializedListener = object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                p0: YouTubePlayer.Provider?,
                p1: YouTubePlayer?,
                p2: Boolean
            ) {
//                youtubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL)
                //p1!!.cueVideo("v8MbOjBCu0o") //loadVideo: chay luon
                p1!!.loadVideo("v8MbOjBCu0o")
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.d("vietnb", "error")
            }
        }
        youtubePlayerView.initialize("AIzaSyDqm9WYFPT782SSqDdwy3iCpBn5_pvv5qs", onInitializedListener)
    }
}