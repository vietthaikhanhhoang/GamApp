package com.customview.ball

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.barservicegam.app.R
import com.customadapter.ball.ListGuessMatchAdapter
import com.fragmentcustom.football.GuessFragment
import com.fragmentcustom.football.dddbtk.MatchDetailFragment
import com.fragula.extensions.addFragment
import org.json.JSONArray

class listguessmatch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {
    lateinit var rclView: RecyclerView
    var mList = JSONArray()
    var listGuessMatchAdapter = ListGuessMatchAdapter(mList)
    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.guessmatchview, this)
        rclView = view.findViewById(R.id.rclView)
    }

    fun refreshData(mList: JSONArray){
        this.mList = mList
        listGuessMatchAdapter.mList = this.mList
        rclView.adapter = listGuessMatchAdapter
        rclView.layoutManager = LinearLayoutManager(this.context)

        listGuessMatchAdapter.setListGuessMatchAdapter(object : ListGuessMatchAdapter.ListGuessMatchAdapterListener{
            override fun click_ListGuessMatchAdapter(position: Int) {

                val fragment = findFragment<GuessFragment>()
                fragment.addFragment<MatchDetailFragment> {
                    "param1" to mList[position].toString()
                    "param2" to ""
                }
            }
        })
    }
}