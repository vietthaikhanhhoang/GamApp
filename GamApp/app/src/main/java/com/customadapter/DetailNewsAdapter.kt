package com.customadapter

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.barservicegam.app.R
import com.fragmentcustom.HomePagerFragment
import com.fragmentcustom.RelativeFragment
import com.khaolok.myloadmoreitem.DetailView
import com.lib.Utils
import com.lib.Utils.underline
import data.dataHtml
import org.json.JSONArray

public class DetailNewsAdapter(var mList: ArrayList<dataHtml>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class DetailNewsTitle(v: View) : RecyclerView.ViewHolder(v){
        var txtTitle: TextView = itemView!!.findViewById(R.id.txtTitle)

        fun constructor() {
//            Log.d("vietnb", "dm no co vao khong nhi 111")
//            val typeface = Typeface.createFromAsset(txtTitle.getContext().assets, "fonts/sfuitextitalic.ttf")
//            txtTitle.setTypeface(typeface)
        }
    }

    inner class DetailNewsCategory(v: View) : RecyclerView.ViewHolder(v) {
        var txtCategory: TextView = itemView!!.findViewById(R.id.txtCategory)
        var txtWebsite: TextView = itemView!!.findViewById(R.id.txtWebsite)

        fun constructor() {
            val typeface = Typeface.createFromAsset(txtCategory.getContext().assets, "fonts/sfuitextitalic.ttf")
            txtCategory.setTypeface(typeface)
            txtWebsite.setTypeface(typeface)
        }
    }

    inner class DetailNewsDesc(v: View) : RecyclerView.ViewHolder(v) {
        var txtDesc: TextView = itemView!!.findViewById(R.id.txtDesc)

        fun constructor() {
            val typeface = Typeface.createFromAsset(txtDesc.getContext().assets, "fonts/sfuitextitalic.ttf")
            txtDesc.setTypeface(typeface)
        }
    }

    inner class DetailNewsContent(v: View) : RecyclerView.ViewHolder(v) {
        var txtContent: TextView = itemView!!.findViewById(R.id.txtContent)

        fun constructor() {
            val typeface = Typeface.createFromAsset(txtContent.getContext().assets, "fonts/sfuitextitalic.ttf")
            txtContent.setTypeface(typeface)
        }
    }

    inner class DetailNewsImage(v: View) : RecyclerView.ViewHolder(v) {
        var imgCover: ImageView = itemView!!.findViewById(R.id.imgCover)
    }

    inner class DetailNewsWebview(v: View) : RecyclerView.ViewHolder(v) {
        var webNews: WebView = itemView!!.findViewById(R.id.webNews)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType == DetailView.DETAILNEWS_HOLDER_TITLE) {
            return DetailNewsTitle(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.detailnews_title_holder,
                    parent,
                    false
                )
            )
        } else if(viewType == DetailView.DETAILNEWS_HOLDER_CATEGORY) {
            return DetailNewsCategory(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.detailnews_category_holder,
                    parent,
                    false
                )
            )
        } else if(viewType == DetailView.DETAILNEWS_HOLDER_DESC) {
            return DetailNewsDesc(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.detailnews_desc_holder,
                    parent,
                    false
                )
            )
        } else if(viewType == DetailView.DETAILNEWS_HOLDER_CONTENT) {
            return DetailNewsContent(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.detailnews_content_holder,
                    parent,
                    false
                )
            )
        } else if(viewType == DetailView.DETAILNEWS_HOLDER_IMAGE) {
            return DetailNewsImage(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.detailnews_image_holder,
                    parent,
                    false
                )
            )
        } else if(viewType == DetailView.DETAILNEWS_HOLDER_WEBVIEW) {
            return DetailNewsWebview(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.detailnews_webview_holder,
                    parent,
                    false
                )
            )
        }

        return DetailNewsContent (
            LayoutInflater.from(parent.context).inflate(
                R.layout.detailnews_content_holder,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {
        if(mList[position].type == "title") {
            return DetailView.DETAILNEWS_HOLDER_TITLE
        } else if(mList[position].type == "category") {
            return DetailView.DETAILNEWS_HOLDER_CATEGORY
        } else if(mList[position].type == "desc") {
            return DetailView.DETAILNEWS_HOLDER_DESC
        } else if(mList[position].type == "content" || mList[position].type == "italic" || mList[position].type == "b"
            || mList[position].type == "strong" || mList[position].type == "i" || mList[position].type == "underline") {
            return DetailView.DETAILNEWS_HOLDER_CONTENT
        } else if(mList[position].type == "img") {
            return DetailView.DETAILNEWS_HOLDER_IMAGE
        } else if(mList[position].type == "webview") {
            return DetailView.DETAILNEWS_HOLDER_WEBVIEW
        }

        return DetailView.DETAILNEWS_HOLDER_CONTENT
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DetailNewsAdapter.DetailNewsTitle) {
            holder.txtTitle.text = mList[position].content
        } else if (holder is DetailNewsAdapter.DetailNewsCategory) {

            val arrInfo = mList[position].content.split("|").toTypedArray()
            if(arrInfo.count() > 2) {
                holder.txtCategory.text = arrInfo[0]
                holder.txtWebsite.text = arrInfo[1] + " | " + arrInfo[2]
            }
        } else if (holder is DetailNewsAdapter.DetailNewsDesc) {
            holder.txtDesc.text = mList[position].content
        } else if (holder is DetailNewsAdapter.DetailNewsContent) {
            if(mList[position].bg) {
                holder.txtContent.setBackgroundResource(R.color.gray)
                val layoutParam = holder.txtContent.layoutParams
                holder.txtContent.setPadding(8, 8 , 8 , 8)
            }
            holder.txtContent.text = mList[position].content

            if(mList[position].type == "italic") {
                holder.txtContent.text = HtmlCompat.fromHtml(mList[position].content, HtmlCompat.FROM_HTML_MODE_LEGACY)
                holder.txtContent.setTypeface(null, Typeface.ITALIC)
            } else if(mList[position].type == "underline") {
                holder.txtContent.text = HtmlCompat.fromHtml(mList[position].content, HtmlCompat.FROM_HTML_MODE_LEGACY)
                holder.txtContent.underline()
            } else if(mList[position].type == "strong") {
                holder.txtContent.setTypeface(null, Typeface.BOLD)
            } else if(mList[position].type == "texthtml") {
                holder.txtContent.text = HtmlCompat.fromHtml(mList[position].content, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
        } else if (holder is DetailNewsAdapter.DetailNewsImage) {
            val cover = mList[position].content

            val radius = 0; // corner radius, higher value = more rounded
            val margin = 0; // crop margin, set to 0 for corners with no crop
//            Glide.with(holder.imgCover.context)
//                .load(cover)
//                .transform(RoundedCornersTransformation(radius, margin))
//                .placeholder(R.drawable.thumbnews)
//                .into(holder.imgCover)

            Utils.bindImageBitMap(holder.imgCover, holder.imgCover.context, cover)
        } else if (holder is DetailNewsAdapter.DetailNewsWebview) {
            var content = Utils.embeddStyleLOADHMTL(mList[position].content)
            holder.webNews.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null)
        }
    }
}