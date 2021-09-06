package com.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import com.barservicegam.app.R
import com.fragmentcustom.*
import com.fragula.Navigator
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lib.*

class MainActivity : AppCompatActivity() {
    lateinit var navigatorTabNews: Navigator
    lateinit var navigatorTabVideos: Navigator
    lateinit var navigatorTabExplore: Navigator
    lateinit var navigatorTabSetting: Navigator

    lateinit var layoutParent: ConstraintLayout
    lateinit var bottomView: BottomNavigationView

    var homePagerFragment = HomePagerFragment.newInstance("", "")
    var listVideoFragment = ListVideoFragment.newInstance("", "")
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
}