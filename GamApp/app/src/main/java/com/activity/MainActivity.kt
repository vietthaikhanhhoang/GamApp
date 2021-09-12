package com.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.barservicegam.app.BuildConfig
import com.barservicegam.app.R
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.fragmentcustom.*
import com.fragula.Navigator
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.youtube.player.YouTubePlayerFragment
import com.lib.*
import com.lib.eventbus.EventBusFire
import org.greenrobot.eventbus.EventBus


class MainActivity : AppCompatActivity() {
    lateinit var callbackManager: CallbackManager

    lateinit var navigatorTabNews: Navigator
    lateinit var navigatorTabVideos: Navigator
    lateinit var navigatorTabExplore: Navigator
    lateinit var navigatorTabSetting: Navigator

    lateinit var layoutParent: ConstraintLayout
    lateinit var bottomView: BottomNavigationView

    var homePagerFragment = HomePagerFragment.newInstance("", "")
    var listVideoFragment = VideoPagerFragment.newInstance("", "")
    var exploreFragment = AutoSearchFragment.newInstance("", "")
    var settingFragment = SettingFragment.newInstance("", "")

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        OrientationUtils.lockOrientationPortrait(this)
        setContentView(R.layout.activity_main2)
        Utils.hiddenBottomBar(this)
//        WindowCompat.setDecorFitsSystemWindows(window, true)

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
    fun loginFacebook()
    {
        LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile", "email"))
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult?> {
            override fun onSuccess(result: LoginResult?) {
                if(result != null) {
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
                if (jsonObject.has("name")) {
                    val facebookName = jsonObject.getString("name")
                    Log.i("Facebook Name: ", facebookName)
                } else {
                    Log.i("Facebook Name: ", "Not exists")
                }


                // Facebook Profile Pic URL
                if (jsonObject.has("picture")) {
                    val facebookPictureObject = jsonObject.getJSONObject("picture")
                    if (facebookPictureObject.has("data")) {
                        val facebookDataObject = facebookPictureObject.getJSONObject("data")
                        if (facebookDataObject.has("url")) {
                            val facebookProfilePicURL = facebookDataObject.getString("url")
                            Log.i("Facebook Profile Pic URL: ", facebookProfilePicURL)
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
            }).executeAsync()
    }

    fun logoutFB()
    {
        LoginManager.getInstance().logOut()
    }
}