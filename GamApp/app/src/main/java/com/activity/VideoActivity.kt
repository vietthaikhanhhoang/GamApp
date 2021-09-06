package com.activity

import android.media.MediaPlayer.OnPreparedListener
import android.os.Bundle
import android.os.Handler
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.barservicegam.app.R


class VideoActivity : AppCompatActivity() {
    var current_pos:Int = 0
    var total_duration:Int = 0

    // declaring a null variable for VideoView
    var simpleVideoView: VideoView? = null
    // declaring a null variable for MediaController
    var mediaControls: MediaController? = null

    var seekBar:SeekBar? = null

    fun getIDLayoutButton()
    {
        val btnPlay = findViewById<Button>(R.id.btnPlay)
        val btnPause = findViewById<Button>(R.id.btnPause)
        seekBar = findViewById<SeekBar>(R.id.seekBar)
        simpleVideoView = findViewById<VideoView>(R.id.simpleVideoView)

        btnPlay.setOnClickListener{
            // starting the video
            simpleVideoView!!.start()
        }

        btnPause.setOnClickListener{
            simpleVideoView!!.pause()
        }

        simpleVideoView!!.setOnPreparedListener(OnPreparedListener { setVideoProgress() })
    }

    // display video progress
    fun setVideoProgress() {
        //get the video duration
        //current_pos = videoView.getCurrentPosition()
        total_duration = simpleVideoView!!.duration

        //display video duration
        //total.setText(timeConversion(total_duration as Long))
        //current.setText(timeConversion(current_pos as Long))
        seekBar!!.setMax(total_duration as Int)
        val handler = Handler()
        val runnable: Runnable = object : Runnable {
            override fun run() {
                try {
                    current_pos = simpleVideoView!!.currentPosition
                    //current.setText(timeConversion(current_pos as Long))
                    seekBar!!.setProgress(current_pos)
                    handler.postDelayed(this, 1000)
                } catch (ed: IllegalStateException) {
                    ed.printStackTrace()
                }
            }
        }
        handler.postDelayed(runnable, 1000)

        //seekBar change listner
        seekBar!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                current_pos = seekBar.progress
                simpleVideoView!!.seekTo(current_pos as Int)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        getIDLayoutButton()

        if (mediaControls == null) {
            // creating an object of media controller class
            mediaControls = MediaController(this)
            // set the anchor view for the video view
            mediaControls!!.setAnchorView(this.simpleVideoView)
        }

        // set the media controller for video view
        //simpleVideoView!!.setMediaController(mediaControls)

        // set the absolute path of the video file which is going to be played
//        simpleVideoView!!.setVideoURI(Uri.parse("android.resource://"
//                + packageName + "/" + R.raw.gfgvideo))

        simpleVideoView!!.setVideoPath("http://www.ebookfrenzy.com/android_book/movie.mp4")

        simpleVideoView!!.requestFocus()

        // display a toast message
        // after the video is completed
        simpleVideoView!!.setOnCompletionListener {
            Toast.makeText(applicationContext, "Video completed",
                    Toast.LENGTH_LONG).show()
        }

        // display a toast message if any
        // error occurs while playing the video
        simpleVideoView!!.setOnErrorListener { mp, what, extra ->
            Toast.makeText(applicationContext, "An Error Occured " +
                    "While Playing Video !!!", Toast.LENGTH_LONG).show()
            false
        }
    }

    fun showToast(text: String)
    {
        Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()
    }
}