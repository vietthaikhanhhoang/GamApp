package com.customview

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.main.app.MainActivity
import com.barservicegam.app.R
import com.lib.Utils
import com.lib.toDp
import com.lib.toPx
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class YoutubeVideo @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    val menuHandler = Handler(Looper.getMainLooper())
    val runnable = Runnable {
        downMenu(200)
    }

    var isMute:Boolean = false

    lateinit var layoutParent: ConstraintLayout
    private lateinit var youtube_player_view: YouTubePlayerView
    lateinit var youtubePlayer: YouTubePlayer
    var isFullScreen:Boolean = false

    lateinit var layoutVideo: ConstraintLayout
    lateinit var imgFullScreen: ImageView
    lateinit var layoutFullScreen: ConstraintLayout

    lateinit var imgPlay: ImageView
    lateinit var layoutPlay: ConstraintLayout

    lateinit var imgVolume: ImageView
    lateinit var layoutVolume: ConstraintLayout

    lateinit var txtTime: TextView
    lateinit var seekBarVideo: SeekBar

    lateinit var layoutMenu:ConstraintLayout
    lateinit var layoutMenuChild: ConstraintLayout

    lateinit var txtInteractVideo: TextView

    var tracker = YouTubePlayerTracker()
    var isDragSeekBar = false

    var urlVideo:String = ""
    var youtubeOnReady:Boolean = false

    var isShowMenu:Boolean = false

    fun isStartedVideo() : Boolean{
        if(tracker.state == PlayerConstants.PlayerState.UNSTARTED) {
            return true
        }
        return false
    }

    fun loadVideo(url: String) {
        urlVideo = url
        if(youtubeOnReady) {

            resetVideo()
            menuHandler.removeCallbacks(runnable)
            downMenu(0)

            youtubePlayer?.loadVideo(url, 0f)
        }
    }

    private fun initYoutube() {
        youtube_player_view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                //youTubePlayer.cueVideo(videoID, 0f)
                youtubePlayer = youTubePlayer
                youtubePlayer?.addListener(tracker)
                youtubeOnReady = true

                if(isMute) {
                    mute()
                } else {
                    unMute()
                }
            }

            override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
                super.onError(youTubePlayer, error)
                Log.d("vietnb", "Có lỗi xảy ra")
            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                super.onCurrentSecond(youTubePlayer, second)

                if(isDragSeekBar) return

                val duration:Float = tracker.videoDuration

                val textcurrent = String.format("%02d:%02d", (second / 60).toInt(),((second % 60)).toInt())
                val textduration = String.format("%02d:%02d", (tracker.videoDuration / 60).toInt(),((tracker.videoDuration % 60)).toInt())
                txtTime.text = textcurrent + " / " + textduration
                //Log.d("vietnb", "thoi gian hien tai: $second || tong: $duration")

                if(duration != 0f) {
                    val value = (second*100/duration).toInt()
//                    Log.d("vietnb", "thoi gian hien tai: $value")
                    seekBarVideo.progress = value
                }
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                super.onStateChange(youTubePlayer, state)
                if(state == PlayerConstants.PlayerState.PLAYING) {
                    imgPlay.setImageResource(R.drawable.pause)
                } else if(state == PlayerConstants.PlayerState.PAUSED) {
                    imgPlay.setImageResource(R.drawable.play)
                }
                else if(state == PlayerConstants.PlayerState.ENDED) {
                    imgPlay.setImageResource(R.drawable.replay)
                    upMenu()
                    //Toast.makeText(context, "Ket thuc video", Toast.LENGTH_SHORT).show()
                    val textduration = String.format("%02d:%02d", (tracker.videoDuration / 60).toInt(),((tracker.videoDuration % 60)).toInt())
                    txtTime.text = textduration + " / " + textduration
                }
            }
        })
    }

    fun mute() {
        isMute = true
        youtubePlayer.setVolume(0)
        imgVolume.setImageResource(R.drawable.mute)
    }

    fun unMute() {
        isMute = false
        youtubePlayer.setVolume(100)
        imgVolume.setImageResource(R.drawable.unmute)
    }

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {

        //EventBus.getDefault().register(this)

        val view = View.inflate(context, R.layout.view_youtube, this)
        layoutVideo = view.findViewById(R.id.layoutVideo)

        imgFullScreen = view.findViewById(R.id.imgFullScreen)
        layoutFullScreen = view.findViewById(R.id.layoutFullScreen)

        imgPlay = view.findViewById(R.id.imgPlay)
        layoutPlay = view.findViewById(R.id.layoutPlay)

        imgVolume = view.findViewById(R.id.imgVolume)
        layoutVolume = view.findViewById(R.id.layoutVolume)

        layoutMenu = view.findViewById(R.id.layoutMenu)
        layoutMenuChild = view.findViewById(R.id.layoutMenuChild)

        txtTime = view.findViewById(R.id.txtTime)
        seekBarVideo = view.findViewById(R.id.seekBarVideo)

        youtube_player_view = view.findViewById(R.id.youtube_player_view)
        layoutParent = view.findViewById(R.id.layoutParent)
        txtInteractVideo = view.findViewById(R.id.txtInteractVideo)

        bindDataLayout()
    }

    private fun bindDataLayout() {
        txtInteractVideo.setOnClickListener{
            Log.d("vietnb", "click va layoutVideo")
            menuHandler.removeCallbacks(runnable)
            if(isShowMenu) {
                downMenu(200)
            } else
            {
                upMenu()
                menuHandler.postDelayed(runnable, 6000)
            }
        }

        this.seekBarVideo.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(isDragSeekBar) {
                    if(tracker.state == PlayerConstants.PlayerState.PLAYING) {
                        youtubePlayer.pause()
                    }

                    val second = (progress*tracker.videoDuration)/100
                    youtubePlayer.seekTo((progress*tracker.videoDuration)/100)
                    //Log.d("vietnb", "zzzzzzz: $second")

                    val textcurrent = String.format("%02d:%02d", (second / 60).toInt(),((second % 60)).toInt())
                    val textduration = String.format("%02d:%02d", (tracker.videoDuration / 60).toInt(),((tracker.videoDuration % 60)).toInt())
                    txtTime.text = textcurrent + " / " + textduration
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                menuHandler.removeCallbacks(runnable)
                isDragSeekBar = true
                //Log.d("vietnb", "Nhay vao onStartTrackingTouch")
                youtubePlayer.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if(isDragSeekBar) {
                    seekTimeVideoWhenSeekBarDragged()
                    menuHandler.postDelayed(runnable, 6000)
                }
                isDragSeekBar = false
            }
        })

        txtTime.text = "00:00"

        youtube_player_view.getPlayerUiController().showUi(false)

        initYoutube()

        layoutPlay.setOnClickListener{
            if(youtubeOnReady) {
                Log.d("vietnb", "click vao imgPlay: $youtubePlayer")
                Log.d("vietnb", "trang thai ${tracker.state}")

                if(tracker.state == PlayerConstants.PlayerState.UNSTARTED) {
                    loadVideo(urlVideo)
                }
                else if(tracker.state == PlayerConstants.PlayerState.PLAYING) {
                    youtubePlayer.pause()
                } else {
                    youtubePlayer.play()
                }
            }
        }

        layoutVolume.setOnClickListener{
            if(isMute) {
                unMute()
            } else {
                mute()
            }
        }

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
    }

    fun seekTimeVideoWhenSeekBarDragged() {
        Handler(Looper.getMainLooper()).postDelayed({
            //Log.d("vietnb", "Nhay vao onStopTrackingTouch")
            youtubePlayer.play()
        }, 1000)
    }

    fun playVideo(){
        Handler(Looper.getMainLooper()).post {
            //code that runs in main
            if(youtubeOnReady) {
                youtubePlayer.play()
            }
        }
    }

    fun pauseVideo(){
        Handler(Looper.getMainLooper()).post {
            //code that runs in main
            if(youtubeOnReady) {
                youtubePlayer.pause()
            }
        }
    }

    fun isPlaying() :Boolean {
        if(youtubeOnReady) {
            if(tracker.state == PlayerConstants.PlayerState.PLAYING) {
                return true
            }
        }
        return false
    }

    fun readyVideo(isReady: Boolean){
        Handler(Looper.getMainLooper()).post {
            //code that runs in main
//            if(playerView.player != null) {
//                playerView.player!!.playWhenReady = isReady
//            }
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

        val duration:Float = tracker.videoDuration

        val textcurrent = "00:00"
        var textduration = "00:00"

        if(duration > 0) {
            textduration = String.format("%02d:%02d", (tracker.videoDuration / 60).toInt(),((tracker.videoDuration % 60)).toInt())
        }

        txtTime.text = textcurrent + " / " + textduration
        seekBarVideo.progress = 0
    }
}