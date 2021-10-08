package com.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.barservicegam.app.R
import com.bumptech.glide.Glide
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import org.json.JSONObject

class ChildView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {
    lateinit var txtTitle: TextView
    lateinit var imgCover: ImageView

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.child_holder, this)
        txtTitle = view.findViewById(R.id.txtStar)
        imgCover = view.findViewById(R.id.imgCover)
    }

    fun bindData(data: JSONObject) {
        if(data.has("name")) {
            txtTitle.text = data.getString("name")
        }

        if(data.has("cover")) {
            val cover = data.getString("cover")

            val radius = 0; // corner radius, higher value = more rounded
            val margin = 0; // crop margin, set to 0 for corners with no crop
            Glide.with(imgCover.context)
                .load(cover)
                .transform(RoundedCornersTransformation(radius, margin))
                .placeholder(R.drawable.thumbnews)
                .into(imgCover)

        }
    }
}