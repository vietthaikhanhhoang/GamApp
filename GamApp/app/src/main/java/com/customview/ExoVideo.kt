package com.customview

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.main.app.MainActivity
import com.barservicegam.app.R
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.lib.Utils
import com.lib.eventbus.EventBusFire
import com.lib.toDp
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

    ///tracking currentTime:
    val updateHandler = Handler(Looper.getMainLooper())
    val runnable = Runnable {
        trackingCurrentTimeVideo()
    }

    private fun trackingCurrentTimeVideo() {
//        Log.d("vietnb", "Nhay vao onStopTrackingTouch")
        if(playerView.player != null) {
            var currentTime:Long = playerView.player!!.currentPosition/1000
            var durationTime:Long = playerView.player!!.duration/1000

            txtTime.text = "00:00 / 00:00"

            if(durationTime > 0) {
                val textcurrent = String.format("%02d:%02d", (currentTime / 60).toInt(),((currentTime % 60)).toInt())
                val textduration = String.format("%02d:%02d", (durationTime / 60).toInt(),((durationTime % 60)).toInt())

                txtTime.text = textcurrent + " / " + textduration
                //txtTime.text = currentTime.toString() + "/" + durationTime.toString()

                val value:Float = (currentTime*1f/durationTime).toFloat()
//                Log.d("vietnb", "current: $currentTime")
//                Log.d("vietnb", "duration: $durationTime")
//
//                Log.d("vietnb", "value gia tri :: " + (value*100).toInt().toString())

                seekBarVideo.progress = (value*100).toInt()
            }

            updateHandler.postDelayed(runnable, 1000)
        }
    }

    val menuHandler = Handler(Looper.getMainLooper())
    val menuRunable = Runnable {
        downMenu(200)
    }

    var isVideoEnd:Boolean = false

    var secondTime:Int = 0
    lateinit var txtTime: TextView
    lateinit var playerView: PlayerView
    lateinit var seekBarVideo: SeekBar

    lateinit  var imgPlay: ImageView
    lateinit  var layoutPlay: ConstraintLayout

    lateinit  var imgVolume: ImageView
    lateinit  var layoutVolume: ConstraintLayout

    lateinit  var imgFullScreen: ImageView
    lateinit  var layoutFullScreen: ConstraintLayout

    lateinit var layoutVideo: ConstraintLayout
    lateinit var layoutMenu: ConstraintLayout
    lateinit var layoutMenuChild: ConstraintLayout

    var isShowMenu:Boolean = false

    var isFullScreen:Boolean = false
    lateinit var layoutParent: ConstraintLayout
    lateinit var txtInteractVideo:TextView

    var isDragSeekBar = false

    ////listener
    interface VideoFragmentListener {
        fun videofragment_fullScreen(isFullSreen: Boolean)
    }

    private var listener: VideoFragmentListener? = null

    fun setVideoFragmentListener(listener: VideoFragmentListener) {
        this.listener = listener
    }

    fun loadVideo(urlVideo: String) {
        resetVideo()

        updateHandler.removeCallbacks(runnable)
        downMenu(0)

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

            videoPlayer.addListener(object: Player.EventListener {

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    if(isPlaying) {
                        Log.d("vietnb", "video playing")
                        updateHandler.postDelayed(runnable, 1000)
                    } else {
                        Log.d("vietnb", "video pause")
                        updateHandler.removeCallbacks(runnable)
                    }
                }

                override fun onPlaybackStateChanged(state: Int) {
                    super.onPlaybackStateChanged(state)
                    isVideoEnd = false
                    when (state) {
                        Player.STATE_IDLE -> {
                            Log.d("vietnb", "onPlayerStateChanged: STATE_IDLE")
                        }
                        Player.STATE_BUFFERING -> {
                            Log.d("vietnb", "onPlayerStateChanged: STATE_BUFFERING")
                        }
                        Player.STATE_READY -> {
                            Log.d("vietnb", "onPlayerStateChanged: STATE_READY")
                        }
                        Player.STATE_ENDED -> {
                            Log.d("vietnb", "onPlayerStateChanged: STATE_ENDED")

                            isVideoEnd = true
                            menuHandler.removeCallbacks(menuRunable)
                            imgPlay.setImageResource(R.drawable.replay)
                            upMenu()
                            //Toast.makeText(context, "Ket thuc video", Toast.LENGTH_SHORT).show()
                            var secondduration = playerView.player!!.duration/1000
                            val textduration = String.format("%02d:%02d", (secondduration / 60).toInt(),((secondduration % 60)).toInt())
                            txtTime.text = textduration + " / " + textduration
                        }
                    }
                }
            })

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
        layoutMenu = view.findViewById(R.id.layoutMenu)
        layoutMenuChild = view.findViewById(R.id.layoutMenuChild)
        playerView = view.findViewById(R.id.playerView)

        imgPlay = view.findViewById(R.id.imgPlay)
        layoutPlay = view.findViewById(R.id.layoutPlay)

        imgVolume = view.findViewById(R.id.imgVolume)
        layoutVolume = view.findViewById(R.id.layoutVolume)

        imgFullScreen = view.findViewById(R.id.imgFullScreen)
        layoutFullScreen = view.findViewById(R.id.layoutFullScreen)

        txtTime = view.findViewById(R.id.txtTime)
        seekBarVideo = view.findViewById(R.id.seekBarVideo)
        txtInteractVideo = view.findViewById(R.id.txtInteractVideo)

        bindDataLayout()
    }

    private fun bindDataLayout() {
        txtInteractVideo.setOnClickListener{
            menuHandler.removeCallbacks(menuRunable)
            if(isShowMenu) {
                downMenu(200)
            } else
            {
                upMenu()
                menuHandler.postDelayed(menuRunable, 6000)
            }
        }

        playerView.useController = false

        layoutPlay.setOnClickListener {
            if(playerView.player != null) {
                if(!playerView.player!!.isPlaying) {

                    if(isVideoEnd) {
                        //truong hop endvideo va replay
                        playerView.player!!.seekTo(0)
                        menuHandler.postDelayed(menuRunable, 0)
                    } else {
                        playerView.player!!.play()
                    }
                    imgPlay.setImageResource(R.drawable.pause)
                } else {
                    playerView.player!!.pause()
                    imgPlay.setImageResource(R.drawable.play)
                }
            }
        }

        layoutVolume.setOnClickListener {
            if(playerView.player != null) {
                val volume = playerView.player!!.audioComponent?.volume
                Log.d("vietnb", "volume: $volume")

                if(volume == 1.0f) {
                    playerView.player!!.audioComponent?.volume = 0.0f
                    imgVolume.setImageResource(R.drawable.mute)
                } else {
                    playerView.player!!.audioComponent?.volume = 1.0f
                    imgVolume.setImageResource(R.drawable.unmute)
                }
            }
        }

//        val heightStatus = Utils.getStatusBarHeight(this.requireContext())

        layoutFullScreen.setOnClickListener{

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

        this.seekBarVideo.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.d("vietnb", "chay vao changed seekBar")
                if(playerView.player != null && isDragSeekBar) {
                    if(playerView.player!!.isPlaying) {
                        playerView.player!!.pause()
                    }

                    playerView.player!!.seekTo((progress*playerView.player!!.duration)/100)

                    var secondcurrent = (progress*playerView.player!!.currentPosition/1000)/100

                    var secondduration = playerView.player!!.duration/1000

                    val textcurrent = String.format("%02d:%02d", (secondcurrent / 60).toInt(),((secondcurrent % 60)).toInt())
                    val textduration = String.format("%02d:%02d", (secondduration / 60).toInt(),((secondduration % 60)).toInt())
                    txtTime.text = textcurrent + " / " + textduration
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isDragSeekBar = true
                menuHandler.removeCallbacks(menuRunable)
                Log.d("vietnb", "Nhay vao onStartTrackingTouch")
                if(playerView.player != null) {
                    playerView.player!!.pause()
                }

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if(isDragSeekBar) {
                    seekTimeVideoWhenSeekBarDragged()
                    menuHandler.postDelayed(runnable, 6000)
                }
                isDragSeekBar = false
            }
        })
    }

    fun seekTimeVideoWhenSeekBarDragged() {
        Handler(Looper.getMainLooper()).postDelayed({
            if(playerView.player != null) {
                Log.d("vietnb", "Nhay vao onStopTrackingTouch")
                playerView.player!!.play()
                imgPlay.setImageResource(R.drawable.pause)
            }
        }, 1000)
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

    private fun upMenu(){
        val height = layoutMenu.layoutParams.height.toDp().toFloat() + 20

        Handler(Looper.getMainLooper()).post {
            layoutPlay.visibility = View.VISIBLE
        }

        isShowMenu = true
        val animation = TranslateAnimation(
            .0f, 0.0f,
            height, 0.0f
        ) //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)

        animation.fillAfter = true

        animation.duration = 200 // animation duration
        layoutMenu.startAnimation(animation);  // start animation
    }

    private fun downMenu(duration: Long) {
        val height = layoutMenu.layoutParams.height.toDp().toFloat() + 20

        Handler(Looper.getMainLooper()).post {
            layoutPlay.visibility = View.INVISIBLE
        }

        isShowMenu = false
        val animation = TranslateAnimation(
            0.0f, 0.0f,
            0.0f, height
        ) //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)

        animation.setFillAfter(true)

        animation.duration = 200 // animation duration
        layoutMenu.startAnimation(animation);  // start animation
    }

    private fun resetVideo() {
        seekBarVideo.progress = 0

        val textcurrent = "00:00"
        var textduration = "00:00"

        if(playerView.player != null) {
            var currentTime: Long = playerView.player!!.currentPosition / 1000
            var durationTime: Long = playerView.player!!.duration / 1000
            if (durationTime > 0) {
                textduration = String.format(
                    "%02d:%02d",
                    (durationTime / 60).toInt(),
                    ((durationTime % 60)).toInt()
                )
            }
        }

        txtTime.text = textcurrent + " / " + textduration
    }
}