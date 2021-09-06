package com.fragmentcustom

import android.graphics.SurfaceTexture
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.barservicegam.app.R
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.lib.Utils
import com.lib.toPx


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VideoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_video, container, false)

        layoutParent = view.findViewById(R.id.layoutParent)
        layoutVideo = view.findViewById(R.id.layoutVideo)
        layoutControl = view.findViewById(R.id.layoutControl)

        layoutParent.setOnClickListener{
            if(showingControlVideo) {
                layoutControl.visibility = View.INVISIBLE
            } else {
                layoutControl.visibility = View.VISIBLE
            }
            showingControlVideo = !showingControlVideo
        }

        playerView = view.findViewById(R.id.playerView)
        playerView.useController = false
        initializePlayer()

        imgPlay = view.findViewById(R.id.imgPlay)
        imgPlay.setOnClickListener {
            if(!playerView.player!!.isPlaying) {
                playerView.player!!.play()
                imgPlay.setImageResource(R.drawable.pause)
            } else {
                playerView.player!!.pause()
                imgPlay.setImageResource(R.drawable.play)
            }
        }

        imgVolume = view.findViewById(R.id.imgVolume)
        imgVolume.setOnClickListener {

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

//        val heightStatus = Utils.getStatusBarHeight(this.requireContext())
        val heightBottomBar = 0//Utils.getHeightBottomBar(this.requireContext())
        imgFullscreen = view.findViewById(R.id.imgFullscreen)
        imgFullscreen.setOnClickListener{

            if (layoutVideo.parent != null) {
                (layoutVideo.parent as ViewGroup).removeView(layoutVideo)
            }

            if(!isFullScreen) {
                val width = Utils.getScreenWidth().toInt().toPx()
                val height = Utils.getScreenHeight().toInt().toPx() + heightBottomBar.toPx()
                val offset:Float = ((width - height) / 2).toFloat()
                val lp = ConstraintLayout.LayoutParams(height, width)
                layoutVideo.layoutParams = lp
                layoutVideo.rotation = -90.0f
                layoutVideo.translationX = offset
                layoutVideo.translationY = -offset

                requireActivity().window.addContentView(layoutVideo, lp)

            } else {
                val width = Utils.getScreenWidth().toInt().toPx()
                val height = (width*9)/16
                val offset:Float = ((width - height) / 2).toFloat()
                val lp = ConstraintLayout.LayoutParams(width, height)
                layoutVideo.layoutParams = lp
                layoutVideo.rotation = 0.0f
                layoutVideo.translationX = 0f
                layoutVideo.translationY = 0f

                layoutParent.addView(layoutVideo)
            }
            isFullScreen = !isFullScreen

            if(listener != null) {
                listener!!.videofragment_fullScreen(isFullScreen)
            }
        }

        return view
    }

    private fun initializePlayer() {

        var urlVideo = ""
        //urlVideo = "https://media.tinmoi24.vn/24h/upload/3-2021/videoclip/2021-07-05/1625467769-sr16_jasspers1.m3u8"
        urlVideo = "https://suckhoedoisong.qltns.mediacdn.vn/324455921873985536/2021/9/3/CKKWDprrCZg-16306723867591730630975.mp4"

        playerView.requestFocus()
        val videoPlayer = SimpleExoPlayer.Builder(requireContext()).build()
        val mediaDataSourceFactory = DefaultDataSourceFactory(requireContext())

        var mediaSource:MediaSource? = null
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

        videoPlayer.addMediaSource(mediaSource!!)
        playerView.player = videoPlayer
        videoPlayer.prepare()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VideoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VideoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}