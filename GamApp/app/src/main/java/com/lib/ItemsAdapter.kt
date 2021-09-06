package com.khaolok.myloadmoreitem

import ClickListener
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.barservicegam.app.R
import com.google.android.gms.ads.admanager.AdManagerAdView

public class ItemsAdapter(var mList: ArrayList<String?>, var mContext: Context, var btnAds: AdManagerAdView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class MonthHolder(v: View) : RecyclerView.ViewHolder(v){
        var btnClickItem: Button = itemView!!.findViewById(R.id.btnClickItem)
        var media_container: FrameLayout = itemView!!.findViewById(R.id.media_container)
    }

    interface ClickListener {
        fun onItemClick(v: View,position: Int)
    }

    inner public class LoadingHolder(v: View) : RecyclerView.ViewHolder(v)

    inner public class NoneHolder(v: View) : RecyclerView.ViewHolder(v){
        //var btnNone: Button = itemView!!.findViewById(R.id.btnNone)
        var items_linear_rv = itemView!!.findViewById<RecyclerView>(R.id.rclView)
    }

    private var listener: OnClickListener? = null

    fun setListener(listener: OnClickListener) {
        this.listener = listener
    }

    interface OnClickListener {
        fun onClickItem(position: Int, monthHolder: MonthHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == Constant.VIEW_TYPE_ITEM) {
            return MonthHolder(LayoutInflater.from(parent.context).inflate(R.layout.linear_item_row, parent, false))
        }
        else if(viewType == Constant.VIEW_TYPE_NONE)
        {
            return NoneHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_none, parent, false))
        }

        return LoadingHolder(LayoutInflater.from(parent.context).inflate(R.layout.progress_loading, parent, false))
    }

    override fun getItemCount(): Int {
        if(mList == null) return 0
        return mList!!.size
    }

    fun getItem(position: Int): String? {
        //return if (mDataSet != null) mDataSet[position] else null
        return mList?.get(position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (mList[position] == null) {
            Constant.VIEW_TYPE_LOADING
        } else {
            if(position == 12)
            {
                return Constant.VIEW_TYPE_NONE
            }

            Constant.VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is NoneHolder) {
        }
        else if (holder is MonthHolder)
        {
            holder.btnClickItem.text = mList[position]

            holder.btnClickItem.setOnClickListener {
                listener?.onClickItem(position, holder)
            }

//            holder.btnClickItem.setOnClickListener{
////                MyApplication.itemClick = position
////                Toast.makeText(this.mContext, "Click Item: " + mList[position], Toast.LENGTH_SHORT).show()
////                Log.d("tag 55", "goi ham holder.btnClickItem : " + MyApplication.itemClick)
//            }
        }
    }

    ////them
    fun removeLoadingView() {
        //Remove loading item
        if (mList.size != 0) {
            mList.removeAt(mList.size - 1)
            notifyItemRemoved(mList.size)
        }
    }

    fun addData(dataViews: ArrayList<String?>) {
        this.mList.addAll(dataViews)
        notifyDataSetChanged()
    }

    fun addLoadingView() {
        //add loading item
        Handler().post {
            mList.add(null)
            notifyItemInserted(mList.size - 1)
        }
    }
}










