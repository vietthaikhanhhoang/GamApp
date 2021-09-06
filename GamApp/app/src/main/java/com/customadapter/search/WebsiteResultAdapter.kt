package com.customadapter.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.barservicegam.app.R
import com.bumptech.glide.Glide
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import org.json.JSONArray
import org.json.JSONObject

class WebsiteResultAdapter(var mList: JSONArray) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class WebsiteResult(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtTitle: TextView = itemView!!.findViewById(R.id.txtTitle)
        var imgCover: ImageView = itemView!!.findViewById(R.id.imgCover)

        fun bindData(data: JSONObject) {
            if(data.has("name")) {
                txtTitle.text = data.getString("name")
            }

            if(data.has("cover")) {
                val cover = data.getString("cover")

                val radius = 0; // corner radius, higher value = more rounded
                val margin = 0; // crop margin, set to 0 for corners with no crop
                Glide.with(imgCover.context)
                    .load(cover)
                    .transform(RoundedCornersTransformation(radius, margin))
                    .placeholder(R.drawable.thumbnews)
                    .into(imgCover)

            }

            imgCover.setOnClickListener{
                Log.d("vietnb", "click item")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WebsiteResult(LayoutInflater.from(parent.context).inflate(R.layout.websiteholder, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is WebsiteResult) {
            val jObject = this.mList.getJSONObject(position)
            holder.bindData(jObject)
        }
    }

    override fun getItemCount(): Int {
        return mList.length()
    }
}