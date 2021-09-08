package com.customview

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.activity.MainActivity
import com.barservicegam.app.R
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.lib.Utils
import com.lib.eventbus.EventBusFire
import com.lib.toPx
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


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

    fun playYoutubeVideo(youtubeUrl: String) {
        if(playerView.player != null) {
            playerView.player!!.stop()
        }
        Toast.makeText(this@ExoVideo.context, "Định dạng youtube $youtubeUrl", Toast.LENGTH_SHORT).show()

        val videoPlayer = SimpleExoPlayer.Builder(this.context).build()
        playerView.player = videoPlayer

        object : YouTubeExtractor(this.context) {
            override fun onExtractionComplete(
                ytFiles: SparseArray<YtFile>?,
                videoMeta: VideoMeta?
            ) {
                if(ytFiles != null) {
                    val videoTag = 137
                    val audioTag = 140
                    val audioSource = ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory()).createMediaSource(
                        MediaItem.fromUri(ytFiles.get(audioTag).url))
                    val videoSource = ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory()).createMediaSource(
                        MediaItem.fromUri(ytFiles.get(videoTag).url))
                    val videoPlayer = SimpleExoPlayer.Builder(this@ExoVideo.context).build()
                    videoPlayer.setMediaSource(MergingMediaSource(true, videoSource, audioSource), true)
                    playerView.player = videoPlayer
                    playerView.player!!.prepare()
                    playerView.player!!.play()
                    //playerView.player.seekTo()
                    Toast.makeText(this@ExoVideo.context, "Định dạng youtube $youtubeUrl", Toast.LENGTH_SHORT).show()
                }
            }
        }.extract(youtubeUrl, false, true)

    }

    fun setUrlVideo(urlVideo: String) {

        if(!urlVideo.contains("youtube")) {
            playYoutubeVideo("https://www.youtube.com/watch?v=8MLa-Lh8lkU")
            return
        }

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
        } else if(urlVideo.contains("youtube")) {
            Toast.makeText(this.context, "Định dạng youtube $urlVideo", Toast.LENGTH_SHORT).show()
            mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(
                MediaItem.fromUri(
                    Uri.parse(
                        "8MLa-Lh8lkU"
                    )
                )
            )
        }

        if(playerView.player != null) {
            playerView.player!!.stop()
        }

        if(urlVideo.contains("mp4") || urlVideo.contains("m3u8") || urlVideo.contains("youtube")) {
            videoPlayer.setMediaSource(mediaSource!!)
            playerView.player = videoPlayer
            videoPlayer.prepare()
            videoPlayer.play()
            imgPlay.setImageResource(R.drawable.pause)
        } else {
            Toast.makeText(
                this.context,
                "Chưa hỗ trợ định dạng video này $urlVideo",
                Toast.LENGTH_SHORT
            ).show()
            imgPlay.setImageResource(R.drawable.play)
        }
    }

    init {
        init(attrs)
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onMessageEvent(event: EventBusFire) { /* Do something */
        if(event.eventName == "pauseVideo") {
            pauseVideo()
        }
    }

    private fun init(attrs: AttributeSet?) {

        EventBus.getDefault().register(this)

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
//            if(playerView.player != null) {
//                val volume = playerView.player!!.volume
//                Log.d("vietnb", "volume: $volume")
//
//                if(playerView.player!!.volume == 1.0f) {
//                    playerView.player!!.volume = 0.0f
//                    imgVolume.setImageResource(R.drawable.mute)
//                } else {
//                    playerView.player!!.volume = 1.0f
//                    imgVolume.setImageResource(R.drawable.unmute)
//                }
//            }
        }

//        val heightStatus = Utils.getStatusBarHeight(this.requireContext())

        imgFullscreen.setOnClickListener{

            if (layoutVideo.parent != null) {
                (layoutVideo.parent as ViewGroup).removeView(layoutVideo)
            }

            val topActivity = Utils.getActivity(context)
            if(!isFullScreen) {
                val width = Utils.getScreenWidth().toInt().toPx()
                val height = Utils.getScreenHeight().toInt().toPx() + Utils.getHeightBottomBar(
                    topActivity!!
                ).toPx()
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

    fun pauseVideo(){
        Handler(Looper.getMainLooper()).post {
            //code that runs in main
            if(playerView.player != null) {
                playerView.player!!.pause()
                imgPlay.setImageResource(R.drawable.play)
            }
        }
    }

    fun playVideo(){
        Handler(Looper.getMainLooper()).post {
            //code that runs in main
            if(playerView.player != null) {
                playerView.player!!.play()
                imgPlay.setImageResource(R.drawable.pause)
            }
        }
    }

    fun isPlaying() :Boolean {
        if(playerView.player != null) {
            if(playerView.player!!.isPlaying) return true
        }
        return false
    }

    fun readyVideo(isReady: Boolean){
        Handler(Looper.getMainLooper()).post {
            //code that runs in main
            if(playerView.player != null) {
                playerView.player!!.playWhenReady = isReady
            }
        }
    }
}