package com.customview.setting

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barservicegam.app.R
import com.customadapter.ListNewsAdapter
import com.customadapter.setting.settingtextAdapter
import com.fragmentcustom.*
import com.fragmentcustom.autoplayvideo.AutoplayVideoFragment
import com.fragmentcustom.football.GuessFragment
import com.fragmentcustom.football.dddbtk.MatchDetailFragment
import com.fragula.extensions.addFragment
import com.lib.eventbus.EventBusFire
import data.DataPreference
import data.PREFERENCE
import okhttp3.internal.notifyAll
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream


class settingtext @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {
    lateinit var rclView: RecyclerView
    var mList = JSONArray()
    var settingTextAdapter = settingtextAdapter(mList)
    init {
        init(attrs)
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    fun onMessageEvent(event: EventBusFire) { /* Do something */
        if(event.eventName == "logoutFacebook") {
            for (i in 0 until mList.length()) {
                val jObject = mList.getJSONObject(i)
                if(jObject.getString("text") == "Đăng xuất") {
                    mList.remove(i)
                    settingTextAdapter.mList = mList
                    Handler(Looper.getMainLooper()).post {
                        rclView.adapter?.notifyItemRemoved(i)

//                        settingTextAdapter.notifyItemRemoved(i)
//                        rclView.adapter = settingTextAdapter
                    }
                    break
                }
            }
        } else if(event.eventName == "loginSuccess") {
            val logout = JSONObject()
            logout.put("icon", "logout")
            logout.put("text", "Đăng xuất")
            mList.put(logout)
            settingTextAdapter.mList = mList
            Handler(Looper.getMainLooper()).post {
                settingTextAdapter.mList = mList
                rclView.adapter?.notifyItemInserted(mList.length() - 1)

//                settingTextAdapter.notifyItemInserted(mList.length() - 1)
//                rclView.adapter = settingTextAdapter
            }


        }
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.settingtext, this)

        rclView = view.findViewById(R.id.rclView)
        EventBus.getDefault().register(this)

        val jObjectTerm = JSONObject()
        jObjectTerm.put("icon", "icon_setting_term")
        jObjectTerm.put("text", "Điều khoản sử dụng")
        mList.put(jObjectTerm)

        val jObjectAutoPlay = JSONObject()
        jObjectAutoPlay.put("icon", "icon_setting_term")
        jObjectAutoPlay.put("text", "Cài đặt autoPlay")
        mList.put(jObjectAutoPlay)

        val sharedPreference: DataPreference = DataPreference(context)
        val accountUser = sharedPreference.getValueJSON(PREFERENCE.ACCOUNTUSER)
        if(accountUser is JSONObject) {
            val logout = JSONObject()
            logout.put("icon", "logout")
            logout.put("text", "Đăng xuất")
            mList.put(logout)
        }

        rclView.adapter = settingtextAdapter(mList)
        rclView.layoutManager = LinearLayoutManager(this.context)
    }
}