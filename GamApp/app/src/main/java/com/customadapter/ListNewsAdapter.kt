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
import org.json.JSONArray
import org.json.JSONObject


public class ListNewsAdapter(var mList: JSONArray, var enableLoadMore:Boolean = true, var isRelative:Boolean = false) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
        return mList.length()
    }

//    fun getItem(position: Int): String? {
//        return mList.get(position)
//    }

    override fun getItemViewType(position: Int): Int {
        if (mList[position] == "loading") {
            return Constant.VIEW_TYPE_LOADING
        }

        if(isRelative) {
            return Constant.VIEW_TYPE_ITEM
        }

        val jObject = mList[position]
        if(jObject is JSONObject) {
            if(jObject.has("art_view")) {
                val art_view = jObject.getInt("art_view")
                Log.d("vietnb", "art_view: $art_view")
                if(art_view == 1) {
                    return Constant.VIEW_TYPE_ITEM_LARGE
                } else if(art_view == 2) {
                    return Constant.VIEW_TYPE_ITEM_THREE
                }
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
            val jObject = JSONObject(mList[position].toString())
            val doc = jObject.getJSONObject("Doc")
            val art = doc.getJSONObject("Art")

            if(art.has("title")) {
                holder.txtTitle.text = art["title"].toString()
            }

            if(art.has("cover")) {
                val cover = art["cover"].toString()

                val radius = 20; // corner radius, higher value = more rounded
                val margin = 0; // crop margin, set to 0 for corners with no crop
                Glide.with(holder.imgCover.context)
                    .load(cover)
                    .transform(RoundedCornersTransformation(radius, margin))
                    .placeholder(R.drawable.thumbnews)
                    .into(holder.imgCover)

            }

            if(art.has("listimage")) {
                val listimage = art.getJSONArray("listimage")
                if(listimage.length()> 2) {
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
            }

            var nameWebsite = ""
            if(art.has("sid")) {
                val sid = art.getInt("sid")
                nameWebsite = Global.getNameWebsite(sid, holder.txtDesc.context)
            }

            if(art.has("posttime")) {
                val posttime:Long = art["posttime"] as Long
                nameWebsite = nameWebsite + " • " + Global.currentTimeSecUTC(posttime)
            }

            holder.imgIconMask.visibility = View.INVISIBLE
            if(art.has("listVideos")) {
                val listVideos = art.getJSONArray("listVideos")
                if(listVideos.length() > 0) {
                    holder.imgIconMask.visibility = View.VISIBLE
                }
            }

            if(art.has("sizeCmt")) {
                val sizeCmt = art.getInt("sizeCmt")
                if(sizeCmt > 0) {
                    nameWebsite = nameWebsite + " • " + sizeCmt
                    holder.imgComment.visibility = View.VISIBLE
                }
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
        var last = mList.length() - 1
        mList.remove(last)
        notifyItemRemoved(last)
    }

    fun addData(array: JSONArray) {
        var start = this.mList.length()
        for (i in 0 until array.length()) {
            this.mList.put(array[i])
        }
        notifyItemRangeInserted(start, this.mList.length())
    }

    fun addLoadingView() {
        //add loading item
        mList.put("loading")
        notifyItemInserted(mList.length() - 1)
    }
}