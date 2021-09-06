package com.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import com.barservicegam.app.R
import com.fragmentcustom.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import com.api.Global.currentTimeSecUTC
import com.fragmentcustom.HomePagerFragment
import com.fragmentcustom.SettingFragment
import java.time.LocalDate

class BottomNavActivity : AppCompatActivity() {
    lateinit var contentFragment: FrameLayout
    var arrFragment = ArrayList<Fragment>()
    var currentTabIndex:Int = 0

    lateinit var btnHome: Button
    lateinit var btnSetting: Button

//    var homeFragment:HomeFragment? = null
      var homeFragment: HomePagerFragment? = null

    var settingFragment = SettingFragment.newInstance("", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav)

        homeFragment = HomePagerFragment.newInstance("", "")
        arrFragment.add(homeFragment!!)
        supportFragmentManager.beginTransaction().replace(R.id.contentFragment, homeFragment!!).commit()

        val aaa = supportFragmentManager.beginTransaction()
        
        contentFragment = findViewById(R.id.contentFragment)
        btnHome = findViewById(R.id.btnHome)
        btnSetting = findViewById(R.id.btnSetting)

        btnHome.setOnClickListener{
            val previousTabIndex = this.currentTabIndex
            this.currentTabIndex = 0

            val transaction = supportFragmentManager.beginTransaction()
            if(arrFragment.count() > 0){
                transaction.hide(arrFragment[previousTabIndex])
            }
            transaction.show(arrFragment[this.currentTabIndex])
            transaction.commit()
        }

        btnSetting.setOnClickListener{
            val previousTabIndex = this.currentTabIndex
            this.currentTabIndex = 1

            val transaction = supportFragmentManager.beginTransaction()
            if(arrFragment.contains(settingFragment) == false) {
                settingFragment = SettingFragment.newInstance("", "")
                transaction.add(R.id.contentFragment, settingFragment!!)
                arrFragment.add(settingFragment!!)
            }

            if(arrFragment.count() > 0){
                transaction.hide(arrFragment[previousTabIndex])
            }

            transaction.show(arrFragment[this.currentTabIndex])
            transaction.commit()
        }
    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.contentFragment,fragment)
            commit()
        }
}