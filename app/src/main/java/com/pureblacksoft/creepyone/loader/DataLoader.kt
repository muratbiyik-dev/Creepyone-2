package com.pureblacksoft.creepyone.loader

import android.content.Intent
import androidx.core.content.pm.PackageInfoCompat
import com.pureblacksoft.creepyone.application.CreepyoneApp
import com.pureblacksoft.creepyone.database.CreepyoneDB
import com.pureblacksoft.creepyone.function.LogFun
import com.pureblacksoft.creepyone.function.StoreFun
import com.pureblacksoft.creepyone.service.ServerCheckService
import java.util.*

open class DataLoader
{
    companion object {
        //region Server URL
        const val URL_CREEPYONE = "Creepyone-URL"
        const val URL_IMAGE = URL_CREEPYONE + "image/"
        const val URL_SCRIPT = URL_CREEPYONE + "script/"
        //endregion

        //region Language
        val LANG_ENGLISH: String = java.util.Locale("en").language
        val LANG_TURKISH: String = java.util.Locale("tr").language
        //endregion

        //region Operation Id
        const val ID_OP_UPDATE = 0
        const val ID_OP_DELETE = 1
        //endregion

        var checkingServer = false
        var totalServerFail = 0
        lateinit var appLang: String
        lateinit var creepyoneDB: CreepyoneDB
    }

    val appContext = CreepyoneApp.getAppContext()

    fun setDatabase() {
        LogFun.logDataI("setDatabase() -> Running")

        creepyoneDB = CreepyoneDB(appContext)
    }

    fun checkAppVersion() {
        LogFun.logDataI("checkAppVersion() -> Running")

        val packInfo = appContext.packageManager.getPackageInfo(appContext.packageName, 0)
        val appVer = PackageInfoCompat.getLongVersionCode(packInfo).toInt()
        val lastAppVer = StoreFun.readInt(StoreFun.KEY_APP_VER)

        if (lastAppVer != 0 && appVer != lastAppVer) {
            LogFun.logDataI("(appVer != lastAppVer) -> Delete All Data")

            StoreFun.remove(StoreFun.KEY_STORY_OP_TIME)
            creepyoneDB.deleteStoryTable()
        }

        StoreFun.writeInt(StoreFun.KEY_APP_VER, appVer)
    }

    fun checkAppLanguage() {
        LogFun.logDataI("checkAppLanguage() -> Running")

        appLang = when (Locale.getDefault().language) {
            LANG_TURKISH -> {
                LANG_TURKISH
            }
            else -> {
                LANG_ENGLISH
            }
        }
        val lastAppLang = StoreFun.readString(StoreFun.KEY_APP_LANG)

        if (lastAppLang != null && appLang != lastAppLang) {
            LogFun.logDataI("(appLang != lastAppLang) -> Delete All Data")

            StoreFun.remove(StoreFun.KEY_STORY_OP_TIME)
            creepyoneDB.deleteStoryTable()
        }

        StoreFun.writeString(StoreFun.KEY_APP_LANG, appLang)
    }

    fun checkServer() {
        if (!checkingServer) {
            LogFun.logDataI("checkServer() -> Running")

            checkingServer = true

            val serverIntent = Intent(appContext, ServerCheckService::class.java)
            appContext.startService(serverIntent)
        }
    }
}

//PureBlack Software / Murat BIYIK