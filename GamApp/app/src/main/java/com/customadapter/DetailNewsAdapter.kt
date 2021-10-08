package com.customadapter

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.barservicegam.app.R
import com.bumptech.glide.Glide
import com.customview.BVPlayerVideo
import com.khaolok.myloadmoreitem.DetailView
import com.lib.Utils
import com.lib.Utils.underline
import com.lib.toPx
import data.dataHtml
import jp.wasabeef.glide.transformations.RoundedCornersTransformation


public class DetailNewsAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var positionVideoPlay: Int = -1
    lateinit  var rclView:RecyclerView
    lateinit  var mList:ArrayList<dataHtml>
    lateinit var bvPlayerVideo: BVPlayerVideo

    constructor(context: Context, mList: ArrayList<dataHtml>, rclView: RecyclerView): this(context) {
        this.mList = mList

//        positionVideoPlay = 1
        this.rclView = rclView
        this.bvPlayerVideo = BVPlayerVideo(this.context!!)

        rclView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

            }
        })
    }

    inner class DetailNewsTitle(v: View) : RecyclerView.ViewHolder(v){
        var txtTitle: TextView = itemView!!.findViewById(R.id.txtStar)

        init {
            val typeface = Typeface.createFromAsset(
                txtTitle.getContext().assets,
                "fonts/sfuidisplaysemibold.ttf"
            )
            txtTitle.setTypeface(typeface)
            txtTitle.textSize = 30f
            txtTitle.setTextColor(txtTitle.getResources().getColor(R.color.contentdetailnews, null))
        }
    }

    inner class DetailNewsCategory(v: View) : RecyclerView.ViewHolder(v) {
        var txtCategory: TextView = itemView!!.findViewById(R.id.txtCategory)
        var txtWebsite: TextView = itemView!!.findViewById(R.id.txtWebsite)
        var txtTime: TextView = itemView!!.findViewById(R.id.txtTime)

        init {
            val typeface = Typeface.createFromAsset(
                txtWebsite.getContext().assets,
                "fonts/sfuidisplaysemibold.ttf"
            )
            txtCategory.setTypeface(typeface)
            txtWebsite.setTypeface(typeface)
            txtTime.setTypeface(typeface)

            txtCategory.textSize = 14f
            txtWebsite.textSize = 14f
            txtTime.textSize = 14f

            txtCategory.setTextColor(txtCategory.getResources().getColor(R.color.white, null))
            txtWebsite.setTextColor(
                txtCategory.getResources().getColor(
                    R.color.com_facebook_blue,
                    null
                )
            )
            txtWebsite.alpha = 0.5f
            txtTime.setTextColor(txtTime.getResources().getColor(R.color.descnewscolor, null))
        }
    }

    inner class DetailNewsDesc(v: View) : RecyclerView.ViewHolder(v) {
        var txtDesc: TextView = itemView!!.findViewById(R.id.txtCategory)

        init {
            val typeface = Typeface.createFromAsset(
                txtDesc.getContext().assets,
                "fonts/sfuidisplaysemibold.ttf"
            )
            txtDesc.setTypeface(typeface)
            txtDesc.textSize = 20f
            txtDesc.setTextColor(txtDesc.getResources().getColor(R.color.contentdetailnews, null))
        }
    }

    inner class DetailNewsContent(v: View) : RecyclerView.ViewHolder(v) {
        var txtContent: TextView = itemView!!.findViewById(R.id.txtContent)

        init {
            val typeface = Typeface.createFromAsset(
                txtContent.getContext().assets,
                "fonts/sfuitextregular.ttf"
            )
            txtContent.setTypeface(typeface)
            txtContent.textSize = 20f
            txtContent.setTextColor(
                txtContent.getResources().getColor(
                    R.color.contentdetailnews,
                    null
                )
            )
        }
    }

    inner class DetailNewsImage(v: View) : RecyclerView.ViewHolder(v) {
        var imgCover: ImageView = itemView!!.findViewById(R.id.imgCover)
    }

    inner class DetailNewsWebview(v: View) : RecyclerView.ViewHolder(v) {
        var webNews: WebView = itemView!!.findViewById(R.id.webNews)
    }

    inner class DetailNewsVideo(v: View) : RecyclerView.ViewHolder(v) {
        var layoutVideo: ConstraintLayout = itemView!!.findViewById(R.id.layoutVideo)
        var imgCover: ImageView = itemView!!.findViewById(R.id.imgCover)
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
        } else if(viewType == DetailView.DETAILNEWS_HOLDER_VIDEO) {
            return DetailNewsVideo(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.detailnews_video_holder,
                    parent,
                    false
                )
            )
        }

        return DetailNewsContent(
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
        } else if(mList[position].type == "video") {
            return DetailView.DETAILNEWS_HOLDER_VIDEO
        }

        return DetailView.DETAILNEWS_HOLDER_CONTENT
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DetailNewsAdapter.DetailNewsTitle) {
            holder.txtTitle.text = mList[position].content
        } else if (holder is DetailNewsAdapter.DetailNewsCategory) {

            val arrInfo = mList[position].content.split(" • ").toTypedArray()
            if(arrInfo.count() > 0) {
                holder.txtCategory.text = arrInfo[0]
            }
            if(arrInfo.count() > 1) {
                holder.txtWebsite.text = arrInfo[1]
            }
            if(arrInfo.count() > 2) {
                holder.txtTime.text = " • " + arrInfo[2]
            }
        } else if (holder is DetailNewsAdapter.DetailNewsDesc) {
            holder.txtDesc.text = mList[position].content
        } else if (holder is DetailNewsAdapter.DetailNewsContent) {
            var lp: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            lp.setMargins(16.toPx(), 8.toPx(), 16.toPx(), 0)
            holder.txtContent.setPadding(0, 0, 0, 0)

//            holder.txtContent.setBackgroundResource(R.color.white)

            if(mList[position].bg) {
                lp.setMargins(16.toPx(), 0, 16.toPx(), 0)
                holder.txtContent.setPadding(8.toPx(), 8.toPx(), 8.toPx(), 8.toPx())

                holder.txtContent.setBackgroundResource(R.color.gray)
            }
            holder.txtContent.layoutParams = lp

            holder.txtContent.text = mList[position].content

            if(mList[position].type == "italic") {
                holder.txtContent.text = HtmlCompat.fromHtml(
                    mList[position].content,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                holder.txtContent.setTypeface(null, Typeface.ITALIC)
            } else if(mList[position].type == "underline") {
                holder.txtContent.text = HtmlCompat.fromHtml(
                    mList[position].content,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                holder.txtContent.underline()
            } else if(mList[position].type == "strong") {
                holder.txtContent.setTypeface(null, Typeface.BOLD)
            } else if(mList[position].type == "texthtml") {
                holder.txtContent.text = HtmlCompat.fromHtml(
                    mList[position].content,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
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

            ///neu image la the dau tien thi top = 0
            var lp: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            lp.setMargins(16.toPx(), 16.toPx(), 16.toPx(), 0)

            if(mList[position].bg) {
                lp.setMargins(16.toPx(), 0, 16.toPx(), 0)
            }
            holder.imgCover.layoutParams = lp

        } else if (holder is DetailNewsAdapter.DetailNewsWebview) {
            var content = Utils.embeddStyleLOADHMTL(mList[position].content)
            holder.webNews.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null)
        } else if (holder is DetailNewsAdapter.DetailNewsVideo) {
            var content = mList[position].content
            val radius = 0; // corner radius, higher value = more rounded
            val margin = 0; // crop margin, set to 0 for corners with no crop

            //content = "https://media.tinmoi24.vn/24h/upload/3-2021/videoclip/2021-07-23/1626976327-layvosinhtu.m3u8"
            //https://i1-thethao.vnecdn.net/2021/09/07/grant-jpeg-1631018928-4564-1631018954.jpg?w=680&h=0&q=100&dpr=1&fit=crop&s=v7oFroC3BqAGX6iov9xASA

            Glide.with(holder.imgCover.context)
                .load(content)
                .transform(RoundedCornersTransformation(radius, margin))
                .placeholder(R.drawable.thumbnews)
                .into(holder.imgCover)

            if(holder.layoutVideo.findViewWithTag<BVPlayerVideo>(100) != null) {
                val btnExtra = holder.layoutVideo.findViewWithTag<BVPlayerVideo>(100)
                if (btnExtra.parent != null) {
                    (btnExtra.parent as ViewGroup).removeView(btnExtra)
                }
            }

            if(positionVideoPlay == position) {
                bvPlayerVideo.tag = 100
                bvPlayerVideo.layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.MATCH_PARENT
                )
                holder.layoutVideo.addView(bvPlayerVideo)
            }

            holder.layoutVideo.setOnClickListener{
                if(positionVideoPlay != position) {
                    showVideoPosition(position)
                }
            }
        }
    }

    fun showVideoPosition(position: Int) {
        if (bvPlayerVideo.parent != null) {
            (bvPlayerVideo.parent as ViewGroup).removeView(bvPlayerVideo)
        }

        Log.d("vietnb", "position luc nay: $position")
        if(position == -1) {
            pauseVideo()
            return
        }

        positionVideoPlay = position

        //var urlVideo = "https://nld.mediacdn.vn/291774122806476800/2021/9/7/2742493976237-16310038843781108475104.mp4"
        var urlVideo = mList[position].content
//        val mVideo = this.mList.getJSONObject(position)
//        if(mVideo.has("listVideos")) {
//            val listVideos = mVideo.getJSONArray("listVideos")
//            if(listVideos.length() > 0) {
//                urlVideo = listVideos[0].toString()
//            }
//        }

        bvPlayerVideo.loadVideo(urlVideo)
        notifyItemChanged(position)
    }

    fun pauseVideo() {
        if(bvPlayerVideo != null) {
            bvPlayerVideo.pauseVideo()
        }
    }

    fun playVideo() {
        if(bvPlayerVideo != null) {
            bvPlayerVideo.playVideo()
        }
    }
}