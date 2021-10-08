package com.customadapter.ball

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.barservicegam.app.R
import com.customview.ball.listguessmatch
import com.khaolok.myloadmoreitem.Constant
import org.json.JSONArray
import org.json.JSONObject

public class ListTitleGuessMatchAdapter(var mList: JSONArray) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class TitleGuessMatchHolder(v: View) : RecyclerView.ViewHolder(v){
        var txtTitle: TextView = itemView!!.findViewById(R.id.txtStar)
        var listguessmatch: listguessmatch = itemView!!.findViewById(R.id.listguessmatch)

        init {
            val typefaceTitle = Typeface.createFromAsset(txtTitle.getContext().assets, "fonts/sfuidisplaysemibold.ttf")
            txtTitle.setTypeface(typefaceTitle)
            txtTitle.setTextColor(txtTitle.getResources().getColor(R.color.titlenewscolor, null))
        }
    }

    inner public class LoadingHolder(v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == Constant.VIEW_TYPE_ITEM) {
            return TitleGuessMatchHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.titleguessmatchholder,
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

    override fun getItemViewType(position: Int): Int {
        if (mList[position] == "loading") {
            return Constant.VIEW_TYPE_LOADING
        }

        return Constant.VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TitleGuessMatchHolder)
        {
            val jObject = JSONObject(mList[position].toString())
            if(jObject.has("match_time")) {
                holder.txtTitle.text = jObject.getString("match_time")

                if(jObject.has("match")) {
                    val match = jObject.getJSONArray("match")
                    holder.listguessmatch.refreshData(match)
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