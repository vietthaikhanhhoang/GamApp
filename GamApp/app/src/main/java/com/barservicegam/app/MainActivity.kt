package com.barservicegam.app

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.opengl.Visibility
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.fragmentcustom.*
import com.fragula.Navigator
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lib.*
import com.lib.eventbus.EventBusFire
import data.DataPreference
import data.PREFERENCE
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var callbackManager: CallbackManager

    lateinit var navigatorTabNews: Navigator
    lateinit var navigatorTabVideos: Navigator
    lateinit var navigatorTabExplore: Navigator
    lateinit var navigatorTabSetting: Navigator

    lateinit var layoutParent: ConstraintLayout
    lateinit var bottomView: BottomNavigationView

    var homePagerFragment = HomePagerFragment.newInstance("", "")//HomePagerFragment.newInstance("", "")
    var listVideoFragment = VideoPagerFragment.newInstance("", "")
    var exploreFragment = AutoSearchFragment.newInstance("", "")
    var settingFragment = YoutubeFragment.newInstance("", "")

    fun actionBack() {
        if(bottomView.selectedItemId == R.id.navigation_news) {
            if (navigatorTabNews.fragmentCount > 1) {
                navigatorTabNews.goToPreviousFragmentAndRemoveLast()
            } else {
                super.onBackPressed()
            }
        }
        else if(bottomView.selectedItemId == R.id.navigation_video) {
            if (navigatorTabVideos.fragmentCount > 1) {
                navigatorTabVideos.goToPreviousFragmentAndRemoveLast()
            } else {
                super.onBackPressed()
            }
        }
        else if(bottomView.selectedItemId == R.id.navigation_explore) {
            if (navigatorTabExplore.fragmentCount > 1) {
                navigatorTabExplore.goToPreviousFragmentAndRemoveLast()
            } else {
                super.onBackPressed()
            }
        } else if(bottomView.selectedItemId == R.id.navigation_setting) {
            if (navigatorTabSetting.fragmentCount > 1) {
                navigatorTabSetting.goToPreviousFragmentAndRemoveLast()
            } else {
                super.onBackPressed()
            }
        }

    }

    override fun onBackPressed() {
        if (navigatorTabNews.fragmentCount > 1) {
            navigatorTabNews.goToPreviousFragmentAndRemoveLast()
        } else {
            super.onBackPressed()
        }
    }

    fun getVideoThumbnail(videoPath: String?, width: Int, height: Int): Bitmap? {
        var bitmap: Bitmap? = null
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath!!, MediaStore.Video.Thumbnails.MICRO_KIND)
        if (bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(
                bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT
            )
        }
        return bitmap
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        OrientationUtils.lockOrientationPortrait(this)
        setContentView(R.layout.activity_main2)
        Utils.hiddenBottomBar(this)
//        WindowCompat.setDecorFitsSystemWindows(window, true)

        var str = "https://suckhoedoisong.qltns.mediacdn.vn/324455921873985536/2021/9/10/bt-long-16312553669101597457462.mp4"
//        str = "https://media.tinmoi24.vn/24h/upload/3-2021/videoclip/2021-07-23/1626976327-layvosinhtu.m3u8"
        val image = findViewById<ImageView>(R.id.imgThumbVideo)
        image.visibility = View.INVISIBLE

        val bm = getVideoThumbnail(str, 100, 100)//Utils.getThumbVideoMp4(str)
        image.setImageBitmap(bm)

        FacebookSdk.fullyInitialize()
        callbackManager = CallbackManager.Factory.create()

        layoutParent = findViewById(R.id.layoutParent)
        navigatorTabNews = findViewById(R.id.navigatorTabNews)
        navigatorTabVideos = findViewById(R.id.navigatorTabVideos)
        navigatorTabExplore = findViewById(R.id.navigatorTabExplore)
        navigatorTabSetting = findViewById(R.id.navigatorTabSetting)

        bottomView = findViewById(R.id.bottomView)
        layoutParent.setOnClickListener{
            this.hideSoftKeyboard()
        }

        if (savedInstanceState == null) {
            //navigatorTab.addFragment(BlankFragment())
            //navigatorTab.addFragment(ListVideoFragment.newInstance("", ""))
            //navigatorTab.addFragment(NestedGridViewFragment.newInstance("", ""))
//            navigatorTab.addFragment(GridViewFragment.newInstance("", ""))
//            navigatorTab.addFragment(AutoSearchFragment.newInstance("", ""))
//            navigatorTab.addFragment(ListWebsiteFragment.newInstance("", ""))

            navigatorTabNews.addFragment(homePagerFragment)
            navigatorTabVideos.addFragment(listVideoFragment)
            navigatorTabExplore.addFragment(exploreFragment)
            navigatorTabSetting.addFragment(settingFragment)
        } else {
            savedInstanceState.getInt(SELECTED_ITEM_KEY)?.let {
                selectTab(it)
            }
        }

        setBottomBarListener()
    }

    fun fullScreen(isFullScreen: Boolean) {
        if(isFullScreen) {
            Utils.hiddenStatusBarAndBottomBar(this)
            bottomView.hide()
        } else {
            bottomView.show()
            Utils.hiddenBottomBar(this)
        }
    }

    private fun setBottomBarListener() {
        bottomView.setOnNavigationItemSelectedListener {

            EventBus.getDefault().post(EventBusFire("pauseVideo", valueString = ""))

            return@setOnNavigationItemSelectedListener selectTab(it.itemId)
        }
    }

    private fun selectTab(tabId: Int) = when (tabId) {
        R.id.navigation_news -> {
            navigatorTabNews.visibility = View.VISIBLE
            navigatorTabVideos.visibility = View.INVISIBLE
            navigatorTabExplore.visibility = View.INVISIBLE
            navigatorTabSetting.visibility = View.INVISIBLE
            true
        }
        R.id.navigation_video -> {
            navigatorTabNews.visibility = View.INVISIBLE
            navigatorTabVideos.visibility = View.VISIBLE
            navigatorTabExplore.visibility = View.INVISIBLE
            navigatorTabSetting.visibility = View.INVISIBLE
            true
        }
        R.id.navigation_explore -> {
            navigatorTabNews.visibility = View.INVISIBLE
            navigatorTabVideos.visibility = View.INVISIBLE
            navigatorTabExplore.visibility = View.VISIBLE
            navigatorTabSetting.visibility = View.INVISIBLE
            true
        }
        R.id.navigation_setting -> {
            navigatorTabNews.visibility = View.INVISIBLE
            navigatorTabVideos.visibility = View.INVISIBLE
            navigatorTabExplore.visibility = View.INVISIBLE
            navigatorTabSetting.visibility = View.VISIBLE
            true
        }
        else -> false
    }

    companion object {
        const val SELECTED_ITEM_KEY = "SELECTED_ITEM_KEY"
    }

    ///login facebook

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("letsSee", "malsehnnnnnn: " + data)
    }

    fun loginFacebook()
    {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile", "email"))
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult?> {
            override fun onSuccess(result: LoginResult?) {
                if (result != null) {
                    Log.d("letsSee", "Facebook token: " + result!!.accessToken.token)
                    getUserProfile(result?.accessToken, result?.accessToken?.userId)
                }
            }

            override fun onCancel() {
                Toast.makeText(this@MainActivity, "Login Cancelled", Toast.LENGTH_LONG).show()
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(this@MainActivity, error.toString(), Toast.LENGTH_LONG).show()
            }

        })
    }

    @SuppressLint("LongLogTag")
    fun getUserProfile(token: AccessToken?, userId: String?) {

        val parameters = Bundle()
        parameters.putString(
            "fields",
            "id, first_name, middle_name, last_name, name, picture, email"
        )
        GraphRequest(token,
            "/$userId/",
            parameters,
            HttpMethod.GET,
            GraphRequest.Callback { response ->
                val jsonObject = response.jsonObject

                // Facebook Access Token
                // You can see Access Token only in Debug mode.
                // You can't see it in Logcat using Log.d, Facebook did that to avoid leaking user's access token.
                if (BuildConfig.DEBUG) {
                    FacebookSdk.setIsDebugEnabled(true)
                    FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS)
                }

                // Facebook Id
                if (jsonObject.has("id")) {
                    val facebookId = jsonObject.getString("id")
                    Log.i("Facebook Id: ", facebookId.toString())
                } else {
                    Log.i("Facebook Id: ", "Not exists")
                }


                // Facebook First Name
                if (jsonObject.has("first_name")) {
                    val facebookFirstName = jsonObject.getString("first_name")
                    Log.i("Facebook First Name: ", facebookFirstName)
                } else {
                    Log.i("Facebook First Name: ", "Not exists")
                }


                // Facebook Middle Name
                if (jsonObject.has("middle_name")) {
                    val facebookMiddleName = jsonObject.getString("middle_name")
                    Log.i("Facebook Middle Name: ", facebookMiddleName)
                } else {
                    Log.i("Facebook Middle Name: ", "Not exists")
                }


                // Facebook Last Name
                if (jsonObject.has("last_name")) {
                    val facebookLastName = jsonObject.getString("last_name")
                    Log.i("Facebook Last Name: ", facebookLastName)
                } else {
                    Log.i("Facebook Last Name: ", "Not exists")
                }


                // Facebook Name
                var name = ""
                if (jsonObject.has("name")) {
                    name = jsonObject.getString("name")
                }


                // Facebook Profile Pic URL
                var avatar = ""
                if (jsonObject.has("picture")) {
                    val facebookPictureObject = jsonObject.getJSONObject("picture")
                    if (facebookPictureObject.has("data")) {
                        val facebookDataObject = facebookPictureObject.getJSONObject("data")
                        if (facebookDataObject.has("url")) {
                            avatar = facebookDataObject.getString("url")
                        }
                    }
                } else {
                    Log.i("Facebook Profile Pic URL: ", "Not exists")
                }

                // Facebook Email
                if (jsonObject.has("email")) {
                    val facebookEmail = jsonObject.getString("email")
                    Log.i("Facebook Email: ", facebookEmail)
                } else {
                    Log.i("Facebook Email: ", "Not exists")
                }

                val sharedPreference: DataPreference = DataPreference(this)
                var jObject = JSONObject()
                jObject.put("name", name)
                jObject.put("avatar", avatar)
                sharedPreference.save(PREFERENCE.ACCOUNTUSER, jObject.toString())
                EventBus.getDefault().post(EventBusFire("loginSuccess", valueString = ""))
                //settingFragment.refreshDataAccountUser()

            }).executeAsync()
    }

    fun logoutFB()
    {
        val sharedPreference:DataPreference = DataPreference(this)
        sharedPreference.save(PREFERENCE.ACCOUNTUSER, "")
        //settingFragment.refreshDataAccountUser()
        LoginManager.getInstance().logOut()
    }
}