package com.pureblacksoft.creepyone.function

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.pureblacksoft.creepyone.R
import com.pureblacksoft.creepyone.dialog.ApproveDialog
import kotlin.system.exitProcess

class LaunchFun
{
    companion object {
        fun checkTerms(context: Context, activity: Activity) {
            val termsAccepted = StoreFun.readBoolean(StoreFun.KEY_TERMS_ACCEPTED)
            if (!termsAccepted) {
                val builder = ApproveDialog.alertBuilder(context)
                builder.setCancelable(false)
                builder.setTitle(R.string.Terms_Title)
                builder.setMessage(R.string.Terms_Content)
                builder.setPositiveButton(R.string.Terms_Accept) { _, _ ->
                    StoreFun.writeBoolean(StoreFun.KEY_TERMS_ACCEPTED, true)
                }
                builder.setNegativeButton(R.string.Terms_Reject) { _, _ ->
                    StoreFun.writeBoolean(StoreFun.KEY_TERMS_ACCEPTED, false)

                    activity.finishAffinity()
                    exitProcess(0)
                }
                builder.show()
            }
        }

        fun checkRating(context: Context) {
            val totalLaunch = StoreFun.readInt(StoreFun.KEY_TOTAL_LAUNCH)
            val totalLaunchReq = 10
            if (totalLaunch != -totalLaunchReq) {
                StoreFun.writeInt(StoreFun.KEY_TOTAL_LAUNCH, totalLaunch + 1)

                if (totalLaunch >= totalLaunchReq) {
                    val builder = ApproveDialog.alertBuilder(context)
                    builder.setTitle(R.string.App_Rating_Title)
                    builder.setMessage(R.string.App_Rating_Content)
                    builder.setPositiveButton(R.string.App_Rating_Positive) { _, _ ->
                        StoreFun.writeInt(StoreFun.KEY_TOTAL_LAUNCH, -totalLaunchReq)

                        val uri = Uri.parse("https://play.google.com/store/apps/details?id=com.pureblacksoft.creepyone")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        context.startActivity(intent)
                    }
                    builder.setNegativeButton(R.string.App_Rating_Negative) { _, _ ->
                        StoreFun.writeInt(StoreFun.KEY_TOTAL_LAUNCH, -totalLaunchReq)
                    }
                    builder.setNeutralButton(R.string.App_Rating_Neutral) { _, _ ->
                        StoreFun.writeInt(StoreFun.KEY_TOTAL_LAUNCH, 0)
                    }
                    builder.show()
                }
            }
        }
    }
}

//PureBlack Software / Murat BIYIK