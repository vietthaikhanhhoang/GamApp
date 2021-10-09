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
import com.khaolok.myloadmoreitem.Constant
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import model.PListingResponse
import org.json.JSONArray
import org.json.JSONObject


public class ListNewsAdapter(var mList: MutableList<model.PListingResponse.DocumentOrBuilder>, var enableLoadMore:Boolean = true, var isRelative:Boolean = false) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

            val typefaceTitle = Typeface.createFromAsset(txtDesc.getContext().assets, "fonts/sfuidisplaysemibold.ttf")
            txtTitle.setTypeface(typefaceTitle)
//            txtTitle.setTextColor(Color.parseColor("#444444"))
            txtTitle.setTextColor(txtDesc.getResources().getColor(R.color.titlenewscolor, null))

            val typefaceDesc = Typeface.createFromAsset(txtDesc.getContext().assets, "fonts/sfuidisplaymedium.ttf")
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
        val document = mList.get(position)

        if (document.art.title == "loading") {
            return Constant.VIEW_TYPE_LOADING
        }

        if(isRelative) {
            return Constant.VIEW_TYPE_ITEM
        }

        if(document is PListingResponse.Document) {
            if(document.artView == PListingResponse.EArticleViewType.VIEW_FEATURE) {
                return Constant.VIEW_TYPE_ITEM_LARGE
            } else if(document.artView == PListingResponse.EArticleViewType.VIEW_THREE) {
                return Constant.VIEW_TYPE_ITEM_THREE
            }
        }

        if(position == 0) {
            return Constant.VIEW_TYPE_ITEM_LARGE
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
                    this.listener!!.click_ListNewsAdapterListener(position)
                }
            }
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