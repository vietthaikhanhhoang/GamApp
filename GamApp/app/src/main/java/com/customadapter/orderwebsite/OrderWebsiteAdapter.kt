package com.customadapter.orderwebsite

import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.barservicegam.app.R
import com.bumptech.glide.Glide
import com.khaolok.myloadmoreitem.Constant
import de.hdodenhof.circleimageview.CircleImageView
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import org.json.JSONArray
import org.json.JSONObject

public class OrderWebsiteAdapter(var mList: Map<Int, String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class OrderWebsiteHolder(v: View) : RecyclerView.ViewHolder(v){
        var txtTitle: TextView = itemView!!.findViewById(R.id.txtTitle)
        var imgCover: CircleImageView = itemView!!.findViewById(R.id.imgCover)
        var layoutParent: LinearLayout = itemView!!.findViewById(R.id.layoutParent)

        init {
            val typefaceTitle = Typeface.createFromAsset(txtTitle.getContext().assets, "fonts/sfuidisplaysemibold.ttf")
            txtTitle.setTypeface(typefaceTitle)
            txtTitle.setTextColor(txtTitle.getResources().getColor(R.color.titlenewscolor, null))
        }
    }

    interface OrderWebsiteAdapterListener {
        fun click_OrderWebsiteAdapter(position: Int)
    }

    private var listener: OrderWebsiteAdapterListener? = null

    fun setOrderWebsiteAdapterListener(listener: OrderWebsiteAdapterListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OrderWebsiteHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.orderwebsiteholder,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {
        return Constant.VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OrderWebsiteHolder)
        {
            val arrValues = mList.values
            val value = arrValues.elementAt(position)
            val jObject = JSONObject(value.toString())
            holder.txtTitle.text = jObject.getString("name")

            val cover = jObject.getString("icon_url")
            val radius = 20; // corner radius, higher value = more rounded
            val margin = 0; // crop margin, set to 0 for corners with no crop
            Glide.with(holder.imgCover.context)
                .load(cover)
                .transform(RoundedCornersTransformation(radius, margin))
                .placeholder(R.drawable.thumbnews)
                .into(holder.imgCover)

            holder.layoutParent.setOnClickListener {
                if(listener != null) {
                    listener!!.click_OrderWebsiteAdapter(position)
                }
            }
        }
    }
}