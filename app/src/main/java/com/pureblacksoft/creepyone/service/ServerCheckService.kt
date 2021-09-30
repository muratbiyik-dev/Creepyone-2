package com.pureblacksoft.creepyone.service

import android.app.IntentService
import android.content.Intent
import com.pureblacksoft.creepyone.function.LogFun
import com.pureblacksoft.creepyone.loader.DataLoader
import java.lang.Exception
import java.net.URL

class ServerCheckService : IntentService("ServerCheckService")
{
    companion object {
        var onSuccess: (() -> Unit)? = null
        var onFailure: (() -> Unit)? = null
    }

    override fun onHandleIntent(intent: Intent?) {
        try {
            val serverURL = URL(DataLoader.URL_CREEPYONE)
            val connection = serverURL.openConnection()
            connection.connectTimeout = 10000
            connection.connect()

            LogFun.logDataI("ServerCheckService{} -> Successful")
            onSuccess?.invoke()
        }
        catch (e: Exception) {
            LogFun.logDataE(e.toString())

            LogFun.logDataI("ServerCheckService{} -> Failed")
            onFailure?.invoke()
        }

        stopSelf()
        DataLoader.checkingServer = false
    }
}

//PureBlack Software / Murat BIYIK