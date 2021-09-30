package com.pureblacksoft.creepyone.application

import android.app.Application
import android.content.Context

class CreepyoneApp : Application()
{
    companion object {
        private lateinit var context: Context

        fun getAppContext(): Context {
            return context
        }
    }

    override fun onCreate() {
        super.onCreate()

        context = applicationContext
    }
}

//PureBlack Software / Murat BIYIK