package com.customadapter.setting

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.main.app.MainActivity
import com.barservicegam.app.R
import com.customadapter.ListNewsAdapter
import com.dialog.LogoutDialog
import com.fragmentcustom.autoplayvideo.AutoplayVideoFragment
import com.lib.Utils
import org.json.JSONArray
import org.json.JSONObject


class settingtextAdapter(var mList: JSONArray) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class settingtextholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgIcon: ImageView = itemView!!.findViewById(R.id.imgIcon)
        var txtTitle: TextView = itemView!!.findViewById(R.id.txtStar)
        var layoutParent: ConstraintLayout = itemView!!.findViewById(R.id.layoutParent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return settingtextholder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.settingtextholder,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is settingtextholder) {
            val jObject = mList.getJSONObject(position)

            val text = jObject.getString("text")
            holder.txtTitle.text = text
            Utils.setImageName(jObject.getString("icon"), holder.imgIcon.context, holder.imgIcon)

            holder.layoutParent.setOnClickListener {
                val topActivity = Utils.getActivity(holder.layoutParent.context)
                if(text == "Đăng xuất") {
                    if(topActivity is MainActivity) {
                        val fm: FragmentManager = topActivity.supportFragmentManager
                        val popupDialogFrag = LogoutDialog.newInstance("ho1", "hi2")
                        popupDialogFrag.show(fm, "popup")
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.length()
    }

    fun addData(jObject: JSONObject) {
        this.mList.put(jObject)
        notifyItemInserted(this.mList.length())
    }
}