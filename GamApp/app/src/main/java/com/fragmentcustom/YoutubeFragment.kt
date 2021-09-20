package com.fragmentcustom

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.barservicegam.app.MainActivity
import com.barservicegam.app.R
import com.lib.Utils
import com.lib.toPx
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [YoutubeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class YoutubeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    val API_KEY: String? = null
    val VIDEO_ID = "B08iLAtS3AQ"

//    lateinit var youtubePlayerView: YouTubePlayerView
    lateinit var layoutParent: ConstraintLayout
    private lateinit var youtube_player_view: YouTubePlayerView
    lateinit var youtubePlayer:YouTubePlayer
    var isFullScreen:Boolean = false

    lateinit var layoutVideo: ConstraintLayout
    lateinit var imgFullScreen: ImageView
    lateinit var imgPlay: ImageView
    lateinit var imgMute: ImageView
    lateinit var txtTime: TextView
    lateinit var seekBarVideo: SeekBar
    var valueVolume = 1 //default la bat tieng
    lateinit var btnNextVideo: Button

    var tracker = YouTubePlayerTracker()

    var isDragSeekBar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    fun initYoutubeWithVideoId(videoID: String) {
        youtube_player_view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(videoID, 0f)
                youtubePlayer = youTubePlayer
                if(valueVolume == -1) {
                    valueVolume = 1
                    youtubePlayer.setVolume(1)
                }

                youtubePlayer?.addListener(tracker)
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
                txtTime.text = textcurrent + "/" + textduration
                Log.d("vietnb", "thoi gian hien tai: $second || tong: $duration")

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
                if(state == PlayerConstants.PlayerState.ENDED) {
                    Toast.makeText(requireContext(), "Ket thuc video", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_youtube, container, false)
        layoutVideo = view.findViewById(R.id.layoutVideo)
        imgFullScreen = view.findViewById(R.id.imgFullScreen)
        imgPlay = view.findViewById(R.id.imgPlay)
        imgMute = view.findViewById(R.id.imgMute)
        btnNextVideo = view.findViewById(R.id.btnNextVideo)

        txtTime = view.findViewById(R.id.txtTime)
        seekBarVideo = view.findViewById(R.id.seekBarVideo)

        btnNextVideo.setOnClickListener {
            Log.d("vietnb", "click vao next video")
            //initYoutubeWithVideoId("KkLOKhvyhS4")
//            youtubePlayer.cueVideo("KkLOKhvyhS4", 0f)
            youtubePlayer.loadVideo("KkLOKhvyhS4", 0f)
        }

        this.seekBarVideo.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                isDragSeekBar = true
                if(tracker.state == PlayerConstants.PlayerState.PLAYING) {
                    youtubePlayer.pause()
                }

                val second = (progress*tracker.videoDuration)/100
                youtubePlayer.seekTo((progress*tracker.videoDuration)/100)
                Log.d("vietnb", "zzzzzzz: $second")

                val textcurrent = String.format("%02d:%02d", (second / 60).toInt(),((second % 60)).toInt())
                val textduration = String.format("%02d:%02d", (tracker.videoDuration / 60).toInt(),((tracker.videoDuration % 60)).toInt())
                txtTime.text = textcurrent + "/" + textduration
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isDragSeekBar = true
                Log.d("vietnb", "Nhay vao onStartTrackingTouch")
                youtubePlayer.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekTimeVideoWhenSeekBarDragged()
            }
        })

        txtTime.text = "00:00"

        youtube_player_view = view.findViewById(R.id.youtube_player_view)
        youtube_player_view.getPlayerUiController().showUi(false)

        layoutParent = view.findViewById(R.id.layoutParent)

        initYoutubeWithVideoId("GSn3daa1ZX4")

        imgPlay.setOnClickListener{
            if(tracker.state == PlayerConstants.PlayerState.PLAYING) {
                youtubePlayer.pause()
                imgPlay.setImageResource(R.drawable.play)
            } else {
                youtubePlayer.play()
                imgPlay.setImageResource(R.drawable.pause)
            }
        }

        imgMute.setOnClickListener{
            if(valueVolume == 1) {
                valueVolume = 0
                imgMute.setImageResource(R.drawable.unmute)
            } else {
                valueVolume = 1
                imgMute.setImageResource(R.drawable.mute)
            }
            youtubePlayer.setVolume(valueVolume)
        }

        imgFullScreen.setOnClickListener{
            if (layoutVideo.parent != null) {
                (layoutVideo.parent as ViewGroup).removeView(layoutVideo)
            }

            val topActivity = Utils.getActivity(requireContext())
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
                val width = Utils.getScreenWidth().toInt().toPx()// - 32.toPx()
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

        return view
    }

    fun seekTimeVideoWhenSeekBarDragged() {
        Handler(Looper.getMainLooper()).postDelayed({
            isDragSeekBar = false
            Log.d("vietnb", "Nhay vao onStopTrackingTouch")
            youtubePlayer.play()
            imgPlay.setImageResource(R.drawable.pause)
        }, 1000)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment YoutubeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            YoutubeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}