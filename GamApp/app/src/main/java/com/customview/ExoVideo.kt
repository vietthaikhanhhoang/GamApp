package com.customview

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.contains
import androidx.fragment.app.findFragment
import com.activity.MainActivity
import com.barservicegam.app.R
import com.fragmentcustom.ListVideoFragment
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.lib.Utils
import com.lib.toPx


class ExoVideo @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var playerView: PlayerView

    lateinit  var imgPlay: ImageView
    lateinit  var imgVolume: ImageView
    lateinit  var imgFullscreen: ImageView

    lateinit var layoutVideo: ConstraintLayout
    lateinit var layoutControl: ConstraintLayout

    var isFullScreen:Boolean = false
    lateinit var layoutParent: ConstraintLayout

    var showingControlVideo:Boolean = true

    ////listener
    interface VideoFragmentListener {
        fun videofragment_fullScreen(isFullSreen: Boolean)
    }

    private var listener: VideoFragmentListener? = null

    fun setVideoFragmentListener(listener: VideoFragmentListener) {
        this.listener = listener
    }

    fun setUrlVideo(urlVideo: String) {

//        urlVideo = ""
        //urlVideo = "https://media.tinmoi24.vn/24h/upload/3-2021/videoclip/2021-07-05/1625467769-sr16_jasspers1.m3u8"
//        urlVideo = "https://suckhoedoisong.qltns.mediacdn.vn/324455921873985536/2021/9/3/CKKWDprrCZg-16306723867591730630975.mp4"

        playerView.requestFocus()
        val videoPlayer = SimpleExoPlayer.Builder(this.context).build()
        val mediaDataSourceFactory = DefaultDataSourceFactory(this.context)

        var mediaSource: MediaSource? = null
        if(urlVideo.contains("m3u8")) {
            mediaSource = HlsMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
                MediaItem.fromUri(
                    Uri.parse(
                        urlVideo
                    )
                )
            )
        } else if(urlVideo.contains("mp4")) {
            mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
                MediaItem.fromUri(
                    Uri.parse(
                        urlVideo
                    )
                )
            )
        }

        if(playerView.player != null) {
            playerView.player!!.stop()
        }

        if(urlVideo.contains("mp4") || urlVideo.contains("m3u8")) {
            videoPlayer.setMediaSource(mediaSource!!)
            playerView.player = videoPlayer
            videoPlayer.prepare()
            videoPlayer.play()
            imgPlay.setImageResource(R.drawable.pause)
        }
    }

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.fragment_video, this)
        layoutParent = view.findViewById(R.id.layoutParent)
        layoutVideo = view.findViewById(R.id.layoutVideo)
        layoutControl = view.findViewById(R.id.layoutControl)
        playerView = view.findViewById(R.id.playerView)
        imgPlay = view.findViewById(R.id.imgPlay)
        imgVolume = view.findViewById(R.id.imgVolume)
        imgFullscreen = view.findViewById(R.id.imgFullscreen)

        bindDataLayout()
    }

    fun bindDataLayout() {
        layoutParent.setOnClickListener{
            if(showingControlVideo) {
                layoutControl.visibility = View.INVISIBLE
            } else {
                layoutControl.visibility = View.VISIBLE
            }
            showingControlVideo = !showingControlVideo
        }

        playerView.useController = false

        imgPlay.setOnClickListener {
            if(playerView.player != null) {
                if(!playerView.player!!.isPlaying) {
                    playerView.player!!.play()
                    imgPlay.setImageResource(R.drawable.pause)
                } else {
                    playerView.player!!.pause()
                    imgPlay.setImageResource(R.drawable.play)
                }
            }
        }

        imgVolume.setOnClickListener {
            if(playerView.player != null) {
                val volume = playerView.player!!.volume
                Log.d("vietnb", "volume: $volume")

                if(playerView.player!!.volume == 1.0f) {
                    playerView.player!!.volume = 0.0f
                    imgVolume.setImageResource(R.drawable.mute)
                } else {
                    playerView.player!!.volume = 1.0f
                    imgVolume.setImageResource(R.drawable.unmute)
                }
            }
        }

//        val heightStatus = Utils.getStatusBarHeight(this.requireContext())

        imgFullscreen.setOnClickListener{

            if (layoutVideo.parent != null) {
                (layoutVideo.parent as ViewGroup).removeView(layoutVideo)
            }

            val topActivity = Utils.getActivity(context)
            if(!isFullScreen) {
                val width = Utils.getScreenWidth().toInt().toPx()
                val height = Utils.getScreenHeight().toInt().toPx() + Utils.getHeightBottomBar(topActivity!!).toPx()
                val offset:Float = ((width - height) / 2).toFloat()
                val lp = ConstraintLayout.LayoutParams(height, width)
                layoutVideo.layoutParams = lp
                layoutVideo.rotation = -90.0f
                layoutVideo.translationX = offset
                layoutVideo.translationY = -offset

                if (topActivity is MainActivity) {
                    topActivity!!.bottomView.visibility = View.GONE
                    topActivity!!.window.addContentView(layoutVideo, lp)
                    topActivity!!.fullScreen(true)
                }
            } else {
                val width = Utils.getScreenWidth().toInt().toPx() - 32.toPx()
                val height = (width*9)/16
                val offset:Float = ((width - height) / 2).toFloat()
                val lp = ConstraintLayout.LayoutParams(width, height)
                layoutVideo.layoutParams = lp
                layoutVideo.rotation = 0.0f
                layoutVideo.translationX = 0f
                layoutVideo.translationY = 0f

                layoutParent.addView(layoutVideo)

                if (topActivity is MainActivity) {
                    topActivity.fullScreen(false)
                }
            }
            isFullScreen = !isFullScreen
        }
    }
}