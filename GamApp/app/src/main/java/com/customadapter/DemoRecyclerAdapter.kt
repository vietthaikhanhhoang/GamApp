package com.customadapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.api.Global
import com.barservicegam.app.R
import com.customview.segmentpredict
import com.khaolok.myloadmoreitem.Constant
import com.lib.onLayout
import org.json.JSONArray
import org.json.JSONObject


public class DemoRecyclerAdapter(var mList: JSONArray, var enableLoadMore: Boolean = true) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var hashMapSegment = HashMap<Int, Boolean>()

    var hashMapPredict = HashMap<Int, JSONObject>()

    inner class DemoHolder(v: View) : RecyclerView.ViewHolder(v){
        var constraintLayoutP: ConstraintLayout = itemView!!.findViewById(R.id.constraintLayoutP)
        var btnAddSegment: Button = itemView!!.findViewById(R.id.btnAddSegment)
        var btnPredict: Button = itemView!!.findViewById(R.id.btnPredict)
        var layoutCustom: ConstraintLayout = itemView!!.findViewById(R.id.layoutCustom)
//        var sgPredict: segmentpredict = itemView!!.findViewById(R.id.sgPredict)
        var predictBallView: PredictBallView = itemView!!.findViewById(R.id.predictBallView)
    }

    interface DemoRecyclerAdapterListener {
        fun click_DemoRecyclerAdapterListener(position: Int)
    }

    private var listener: DemoRecyclerAdapterListener? = null

    fun setDemoRecyclerAdapterListener(listener: DemoRecyclerAdapterListener?) {
        this.listener = listener
    }

    inner public class LoadingHolder(v: View) : RecyclerView.ViewHolder(v)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == Constant.VIEW_TYPE_ITEM) {
            val view = DemoHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.demoholder,
                    parent,
                    false
                )
            )

            return view
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
        if(mList == null) return 0
        return mList!!.length()
    }

//    fun getItem(position: Int): String? {
//        return mList.get(position)
//    }

    override fun getItemViewType(position: Int): Int {
        return if (mList.length() - 1 == position && enableLoadMore) {
            Constant.VIEW_TYPE_LOADING
        } else {
            Constant.VIEW_TYPE_ITEM
        }
        return Constant.VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DemoHolder)
        {
            val art = JSONObject(mList[position].toString())

//            holder.predictBallView.segmentPredict.rateSegmentPredict(0, 0, 0)

            //holder.sgPredict.visibility = View.INVISIBLE
//            if(hashMapSegment.size > 0) {
//                if(hashMapSegment.containsKey(position)) { //da click va can add moi
//
//                    val value = hashMapSegment.get(position)!!
//                    if(value) {
//                        holder.sgPredict.visibility = View.VISIBLE
//                    }
//                }
//            }

//            if(holder.sgPredict != null && hashMapPredict.size > 0) {
//                if(hashMapPredict.containsKey(position)) {
//                    val value = hashMapPredict.get(position)!!
//                    val win = value.getInt("win")
//                    val draw = value.getInt("draw")
//                    val lose = value.getInt("lose")
//
//                    holder.sgPredict.rateSegmentPredict(win, draw, lose)
//                }
//            }

            var title = ""
            if(art.has("title")) {
                title = art["title"].toString()
            }

            if(art.has("cover")) {
                val cover = art["cover"].toString()

//                val radius = 20; // corner radius, higher value = more rounded
//                val margin = 0; // crop margin, set to 0 for corners with no crop
//                Glide.with(holder.imgCover.context)
//                    .load(cover)
//                    .transform(RoundedCornersTransformation(radius, margin))
//                    .placeholder(R.drawable.thumbnews)
//                    .into(holder.imgCover)

            }

            var nameWebsite = ""
//            if(art.has("sid")) {
//                val sid = art.getInt("sid")
//                nameWebsite = Global.getNameWebsite(sid)
//            }

            if(art.has("posttime")) {
                val posttime:Long = art["posttime"] as Long
                nameWebsite = nameWebsite + " | " + Global.currentTimeSecUTC(posttime)
            }

            if(art.has("sizeCmt")) {
                val sizeCmt = art["sizeCmt"]
                nameWebsite = nameWebsite + " | " + sizeCmt + " Bình luận"
            }

            //holder.txtDesc.text = nameWebsite

            holder.btnAddSegment.text = title
            holder.btnAddSegment.setOnClickListener{
                if(hashMapSegment.containsKey(position)) {
                    var value = hashMapSegment[position]!!
                    hashMapSegment[position] = !value
                }
                else { //chua click bao gio, set cho la da click = true
                    hashMapSegment[position] = true
                }

                val jObject = mList.getJSONObject(position)
                jObject.put("title", "xong mie roi")
                notifyItemChanged(position)

                if(listener != null) {
                    this.listener!!.click_DemoRecyclerAdapterListener(position)
                }
            }

            holder.btnPredict.setOnClickListener{
                Log.d("vietnb", "click vao predict")

                val jObject = JSONObject()
                jObject.put("win", "0")
                jObject.put("draw", "0")
                jObject.put("lose", "50")
                hashMapPredict[position] = jObject
                notifyItemChanged(position)
            }
        }
    }

    fun removeLoadingView() {
        //Remove loading item
        if (mList.length() != 0) {
            var last = mList.length() - 1
            mList.remove(last)
            notifyItemRemoved(last)
        }
    }

    fun addData(array: JSONArray) {
        for (i in 0 until array.length()) {
            this.mList.put(array[i])
        }

        notifyDataSetChanged()
    }

    fun addLoadingView() {
        //add loading item
        mList.put(null)
        notifyItemInserted(mList.length() - 1)
    }
}