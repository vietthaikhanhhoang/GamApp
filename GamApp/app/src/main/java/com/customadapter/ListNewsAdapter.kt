package com.customadapter

import android.R.array
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
import com.fragmentcustom.ContentFragment
import com.khaolok.myloadmoreitem.Constant
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import org.json.JSONArray
import org.json.JSONObject


public class ListNewsAdapter(var mList: JSONArray, var enableLoadMore:Boolean = true) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class NewsHolder1(v: View) : RecyclerView.ViewHolder(v){
        var txtTitle: TextView = itemView!!.findViewById(R.id.txtTitle)
        var imgCover: ImageView = itemView!!.findViewById(R.id.imgCover)
        var txtDesc: TextView = itemView!!.findViewById(R.id.txtDesc)
        var constraintLayoutP: ConstraintLayout = itemView!!.findViewById(R.id.constraintLayoutP)
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

        return Constant.VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NewsHolder1)
        {
            val art = JSONObject(mList[position].toString())
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

            var nameWebsite = ""
            if(art.has("sid")) {
                val sid = art.getInt("sid")
                //nameWebsite = Global.getNameWebsite(sid)
            }

            if(art.has("posttime")) {
                val posttime:Long = art["posttime"] as Long
                //nameWebsite = nameWebsite + " | " + Global.currentTimeSecUTC(posttime)
            }

            if(art.has("sizeCmt")) {
                val sizeCmt = art["sizeCmt"]
                nameWebsite = nameWebsite + " | " + sizeCmt + " Bình luận"
            }

            holder.txtDesc.text = nameWebsite

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