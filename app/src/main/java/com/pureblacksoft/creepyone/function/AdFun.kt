package com.pureblacksoft.creepyone.function

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import com.google.android.gms.ads.*
import com.pureblacksoft.creepyone.R

class AdFun
{
    companion object {
        fun loadBannerAd(context: Context, adSize: AdSize, flMobileAd: FrameLayout) {
            val adView = AdView(context)
            adView.adSize = adSize
            adView.adUnitId = context.getString(R.string.AD_UNIT_ID)

            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)

            flMobileAd.addView(adView)

            adView.adListener = object: AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()

                    flMobileAd.visibility = View.VISIBLE
                }

                override fun onAdFailedToLoad(adError: LoadAdError?) {
                    super.onAdFailedToLoad(adError)

                    flMobileAd.visibility = View.GONE
                }
            }
        }

        fun getAdSize(activity: Activity): AdSize {
            val display = activity.windowManager.defaultDisplay
            val metrics = DisplayMetrics()
            display.getMetrics(metrics)

            val widthPixels = metrics.widthPixels
            val density = metrics.density
            val adWidth = (widthPixels / density).toInt()

            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth)
        }
    }
}