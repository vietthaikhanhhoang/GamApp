package com.barservicegam.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.barservicegam.app.R
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

class DemoYoutubeVideo : YouTubeBaseActivity() {
    lateinit var youtubePlayerView: YouTubePlayerView
    lateinit var onInitializedListener: YouTubePlayer.OnInitializedListener
    lateinit var btnPlay: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_youtube_video)

        youtubePlayerView = findViewById(R.id.youtubePlayerView)
        btnPlay = findViewById(R.id.btnPlay)

        onInitializedListener = object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                p0: YouTubePlayer.Provider?,
                p1: YouTubePlayer?,
                p2: Boolean
            ) {
                p1!!.loadVideo("T_cwZjkSpy4")
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.d("vietnb", "error")
            }
        }

        btnPlay.setOnClickListener{
            youtubePlayerView.initialize("AIzaSyDqm9WYFPT782SSqDdwy3iCpBn5_pvv5qs", onInitializedListener)
        }
    }
}