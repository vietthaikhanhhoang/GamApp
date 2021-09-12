package com.lib

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.Surface
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.barservicegam.app.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.util.HashMap
import kotlin.math.roundToInt


fun Int.toDp():Int = (this/Resources.getSystem().displayMetrics.density).toInt()
fun Int.toPx():Int = (this*Resources.getSystem().displayMetrics.density).toInt()

// extension property to get display metrics instance
val Activity.displayMetrics: DisplayMetrics
    get() {
        // display metrics is a structure describing general information
        // about a display, such as its size, density, and font scaling
        val displayMetrics = DisplayMetrics()

        if (Build.VERSION.SDK_INT >= 30){
            display?.apply {
                getRealMetrics(displayMetrics)
            }
        }else{
            // getMetrics() method was deprecated in api level 30
            windowManager.defaultDisplay.getMetrics(displayMetrics)
        }

        return displayMetrics
    }


// extension property to get screen width and height in dp
val Activity.screenSizeInDp: Point
    get() {
        val point = Point()
        displayMetrics.apply {
            // screen width in dp
            point.x = (widthPixels / density).roundToInt()

            // screen height in dp
            point.y = (heightPixels / density).roundToInt()
        }

        return point
    }

fun Activity.hideSoftKeyboard(){
    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
        hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}

fun Fragment.hideSoftKeyboard(){
    val imm = androidx.core.content.ContextCompat.getSystemService(
        view!!.context,
        android.view.inputmethod.InputMethodManager::class.java
    )
    imm?.hideSoftInputFromWindow(view!!.windowToken, 0)
}

object Utils {

    fun getThumbVideoMp4(videoPath: String?): Bitmap? {
        var bitmap: Bitmap? = null
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(videoPath, HashMap<String, String>())
            bitmap = mediaMetadataRetriever.frameAtTime
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaMetadataRetriever?.release()
        }
        return bitmap
    }

    fun getActivity(context: Context): Activity? {
        var context = context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }

    fun TextView.underline() {
        paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    fun showToast(text: String, context: Context){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }

    fun hiddenStatusBarAndBottomBar(context: Activity){ //phuc vu cho mode fullscreen khi play video
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            val v: View = context.getWindow().getDecorView()
            v.systemUiVisibility = View.GONE
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            val decorView: View = context.getWindow().getDecorView()
            val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN
            decorView.systemUiVisibility = uiOptions
        }
    }

    fun hiddenBottomBar(context: Activity) {
        val decorView = context.window.decorView
        val uiOptions = (
                         View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        decorView.systemUiVisibility = uiOptions
    }

    fun hiddenBottomBarAndStatusWhite(context: Activity){
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            val v: View = context.getWindow().getDecorView()
            v.systemUiVisibility = View.GONE
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            val decorView: View = context.getWindow().getDecorView()
            val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            decorView.systemUiVisibility = uiOptions
        }

        if (Build.VERSION.SDK_INT >= 21) {
            context.getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.white));// set status background white
        }
    }

    fun hiddenBottomBarAndStatusBlack(context: Activity){
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            val v: View = context.getWindow().getDecorView()
            v.systemUiVisibility = View.GONE
        } else if (Build.VERSION.SDK_INT >= 19) {
            context.window.decorView.systemUiVisibility = context.window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() //set status text  light
        }

        if (Build.VERSION.SDK_INT >= 21) {
            context.getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.black));// set status background white
        }
    }

    fun getHeightAppBar(context: Context): Int { //tinh theo dp
        val tv = TypedValue()
        if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics).toDp()
        }
        return 0
    }

    fun getStatusBarHeight(context: Context): Int {
        var statusBarHeight = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = context.resources.getDimensionPixelSize(resourceId).toDp()
        }
        return statusBarHeight
    }

    fun dpToPx(dp: Int, context: Context): Int {
        val density = context.resources
            .displayMetrics.density
        return Math.round(dp.toFloat() * density)
    }

    fun pxToDp(dp: Int, context: Context): Int {
        val density = context.resources
            .displayMetrics.density
        return Math.round(dp.toFloat() / density)
    }

    fun getScreenWidth(): Float { // tinh theo dp
        return Resources.getSystem().getDisplayMetrics().widthPixels/ Resources.getSystem().getDisplayMetrics().density
    }

    fun getScreenHeight(): Float { // tinh theo dp
        return Resources.getSystem().getDisplayMetrics().heightPixels/ Resources.getSystem().getDisplayMetrics().density
    }

    fun getHeightBottomBar(context: Context) : Int{
        val resourceId = context.resources.getIdentifier(
            "design_bottom_navigation_height",
            "dimen",
            context.packageName
        )
        var height = 0
        if (resourceId > 0) {
            height = context.resources.getDimensionPixelSize(resourceId).toDp()
        }
        else {
            return 0
        }

        return height
    }

    fun bindImageBitMap(imgView: ImageView, context: Context, urlImage: String){
        Glide.with(context)
            .load(urlImage)
            .into(object : CustomTarget<Drawable?>() {
                override fun onResourceReady(
                    resource: Drawable,
                    @Nullable transition: Transition<in Drawable?>?
                ) {
                    val bitmap = (resource as BitmapDrawable).bitmap
//                    Log.d("vietnb", "chieu rong: " + bitmap.width)
////                    Log.d("vietnb", "chieu cao: " + bitmap.height)
//                    Log.d("vietnb", "load anh lien tuc ong a")

                    imgView.setImageBitmap(bitmap)
                    val width = Utils.getScreenWidth() - 32 //theo dp
                    val height: Float =
                        (width * bitmap.height) / bitmap.width //kich thuoc that cua anh

                    val param = imgView.layoutParams
                    param.height = height.toInt().toPx() //gan len px phai day lai
                    imgView.layoutParams = param
                }

                override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
                override fun onLoadFailed(@Nullable errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                }
            })
    }

    fun embeddStyleLOADHMTL(content: String) : String {
        var style = "<style type=\"text/css\">"
        style += "@font-face {font-family:MyFont;src:url(\"file:///android_asset/fonts/sfuitextitalic.ttf\")}"
        style += "p,body {font-family:MyFont;font-size:20px;text-align:left;line-height:30px}</style>"
        var html = "<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">" + style + "</head><body bgcolor=\"white\">" + content
        html+= "</body></html>"

        return html
    }

    fun setImageName(name:String, context: Context, image: ImageView) {
        val PACKAGE_NAME = context.packageName
        val imgId = context.resources.getIdentifier("$PACKAGE_NAME:drawable/$name", null, null)
        Log.d("vietnb", "IMG ID :: $imgId")
        Log.d("vietnb", "PACKAGE_NAME :: $PACKAGE_NAME")
        image.setImageBitmap(BitmapFactory.decodeResource(context.resources, imgId))
    }
}

object OrientationUtils {
    /** Locks the device window in landscape mode.  */
    fun lockOrientationLandscape(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    }

    /** Locks the device window in portrait mode.  */
    fun lockOrientationPortrait(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /** Locks the device window in actual screen mode.  */
    fun lockOrientation(activity: Activity) {
        val orientation = activity.resources.configuration.orientation
        val rotation =
            (activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
                .rotation

        // Copied from Android docs, since we don't have these values in Froyo
        // 2.2
        var SCREEN_ORIENTATION_REVERSE_LANDSCAPE = 8
        var SCREEN_ORIENTATION_REVERSE_PORTRAIT = 9

        // Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            SCREEN_ORIENTATION_REVERSE_LANDSCAPE = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            SCREEN_ORIENTATION_REVERSE_PORTRAIT = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
        } else if (rotation == Surface.ROTATION_180 || rotation == Surface.ROTATION_270) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                activity.requestedOrientation = SCREEN_ORIENTATION_REVERSE_PORTRAIT
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                activity.requestedOrientation = SCREEN_ORIENTATION_REVERSE_LANDSCAPE
            }
        }
    }

    /** Unlocks the device window in user defined screen mode.  */
    fun unlockOrientation(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
    }
}

