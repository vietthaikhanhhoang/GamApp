package com.customadapter.ball
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.barservicegam.app.R
import com.bumptech.glide.Glide
import com.fragula.extensions.addFragment
import com.khaolok.myloadmoreitem.Constant
import com.lib.Utils
import com.main.app.MainActivity
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import org.json.JSONArray
import org.json.JSONObject


public class ListGuessMatchAdapter(var mList: JSONArray) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class GuessMatchHolder(v: View) : RecyclerView.ViewHolder(v){
        var layoutParent: LinearLayout = itemView!!.findViewById(R.id.layoutParent)

        var txtSan: TextView = itemView!!.findViewById(R.id.txtSan)
        var txtGiai: TextView = itemView!!.findViewById(R.id.txtGiai)

        var imgHome: ImageView = itemView!!.findViewById(R.id.imgHome)
        var imgAway: ImageView = itemView!!.findViewById(R.id.imgAway)

        var txtHomeName: TextView = itemView!!.findViewById(R.id.txtHomeName)
        var txtAwayName: TextView = itemView!!.findViewById(R.id.txtAwayName)

        var txtHopeStar: TextView = itemView!!.findViewById(R.id.txtHopeStar)
        var layoutHopeStar: RelativeLayout = itemView!!.findViewById(R.id.layoutHopeStar)

        var txtStatus: TextView = itemView!!.findViewById(R.id.txtStatus)
        var txtTime: TextView = itemView!!.findViewById(R.id.txtTime)

        var layoutCenter: RelativeLayout = itemView!!.findViewById(R.id.layoutCenter)
        var layoutBottom: RelativeLayout = itemView!!.findViewById(R.id.layoutBottom)

        init {
            val typefaceTitle = Typeface.createFromAsset(txtSan.getContext().assets, "fonts/sfuidisplaymedium.ttf")
            txtSan.textSize = 15f
            txtSan.setTypeface(typefaceTitle)
            //txtSan.setTextColor(txtSan.getResources().getColor(R.color.gray, null))

            txtGiai.textSize = 15f
            txtGiai.setTypeface(typefaceTitle)
            //txtGiai.setTextColor(txtSan.getResources().getColor(R.color.gray, null))

            val typefaceTime = Typeface.createFromAsset(txtSan.getContext().assets, "fonts/sfuidisplaysemibold.ttf")
            txtTime.setTypeface(typefaceTime)
        }
    }

    interface ListGuessMatchAdapterListener {
        fun click_ListGuessMatchAdapter(position: Int)
    }

    private var listener: ListGuessMatchAdapterListener? = null

    fun setListGuessMatchAdapter(listener: ListGuessMatchAdapterListener) {
        this.listener = listener
    }

    inner public class LoadingHolder(v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GuessMatchHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.guessmatchholder,
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
        if (holder is GuessMatchHolder)
        {
            val match = JSONObject(mList[position].toString())

            holder.layoutParent.setOnClickListener {
                if(listener != null) {
                    listener!!.click_ListGuessMatchAdapter(position)
                }
            }

            if(match.has("home_club")) {
                val home_club = match.getJSONObject("home_club")
                val logo = home_club.getString("logo")
                val name = home_club.getString("name")
                holder.txtHomeName.text = name

                val radius = 20
                val margin = 0
                Glide.with(holder.imgHome.context)
                    .load(logo)
                    .transform(RoundedCornersTransformation(radius, margin))
                    .placeholder(R.drawable.thumbnews)
                    .into(holder.imgHome)
            }

            if(match.has("away_club")) {
                val away_club = match.getJSONObject("away_club")
                val logo = away_club.getString("logo")
                val name = away_club.getString("name")
                holder.txtAwayName.text = name

                val radius = 20
                val margin = 0
                Glide.with(holder.imgAway.context)
                    .load(logo)
                    .transform(RoundedCornersTransformation(radius, margin))
                    .placeholder(R.drawable.thumbnews)
                    .into(holder.imgAway)
            }

            if(match.has("match_info")) {
                holder.txtTime.text = match.getString("match_info")
            }

            if(match.has("match_venue")) {
                holder.txtSan.text = match.getString("match_venue")
            }

            if(match.has("match_group_icon")) {
                holder.txtGiai.text = match.getString("match_group_icon")
            }

            if(match.has("status")) {
                val status = match.getInt("status")
                if(status == 0) {
                    holder.txtStatus.text = "Sắp diễn ra"
                    holder.txtStatus.setBackgroundResource(R.color.predictColor)

                    holder.layoutCenter.setBackgroundResource(R.color.bgpredictColor)
                    holder.layoutBottom.setBackgroundResource(R.color.bgpredictColor)

                    holder.txtTime.setBackgroundResource(R.color.backgroundGrayLightColor)
                    holder.txtTime.setTextColor(holder.txtTime.resources.getColor(R.color.grayColor, null))

                } else if(status == 1) {
                    holder.txtStatus.text = "Đang diễn ra"
                    holder.txtStatus.setBackgroundResource(R.color.predictFalseColor)

                    holder.layoutCenter.setBackgroundResource(R.color.bgpredictColor)
                    holder.layoutBottom.setBackgroundResource(R.color.bgpredictColor)

                    holder.txtTime.setBackgroundResource(R.color.backgroundGrayLightColor)
                    holder.txtTime.setTextColor(holder.txtTime.resources.getColor(R.color.predictFalseColor, null))
                } else if(status == 2) {
                    holder.txtStatus.text = "Đã kết thúc"
                    holder.txtStatus.setBackgroundColor(holder.txtStatus.resources.getColor(R.color.voteUnselectedColor, null))

                    holder.layoutCenter.setBackgroundResource(R.color.bgvoteUnselectedColor)
                    holder.layoutBottom.setBackgroundResource(R.color.bgvoteUnselectedColor)

                    holder.txtTime.setBackgroundResource(R.color.backgroundGrayLightColor)
                    holder.txtTime.setTextColor(holder.txtTime.resources.getColor(R.color.black, null))
                }
            }

            holder.txtHopeStar.visibility = View.INVISIBLE
            holder.layoutHopeStar.visibility = View.INVISIBLE
            if(match.has("hope_star")) {
                val hope_star = match.getJSONObject("hope_star")
                val win = hope_star.getInt("win")
                val lost = hope_star.getInt("lost")
                val str = "x" + (win/1000).toInt() + " Điểm"
                holder.txtHopeStar.text = str

                holder.txtHopeStar.visibility = View.VISIBLE
                holder.layoutHopeStar.visibility = View.VISIBLE
            }
        }
    }
}