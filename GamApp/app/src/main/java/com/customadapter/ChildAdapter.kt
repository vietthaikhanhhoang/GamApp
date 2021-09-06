package com.customadapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.customview.ChildView
import org.json.JSONArray

class ChildAdapter : BaseAdapter {
    var mList = JSONArray()
    var context: Context? = null

    constructor(context: Context?, mList: JSONArray) : super() {
        this.context = context
        this.mList = mList
    }

    override fun getCount(): Int {
        return mList.length()
    }

    override fun getItem(position: Int): Any {
        return mList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val jObject = this.mList.getJSONObject(position)
        val childView = ChildView(parent!!.context)
        childView.bindData(jObject)
        return childView
    }
}