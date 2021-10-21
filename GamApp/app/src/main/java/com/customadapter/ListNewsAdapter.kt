package com.customadapter

import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.api.Global
import com.barservicegam.app.R
import com.bumptech.glide.Glide
import com.customview.BVPlayerVideo
import com.khaolok.myloadmoreitem.Constant
import com.lib.Utils
import com.main.app.MainActivity
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import model.PListingResponse
import model.PVideo


public class ListNewsAdapter(
    var mList: MutableList<model.PListingResponse.DocumentOrBuilder>,
    var enableLoadMore: Boolean = true,
    var isRelative: Boolean = false
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var positionVideoPlay: Int = -1
    lateinit var bvPlayerVideo: BVPlayerVideo

    inner class NewsHolder1(v: View) : RecyclerView.ViewHolder(v){
        var imgIconMask: ImageView = itemView!!.findViewById(R.id.imgIconMask)
        var txtTitle: TextView = itemView!!.findViewById(R.id.txtStar)
        var imgCover: ImageView = itemView!!.findViewById(R.id.imgCover)
        var txtDesc: TextView = itemView!!.findViewById(R.id.txtCategory)
        var imgComment: ImageView = itemView!!.findViewById(R.id.imgComment)
        var constraintLayoutP: ConstraintLayout = itemView!!.findViewById(R.id.constraintLayoutP)

        var imgCover2: ImageView? = null
        var imgCover3: ImageView? = null

        init {

            val typefaceTitle = Typeface.createFromAsset(
                txtDesc.getContext().assets,
                "fonts/sfuidisplaysemibold.ttf"
            )
            txtTitle.setTypeface(typefaceTitle)
//            txtTitle.setTextColor(Color.parseColor("#444444"))
            txtTitle.setTextColor(txtDesc.getResources().getColor(R.color.titlenewscolor, null))

            val typefaceDesc = Typeface.createFromAsset(
                txtDesc.getContext().assets,
                "fonts/sfuidisplaymedium.ttf"
            )
            txtDesc.setTypeface(typefaceDesc)
            txtDesc.setTextColor(txtDesc.getResources().getColor(R.color.descnewscolor, null))

            if(itemView.findViewById<ImageView>(R.id.imgCover2) != null) {
                imgCover2 = itemView.findViewById<ImageView>(R.id.imgCover2)
            }

            if(itemView.findViewById<ImageView>(R.id.imgCover3) != null) {
                imgCover3 = itemView.findViewById<ImageView>(R.id.imgCover3)
            }
        }
    }

    inner class ListTopic(v: View) : RecyclerView.ViewHolder(v){

    }

    inner class VideoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtTitle: TextView = itemView!!.findViewById(R.id.txtStar)
        var imgWebsite: ImageView = itemView!!.findViewById(R.id.imgWebsite)
        var imgPlay: ImageView = itemView!!.findViewById(R.id.imgPlay)
        var imgCover: ImageView = itemView!!.findViewById(R.id.imgCover)
        var layoutVideo: ConstraintLayout = itemView!!.findViewById(R.id.layoutVideo)
        var txtDesc: TextView = itemView!!.findViewById(R.id.txtCategory)

        var imgShare: ImageView = itemView!!.findViewById(R.id.imgShare)
        var txtComment: TextView = itemView!!.findViewById(R.id.txtComment)
        var imgComment: ImageView = itemView!!.findViewById(R.id.imgComment)

        init {
            val typefaceTitle = Typeface.createFromAsset(
                txtTitle.getContext().assets,
                "fonts/sfuidisplaysemibold.ttf"
            )
            txtTitle.setTypeface(typefaceTitle)
            txtTitle.setTextColor(txtTitle.getResources().getColor(R.color.titlenewscolor, null))

            val typefaceDesc = Typeface.createFromAsset(
                txtDesc.getContext().assets,
                "fonts/sfuidisplaymedium.ttf"
            )
            txtDesc.setTypeface(typefaceDesc)
            txtDesc.setTextColor(txtDesc.getResources().getColor(R.color.descnewscolor, null))
        }

        fun pauseVideo() {
            if(bvPlayerVideo != null) {
                bvPlayerVideo.pauseVideo()
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

//            var urlVideo = ""
//            val mVideo = this.mList.getJSONObject(position)
//            if(mVideo.has("listVideos")) {
//                val listVideos = mVideo.getJSONArray("listVideos")
//                if(listVideos.length() > 0) {
//                    urlVideo = listVideos[0].toString()
//                }
//            }
//
//            bvPlayerVideo.loadVideo(urlVideo)
//            notifyItemChanged(position)
        }

        fun bindData(video: PVideo.VideoMsg, position: Int) {

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

            this.txtTitle.text = video.title

            val cover = video.cover

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

            var nameWebsite = ""
            val sid = video.sid
            nameWebsite = Global.getNameWebsite(sid, this.txtDesc.context)
            this.txtDesc.text = nameWebsite

            this.imgCover.setOnClickListener{
                if(positionVideoPlay != position) {
                    //showVideoPosition(position)
                }
            }

            this.imgShare.setOnClickListener {
                val topActivity = Utils.getActivity(this.imgShare.context)
                if(topActivity is MainActivity) {
                    val fplayurl = video.fplayurl
                    topActivity.shareVideo(fplayurl)
                }
            }

            imgComment.visibility = View.INVISIBLE
            txtComment.visibility = View.INVISIBLE
            val sizeCmt = video.sizeCmt
            if(sizeCmt > 0) {
                txtComment.text = sizeCmt.toString()
                imgComment.visibility = View.VISIBLE
                txtComment.visibility = View.VISIBLE
            }
        }
    }

    inner class EventHolder(v: View) : RecyclerView.ViewHolder(v){

    }

    inner class NoticeHolder(v: View) : RecyclerView.ViewHolder(v){

    }

    inner class SponserHolder(v: View) : RecyclerView.ViewHolder(v){

    }

    inner class CollectionHolder(v: View) : RecyclerView.ViewHolder(v){

    }

    inner class AdsHolder(v: View) : RecyclerView.ViewHolder(v){

    }

    inner class UgcHolder(v: View) : RecyclerView.ViewHolder(v){

    }

    inner class UtilityHolder(v: View) : RecyclerView.ViewHolder(v){

    }

    interface ListNewsAdapterListener {
        fun click_ListNewsAdapterListener(position: Int)
    }

    private var listener: ListNewsAdapterListener? = null

    fun setListNewsAdapterListener(listener: ListNewsAdapterListener?) {
        this.listener = listener
    }

    inner public class LoadingHolder(v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == Constant.VIEW_TYPE_ITEM) {
            return NewsHolder1(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.newsholder1,
                    parent,
                    false
                )
            )
        }

        if (viewType == Constant.VIEW_TYPE_ITEM_LARGE) {
            return NewsHolder1(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.newsholder2,
                    parent,
                    false
                )
            )
        }

        if (viewType == Constant.VIEW_TYPE_ITEM_THREE) {
            return NewsHolder1(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.newsholder3,
                    parent,
                    false
                )
            )
        }

        if (viewType == Constant.VIEW_TYPE_LIST_TOPIC) {
            return ListTopic(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.listtopicholder,
                    parent,
                    false
                )
            )
        }

        if (viewType == Constant.VIEW_TYPE_VIDEO) {
            return VideoHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.listimageholder,
                    parent,
                    false
                )
            )
        }

        if (viewType == Constant.VIEW_TYPE_SPONSER) {
            return SponserHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.sponserholder,
                    parent,
                    false
                )
            )
        }

        if (viewType == Constant.VIEW_TYPE_LIST_EVENT) {
            return EventHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.eventholder,
                    parent,
                    false
                )
            )
        }

        if (viewType == Constant.VIEW_TYPE_LIST_COLLECTION) {
            return CollectionHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.collectionholder,
                    parent,
                    false
                )
            )
        }

        if (viewType == Constant.VIEW_TYPE_NOTICE) {
            return NoticeHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.noticeholder,
                    parent,
                    false
                )
            )
        }

        if (viewType == Constant.VIEW_TYPE_ADS) {
            return AdsHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.adsholder,
                    parent,
                    false
                )
            )
        }

        if (viewType == Constant.VIEW_TYPE_UTILITY) {
            return UtilityHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.utilityholder,
                    parent,
                    false
                )
            )
        }

        if (viewType == Constant.VIEW_TYPE_UGC) {
            return UgcHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.ugcholder,
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

    override fun getItemCount(): Int {
        return mList.count()
    }

    override fun getItemViewType(position: Int): Int {
        if(isRelative) {
            return Constant.VIEW_TYPE_ITEM
        }

        if(position == 0) {
            return Constant.VIEW_TYPE_ITEM_LARGE
        }

        val document = mList.get(position)

        if (document.art.title == "loading") {
            return Constant.VIEW_TYPE_LOADING
        }

        val contentType = document.type
        if(contentType == PListingResponse.EContentType.CONTENT_TYPE_ARTICLES) {
            if(document.artView == PListingResponse.EArticleViewType.VIEW_FEATURE) {
                return Constant.VIEW_TYPE_ITEM_LARGE //to
            } else if(document.artView == PListingResponse.EArticleViewType.VIEW_THREE) {
                return Constant.VIEW_TYPE_ITEM_THREE //3 anh
            }
            return Constant.VIEW_TYPE_ITEM //thuong
        } else if(contentType == PListingResponse.EContentType.CONTENT_TYPE_VIDEOS) {
            return Constant.VIEW_TYPE_VIDEO
        } else if(contentType == PListingResponse.EContentType.CONTENT_TYPE_LIST_TOPICS) {
            return Constant.VIEW_TYPE_LIST_TOPIC
        } else if(contentType == PListingResponse.EContentType.CONTENT_TYPE_VIDEOS) {
            return Constant.VIEW_TYPE_VIDEO
        } else if(contentType == PListingResponse.EContentType.CONTENT_TYPE_NOTICE) {
            return Constant.VIEW_TYPE_NOTICE
        } else if(contentType == PListingResponse.EContentType.CONTENT_TYPE_GOOGLE_ADS) {
            return Constant.VIEW_TYPE_ADS
        } else if(contentType == PListingResponse.EContentType.CONTENT_TYPE_SPONSORS) {
            return Constant.VIEW_TYPE_SPONSER
        } else if(contentType == PListingResponse.EContentType.CONTENT_TYPE_COLLECTION) {
            return Constant.VIEW_TYPE_LIST_COLLECTION
        } else if(contentType == PListingResponse.EContentType.CONTENT_TYPE_EVENTS) {
            return Constant.VIEW_TYPE_LIST_EVENT
        } else if(contentType == PListingResponse.EContentType.CONTENT_TYPE_UTILITIES) {
            return Constant.VIEW_TYPE_UTILITY
        } else if(contentType == PListingResponse.EContentType.CONTENT_TYPE_UGC) {
            return Constant.VIEW_TYPE_UGC
        }

        return Constant.VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NewsHolder1)
        {
            holder.imgComment.visibility = View.INVISIBLE
            val document = mList.get(position)
            val art = document.art
            holder.txtTitle.text = art.title

            val cover = art.cover

            val radius = 20; // corner radius, higher value = more rounded
            val margin = 0; // crop margin, set to 0 for corners with no crop
            Glide.with(holder.imgCover.context)
                .load(cover)
                .transform(RoundedCornersTransformation(radius, margin))
                .placeholder(R.drawable.thumbnews)
                .into(holder.imgCover)

            val listimage = art.listimageList
            if(listimage.count()> 2) {
                val cover1 = listimage[1].toString()
                val radius = 20; // corner radius, higher value = more rounded
                val margin = 0; // crop margin, set to 0 for corners with no crop

                if(holder.imgCover2 != null) {
                    Glide.with(holder.imgCover2!!.context)
                        .load(cover1)
                        .transform(RoundedCornersTransformation(radius, margin))
                        .placeholder(R.drawable.thumbnews)
                        .into(holder.imgCover2!!)
                }

                if(holder.imgCover3 != null) {
                    val cover2 = listimage[2].toString()
                    Glide.with(holder.imgCover3!!.context)
                        .load(cover2)
                        .transform(RoundedCornersTransformation(radius, margin))
                        .placeholder(R.drawable.thumbnews)
                        .into(holder.imgCover3!!)
                }
            }

            var nameWebsite = ""
            val sid = art.sid
            nameWebsite = Global.getNameWebsite(sid, holder.txtDesc.context)

            val posttime:Long = art.posttime
            nameWebsite = nameWebsite + " • " + Global.currentTimeSecUTC(posttime)

            holder.imgIconMask.visibility = View.INVISIBLE
            val listVideos = art.listVideosList
            if(listVideos.count() > 0) {
                holder.imgIconMask.visibility = View.VISIBLE
            }

            val sizeCmt = art.sizeCmt
            if(sizeCmt > 0) {
                nameWebsite = nameWebsite + " • " + sizeCmt
                holder.imgComment.visibility = View.VISIBLE
            }

            holder.txtDesc.text = nameWebsite
            //holder.txtDesc.text = "24h • 2 giờ • 15"

            holder.constraintLayoutP.setOnClickListener{
                if(listener != null) {
                    //luu bai
                    val document = mList.get(position)
                    Log.d("vietnb", "document AAA: $document")

                    this.listener!!.click_ListNewsAdapterListener(position)
                }
            }
        } else if(holder is VideoHolder) {
            val document = mList.get(position)
            val video = document.video
            holder.bindData(video, position)
        }
    }

    fun removeLoadingView() {
        //Remove loading item
        var last = mList.count() - 1
        mList.removeAt(last)
        notifyItemRemoved(last)
    }

    fun addData(array: MutableList<PListingResponse.Document>) {
        var start = this.mList.count()
        for (i in 0 until array.count()) {
            mList.add(array.get(i))

        }
        notifyItemRangeInserted(start, this.mList.count())
    }

    fun addLoadingView() {
        //add loading item
        var artLoading = model.PArticle.ArticleMsg.newBuilder().setTitle("loading")
        val document = PListingResponse.Document.newBuilder().setArt(artLoading)
        mList.add(document)
        notifyItemInserted(mList.count() - 1)
    }
}