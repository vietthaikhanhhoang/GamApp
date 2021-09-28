package com.customview.setting

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barservicegam.app.R
import com.customadapter.setting.settingtextAdapter
import com.lib.eventbus.EventBusFire
import data.DataPreference
import data.PREFERENCE
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONArray
import org.json.JSONObject

class settinghistory @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {
    lateinit var txtSystem: TextView
    lateinit var txtMark:TextView
    lateinit var txtReaded:TextView
    lateinit var txtComment:TextView

    lateinit var layoutSystem: LinearLayout
    lateinit var layoutMark:LinearLayout
    lateinit var layoutReaded:LinearLayout
    lateinit var layoutComment:LinearLayout

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.settinghistory, this)
        txtSystem = view.findViewById(R.id.txtSystem)
        txtMark = view.findViewById(R.id.txtMark)
        txtReaded = view.findViewById(R.id.txtReaded)
        txtComment = view.findViewById(R.id.txtComment)

        layoutSystem = view.findViewById(R.id.layoutSystem)
        layoutMark = view.findViewById(R.id.layoutMark)
        layoutReaded = view.findViewById(R.id.layoutReaded)
        layoutComment = view.findViewById(R.id.layoutComment)
    }
}