import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener


class RecyclerTouchEvent(
        context: Context?,
        recyclerView: RecyclerView?,
        var clickListener: ClickListener?
) :
    OnItemTouchListener {
    private var gestureDetector: GestureDetector

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        if(clickListener != null)
        {
            //Log.d("tag 55", "goi ham onInterceptTouchEvent 11 : " + MyApplication.itemClick)
        }

        if(clickListener != null)
        {
            val childView: View? = rv.findChildViewUnder(e.x, e.y)

            if(childView != null)
            {
                val position = (rv.getChildLayoutPosition(childView!!))
                if(position == 12)
                {
                    clickListener!!.onOutsideClick(e)
                }
            }
        }

        return false
    }

    override fun onTouchEvent(rv: RecyclerView, motionEvent: MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    fun RecyclerTouchEvent(context: Context?, recyclerView: RecyclerView?, clickListener: ClickListener?) {
        this.clickListener = clickListener
        gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return super.onSingleTapUp(e)
            }

            override fun onLongPress(e: MotionEvent) {
                super.onLongPress(e)
            }
        })
    }

    init {
        gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return super.onSingleTapUp(e)
            }

            override fun onLongPress(e: MotionEvent) {
                super.onLongPress(e)
            }
        })
    }
}

interface ClickListener {
    fun onClick(view: View?)
    fun onOutsideClick(event: MotionEvent?)
}