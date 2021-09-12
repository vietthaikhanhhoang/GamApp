package com.customview.setting

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Switch
import androidx.constraintlayout.widget.ConstraintLayout
import com.barservicegam.app.R
import data.DataPreference
import data.PREFERENCE

class settingswitch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {
    lateinit var switchAutoPlay: Switch
    lateinit var switchBright: Switch

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.settingswitch, this)
        switchAutoPlay = view.findViewById(R.id.switchAutoPlay)
        switchBright = view.findViewById(R.id.switchBright)

        val sharedPreference: DataPreference = DataPreference(this.context)
        switchAutoPlay.isChecked = sharedPreference.getValueBoolean(PREFERENCE.AUTOPLAY, false)
        switchBright.isChecked = sharedPreference.getValueBoolean(PREFERENCE.BRIGHT, false)
        switchAutoPlay.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPreference.save(PREFERENCE.AUTOPLAY, isChecked)
        }

        switchBright.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPreference.save(PREFERENCE.BRIGHT, isChecked)
        }
    }
}