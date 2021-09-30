package com.pureblacksoft.creepyone.loader

import android.content.Intent
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.pureblacksoft.creepyone.function.LogFun
import com.pureblacksoft.creepyone.function.StoreFun
import com.pureblacksoft.creepyone.service.StoryDataService
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class StoryDownloader : DataLoader()
{
    companion object {
        //region Data URL
        const val URL_IMAGE_STORY = URL_IMAGE + "story/"
        const val URL_DATA_STORY = URL_SCRIPT + "db_story_data.php?last_op_time='%s'"
        //endregion

        var requesting = false
        var totalRequestFail = 0
        lateinit var statusObject: JSONObject
        lateinit var resultArray: JSONArray

        var onNoUpdate: (() -> Unit)? = null
        var onFailedUpdate: (() -> Unit)? = null
    }

    fun requestData() {
        if (!requesting) {
            val dataType = "Story Data"
            val lastOpTime = StoreFun.readString(StoreFun.KEY_STORY_OP_TIME)
            val dataURL = String.format(URL_DATA_STORY, lastOpTime)
            val dataIntent = Intent(appContext, StoryDataService::class.java)

            LogFun.logDataI("requestData() -> $dataType")

            requesting = true

            val requestQueue = Volley.newRequestQueue(appContext)
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, dataURL, null,
                    { response ->
                        LogFun.logDataI("(Connection Successful) -> $dataURL")

                        try {
                            statusObject = response.getJSONObject("status")
                            resultArray = response.getJSONArray("results")

                            val raLength = resultArray.length()
                            if (raLength > 0) {
                                LogFun.logDataI("(arrayLength > 0) -> Update $dataType")

                                appContext.startService(dataIntent)
                            }
                            else {
                                LogFun.logDataI("(arrayLength <= 0) -> Keep $dataType")

                                requesting = false
                                onNoUpdate?.invoke()
                            }
                        }
                        catch (e: JSONException)
                        {
                            LogFun.logDataE(e.toString())

                            requesting = false
                            onFailedUpdate?.invoke()
                        }
                    },
                    { error ->
                        LogFun.logDataI("(Connection Failed) -> $dataURL")
                        LogFun.logDataE(error.toString())

                        requesting = false
                        totalRequestFail++
                        onFailedUpdate?.invoke()
                    })
            jsonObjectRequest.retryPolicy = DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            requestQueue.add(jsonObjectRequest)
        }
    }
}

//PureBlack Software / Murat BIYIK