package com.customadapter

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.api.Global
import com.barservicegam.app.MainActivity
import com.barservicegam.app.R
import com.bumptech.glide.Glide
import com.customview.BVPlayerVideo
import com.khaolok.myloadmoreitem.Constant
import com.khaolok.myloadmoreitem.RecyclerViewPositionHelper
import com.lib.Utils
import org.json.JSONArray
import org.json.JSONObject

class ListVideoAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var positionVideoPlay: Int = -1
    lateinit  var rclView:RecyclerView
    lateinit  var mList:JSONArray
    lateinit var bvPlayerVideo: BVPlayerVideo

    constructor(context: Context, mList: JSONArray, rclView: RecyclerView): this(context) {
        this.mList = mList

//        positionVideoPlay = 1
        this.rclView = rclView
        this.bvPlayerVideo = BVPlayerVideo(this.context!!)

        rclView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        setPositionVideowhenScrolled()
                        println("end scroll")
                    }
//                    RecyclerView.SCROLL_STATE_DRAGGING -> //println("Scrolling now")
//                    RecyclerView.SCROLL_STATE_SETTLING -> //println("Scroll Settling")
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                //Kiem tra positionVideo
                //println("Dang scroll: $dx || $dy")
                checkVideoOutScreen()
            }
        })
    }

    interface ListVideoAdapterListener{
        fun clickListVideoAdapter(position: Int)
    }

    private var listener: ListVideoAdapterListener? = null

    fun setListener(listener: ListVideoAdapterListener) {
        this.listener = listener
    }

    inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtTitle: TextView = itemView!!.findViewById(R.id.txtTitle)
        var imgWebsite: ImageView = itemView!!.findViewById(R.id.imgWebsite)
        var imgPlay: ImageView = itemView!!.findViewById(R.id.imgPlay)
        var imgCover: ImageView = itemView!!.findViewById(R.id.imgCover)
        var layoutVideo: ConstraintLayout = itemView!!.findViewById(R.id.layoutVideo)
        var txtDesc: TextView = itemView!!.findViewById(R.id.txtDesc)

        var imgShare: ImageView = itemView!!.findViewById(R.id.imgShare)
        var txtComment: TextView = itemView!!.findViewById(R.id.txtComment)
        var imgComment: ImageView = itemView!!.findViewById(R.id.imgComment)

        init {
            val typefaceTitle = Typeface.createFromAsset(txtTitle.getContext().assets, "fonts/sfuidisplaysemibold.ttf")
            txtTitle.setTypeface(typefaceTitle)
            txtTitle.setTextColor(txtTitle.getResources().getColor(R.color.titlenewscolor, null))

            val typefaceDesc = Typeface.createFromAsset(txtDesc.getContext().assets, "fonts/sfuidisplaymedium.ttf")
            txtDesc.setTypeface(typefaceDesc)
            txtDesc.setTextColor(txtDesc.getResources().getColor(R.color.descnewscolor, null))
        }

        fun bindData(video: JSONObject, position: Int) {

            if(this.layoutVideo.findViewWithTag<BVPlayerVideo>(100) != null) {
                val btnExtra = this.layoutVideo.findViewWithTag<BVPlayerVideo>(100)
                if (btnExtra.parent != null) {
                    (btnExtra.parent as ViewGroup).removeView(btnExtra)
                }
            }

            if(positionVideoPlay == position) {
                bvPlayerVideo.tag = 100
                bvPlayerVideo.layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
                )
                this.layoutVideo.addView(bvPlayerVideo)
            }

            if(video.has("title")) {
                this.txtTitle.text = video.getString("title")
            }

            if(video.has("cover")) {
                val cover = video.getString("cover")

                val radius = 0; // corner radius, higher value = more rounded
                val margin = 0; // crop margin, set to 0 for corners with no crop
                Glide.with(this.imgCover.context)
                    .load(cover)
                    //.transform(RoundedCornersTransformation(radius, margin))
                    .placeholder(R.drawable.thumbnews)
                    .into(this.imgCover)

                Glide.with(this.imgWebsite.context)
                    .load(cover)
                    //.transform(RoundedCornersTransformation(12, margin))
                    .placeholder(R.drawable.thumbnews)
                    .into(this.imgWebsite)
            }

            var nameWebsite = ""
            if(video.has("sid")) {
                val sid = video.getInt("sid")
                nameWebsite = Global.getNameWebsite(sid, this.txtDesc.context)
            }
            this.txtDesc.text = nameWebsite

            this.imgCover.setOnClickListener{
                if(positionVideoPlay != position) {
                    showVideoPosition(position)
                }
            }

            this.imgShare.setOnClickListener {
                val topActivity = Utils.getActivity(this.imgShare.context)
                if(topActivity is MainActivity) {
                    if(video.has("fplayurl")) {
                        val fplayurl = video.getString("fplayurl")
                        topActivity.shareVideo(fplayurl)
                    }
                }
            }

            imgComment.visibility = View.INVISIBLE
            txtComment.visibility = View.INVISIBLE
            if(video.has("sizeCmt")) {
                val sizeCmt = video.getInt("sizeCmt")
                if(sizeCmt > 0) {
                    txtComment.text = sizeCmt.toString()
                    imgComment.visibility = View.VISIBLE
                    txtComment.visibility = View.VISIBLE
                }
            }
        }
    }

    inner class LoadingHolder(v: View) : RecyclerView.ViewHolder(v)

    override fun getItemViewType(position: Int): Int {
        if (mList[position] == "loading") {
            return Constant.VIEW_TYPE_LOADING
        }

        return Constant.VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == Constant.VIEW_TYPE_ITEM) {
            return ImageHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.listimageholder,
                    parent,
                    false
                )
            )
        }

        return LoadingHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.progress_loading,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ImageHolder) {
            val video = mList.getJSONObject(position)
            holder.bindData(video, position)
        }
    }

    override fun getItemCount(): Int {
        return mList.length()
    }

    fun setPositionVideowhenScrolled() {
        var position = RecyclerViewPositionHelper.createHelper(rclView).findFirstCompletelyVisibleItemPosition()
        if(position == -1) {
            position = RecyclerViewPositionHelper.createHelper(rclView).findFirstVisibleItemPosition()
        }

        Log.d( "vietnb", "vao den day aaa $position")
        Log.d( "vietnb", "positionVideoPlay $positionVideoPlay")
        if(positionVideoPlay != position) {
            showVideoPosition(position)
        }
    }

    fun showVideoPosition(position: Int) {
        if (bvPlayerVideo.parent != null) {
            (bvPlayerVideo.parent as ViewGroup).removeView(bvPlayerVideo)
        }

        Log.d("vietnb", "position luc nay: $position")
        if(position == -1) {
            pauseVideo()
            return
        }

        positionVideoPlay = position

        var urlVideo = ""
        val mVideo = this.mList.getJSONObject(position)
        if(mVideo.has("listVideos")) {
            val listVideos = mVideo.getJSONArray("listVideos")
            if(listVideos.length() > 0) {
                urlVideo = listVideos[0].toString()
            }
        }

        bvPlayerVideo.loadVideo(urlVideo)
        notifyItemChanged(position)
    }

    fun addData(array: JSONArray) {
        if (bvPlayerVideo.parent != null) {
            (bvPlayerVideo.parent as ViewGroup).removeView(bvPlayerVideo)
        }

        val positionStart = mList.length()
        for (i in 0 until array.length()) {
            this.mList.put(array[i])
        }
        notifyItemRangeInserted(positionStart, mList.length())
    }

    fun addLoadingView() {
        //add loading item
        mList.put("loading")
        notifyItemInserted(mList.length() - 1)
    }

    fun removeLoadingView() {
        //Remove loading item
        var last = mList.length() - 1
        mList.remove(last)
        notifyItemRemoved(last)
    }

    fun checkVideoOutScreen(){
//        Log.d("vietnb", "kiem tra: $positionVideoPlay")
//
//        if(positionVideoPlay != -1) {
//            val firstPosition = RecyclerViewPositionHelper.createHelper(rclView).findFirstVisibleItemPosition()
//            val lastPosition = RecyclerViewPositionHelper.createHelper(rclView).findLastVisibleItemPosition()
//            if(positionVideoPlay< firstPosition || lastPosition > positionVideoPlay) {
//                positionVideoPlay = -1
//                notifyItemChanged(firstPosition)
//                Log.d("vietnb", "xoa cai position: $positionVideoPlay")
//            }
//        }

    }

    fun pauseVideo() {
        if(bvPlayerVideo != null) {
            bvPlayerVideo.pauseVideo()
        }
    }

    fun playVideo() {
        if(bvPlayerVideo != null) {
            bvPlayerVideo.playVideo()
        }
    }

    fun readyVideo(isReady: Boolean) {
//        if(bvPlayerVideo != null) {
//            bvPlayerVideo.readyVideo(isReady)
//        }
    }
}