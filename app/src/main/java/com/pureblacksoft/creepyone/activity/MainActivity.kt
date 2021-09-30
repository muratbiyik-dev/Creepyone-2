package com.pureblacksoft.creepyone.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.MobileAds
import com.pureblacksoft.creepyone.R
import com.pureblacksoft.creepyone.databinding.ActivityMainBinding
import com.pureblacksoft.creepyone.dialog.ManageDialog
import com.pureblacksoft.creepyone.fragment.ArchiveFragment
import com.pureblacksoft.creepyone.fragment.HomeFragment
import com.pureblacksoft.creepyone.fragment.SearchFragment
import com.pureblacksoft.creepyone.function.*
import com.pureblacksoft.creepyone.loader.DataLoader
import com.pureblacksoft.creepyone.loader.StoryDownloader
import com.pureblacksoft.creepyone.service.ServerCheckService
import com.pureblacksoft.creepyone.service.StoryDataService
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity()
{
    companion object {
        //region Fragment Id
        const val ID_FRAGMENT_HOME = 0
        const val ID_FRAGMENT_SEARCH = 1
        const val ID_FRAGMENT_ARCHIVE = 2
        //endregion

        var currentFragmentId = ID_FRAGMENT_HOME

        var onSuccessfulUpdate: (() -> Unit)? = null
        var onFailedUpdate: (() -> Unit)? = null

        private var dataLoaded = false
        private var appLaunched = false
    }

    private lateinit var rootMA: View
    private var backPressedOnce = false
    private val dataLoader = DataLoader()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Creepyone)
        super.onCreate(savedInstanceState)

        rootMA = ActivityMainBinding.inflate(layoutInflater).root
        setContentView(rootMA)

        //region Read Values
        ManageDialog.currentFilterId = StoreFun.readInt(StoreFun.KEY_CURRENT_FILTER)
        ManageDialog.currentSortId = StoreFun.readInt(StoreFun.KEY_CURRENT_SORT)

        PrefFun.currentFont = StoreFun.readInt(StoreFun.KEY_CURRENT_FONT)
        PrefFun.currentFontSize = StoreFun.readInt(StoreFun.KEY_CURRENT_FONT_SIZE)
        //endregion

        //region Load Control
        if (!dataLoaded) {
            dataLoader.setDatabase()
            dataLoader.checkAppVersion()
            dataLoader.checkAppLanguage()
            dataLoaded = true
        }
        //endregion

        syncData()

        //region Comit HomeFragment
        comitFragment(HomeFragment())
        //endregion

        //region Bottom Navigation
        rootMA.bottomNavMA.itemIconTintList = null
        rootMA.bottomNavMA.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.itmSearch -> {
                    comitFragment(SearchFragment())
                }
                R.id.itmArchive -> {
                    comitFragment(ArchiveFragment())
                }
                else -> {
                    comitFragment(HomeFragment())
                }
            }

            true
        }
        //endregion

        //region Launch Control
        if (!appLaunched) {
            LaunchFun.checkTerms(this, this)
            LaunchFun.checkRating(this)
            MobileAds.initialize(this)
            appLaunched = true
        }
        //endregion
    }

    fun syncData() {
        rootMA.pbSyncingMTB.visibility = View.VISIBLE

        dataLoader.checkServer()

        ServerCheckService.onSuccess = {
            val storyDownloader = StoryDownloader()
            storyDownloader.requestData()

            StoryDownloader.onNoUpdate = {
                updateStatus(2)
            }

            StoryDownloader.onFailedUpdate = {
                if (StoryDownloader.totalRequestFail < 3) {
                    storyDownloader.requestData()
                }
                else {
                    updateStatus(0)
                }
            }

            StoryDataService.onFinish = {
                if (StoryDataService.dataUpdated) {
                    updateStatus(1)
                }
                else {
                    updateStatus(0)
                }
            }
        }

        ServerCheckService.onFailure = {
            updateStatus(0)
        }
    }

    private fun updateStatus(statusCode: Int) {
        runOnUiThread {
            rootMA.pbSyncingMTB.visibility = View.GONE

            when (statusCode) {
                0 -> {
                    onFailedUpdate?.invoke()
                    AppFun.showToast(this, R.string.Sync_Failed_Toast, Toast.LENGTH_LONG)
                }
                1 -> {
                    onSuccessfulUpdate?.invoke()
                    AppFun.showToast(this, R.string.Sync_Successful_Toast, Toast.LENGTH_LONG)

                    if (StoryDataService.totalNewStory > 0) {
                        AppFun.showToast(this, R.string.New_Story_Toast, Toast.LENGTH_LONG)
                    }
                }
                2 -> {
                    AppFun.showToast(this, R.string.Sync_Successful_Toast, Toast.LENGTH_LONG)
                }
            }
        }
    }

    private fun comitFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.containerMA, fragment).commit()
    }

    override fun onBackPressed() {
        if (currentFragmentId != ID_FRAGMENT_HOME) {
            val itmHome: View = rootMA.bottomNavMA.findViewById(R.id.itmHome)
            itmHome.performClick()
        }
        else {
            if (backPressedOnce) {
                finishAffinity()

                return
            }

            backPressedOnce = true

            AppFun.showToast(this, R.string.Back_Toast, Toast.LENGTH_SHORT)

            Handler().postDelayed({
                backPressedOnce = false
            }, 2000)
        }
    }
}

//PureBlack Software / Murat BIYIK