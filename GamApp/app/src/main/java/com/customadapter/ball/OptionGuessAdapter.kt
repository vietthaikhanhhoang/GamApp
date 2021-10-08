package com.customadapter.ball

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.barservicegam.app.R
import com.google.android.gms.common.util.DataUtils
import com.khaolok.myloadmoreitem.Constant
import org.json.JSONArray
import org.json.JSONObject


public class OptionGuessAdapter(var mList: JSONArray, var totalRate: Int, var matchInfo: JSONObject) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var selectId: Int = 0 //khong chon gi

    inner class OptionGuessHolder(v: View) : RecyclerView.ViewHolder(v){
        var layoutParent:LinearLayout = v.findViewById(R.id.layoutParent)
        var txtRate:TextView = v.findViewById(R.id.txtRate)
        var rbSelect:RadioButton = v.findViewById(R.id.rbSelect)
        init {
            setRadioCircleColor(rbSelect)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OptionGuessHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.optionguessholder,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mList.length()
    }

    override fun getItemViewType(position: Int): Int {
        return Constant.VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OptionGuessHolder)
        {
            var jObject = mList.getJSONObject(position)
            var opt_id = jObject.getInt("opt_id")
            var match_id = jObject.getInt("match_id")
            var total_opt = jObject.getInt("total_opt")

            holder.txtRate.text = (total_opt*100.0f/totalRate).toInt().toString() + "%"


            holder.rbSelect.isChecked = false
            holder.rbSelect.setTextColor(holder.rbSelect.resources.getColor(R.color.white, null))
            holder.txtRate.setTextColor(holder.txtRate.resources.getColor(R.color.white, null))
            if(selectId - 1 == position) {
                holder.rbSelect.isChecked = true

                holder.rbSelect.setTextColor(
                    holder.rbSelect.resources.getColor(
                        R.color.mainredcolor,
                        null
                    )
                )
                holder.txtRate.setTextColor(
                    holder.txtRate.resources.getColor(
                        R.color.mainredcolor,
                        null
                    )
                )
            }

            holder.layoutParent.setOnClickListener {
                selectId = position + 1
                this.notifyDataSetChanged()
            }

            if(position == 0) {
                val home_club = matchInfo.getJSONObject("home_club")
                val name = home_club.getString("name")
                holder.rbSelect.text = name + " Thắng"
            } else if(position == 1) {
                holder.rbSelect.text = "Hoà"
            } else if(position == 2) {
                val away_club = matchInfo.getJSONObject("away_club")
                val name = away_club.getString("name")
                holder.rbSelect.text = name + " Thắng"
            }

        }
    }

    fun setRadioCircleColor(rbSelect: RadioButton){
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_checked)
            ), intArrayOf(
                rbSelect.resources.getColor(R.color.white, null),
                rbSelect.resources.getColor(R.color.mainredcolor, null)
            )
        )
        rbSelect.buttonTintList = colorStateList
    }
}