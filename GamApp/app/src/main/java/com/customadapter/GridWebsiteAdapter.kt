package com.customadapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barservicegam.app.R
import org.json.JSONArray

class GridWebsiteAdapter(var mList: JSONArray) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var arrMore: ArrayList<Int> = ArrayList<Int>()

    class GridHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtTitle: TextView = itemView!!.findViewById(R.id.txtTitle)
        var grdWebsite: RecyclerView = itemView!!.findViewById(R.id.grdWebsite)
        var btnMore: Button = itemView!!.findViewById(R.id.btnMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GridHolder(LayoutInflater.from(parent.context).inflate(R.layout.grid_holder, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is GridHolder) {
            val jObject = mList.getJSONObject(position)
            if(jObject.has("name")) {
                holder.txtTitle.text = jObject.getString("name")
                holder.btnMore.text = "Thu gọn"
                holder.btnMore.visibility = View.GONE
                if(mList.length() > 3) {
                    holder.btnMore.visibility = View.VISIBLE
                }

                var count = mList.length()
                if(!arrMore.contains(position)) {
                    if(mList.length() > 3) {
                        count = 3
                        holder.btnMore.text = "Mở rộng"
                    }
                }

                val mListProcess = JSONArray()
                for(i in 0 until count) {
                    mListProcess.put(mList[i])
                }

                holder.grdWebsite.adapter = WebsiteAdapter(mListProcess)
                holder.grdWebsite.layoutManager = GridLayoutManager(holder.grdWebsite.context, 3)

                holder.btnMore.setOnClickListener {
                    Log.d("vietnb", "click More")
                    if(arrMore.contains(position)) {
                        arrMore.remove(position)
                    }
                    else {
                        arrMore.add(position)
                    }
                    notifyItemChanged(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.length()
    }

}