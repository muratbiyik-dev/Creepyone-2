package com.pureblacksoft.creepyone.service

import android.app.IntentService
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.pureblacksoft.creepyone.data.Story
import com.pureblacksoft.creepyone.function.LogFun
import com.pureblacksoft.creepyone.function.StoreFun
import com.pureblacksoft.creepyone.loader.DataLoader
import com.pureblacksoft.creepyone.loader.StoryDownloader
import org.json.JSONException
import java.io.ByteArrayOutputStream
import java.net.URL

class StoryDataService : IntentService("StoryDataService")
{
    companion object {
        var dataUpdated = false
        var totalNewStory = 0

        var onFinish: (() -> Unit)? = null
    }

    private val statusObject = StoryDownloader.statusObject
    private val storyArray = StoryDownloader.resultArray

    private val storyIdList = mutableListOf<Int>()
    private val storyTitleList = mutableListOf<String>()
    private val storyContentList = mutableListOf<String>()
    private val storyAuthorList = mutableListOf<String>()
    private val storyImageList = mutableListOf<ByteArray?>()
    private val storyPopularityList = mutableListOf<Int>()
    private val storyLengthList = mutableListOf<Float>()

    private val storyOpIdList = mutableListOf<Int>()

    override fun onCreate() {
        super.onCreate()

        LogFun.logDataI("StoryDataService{} -> Created")
    }

    override fun onHandleIntent(intent: Intent?) {
        getStoryData()
        updateStoryDB()
        serviceFinish()
    }

    private fun getStoryData() {
        LogFun.logDataI("getStoryData() -> Running")

        val saLength = storyArray.length()

        try {
            //region Global Data
            for (i in 0 until saLength) {
                val jsonObject = storyArray.getJSONObject(i)

                val id = jsonObject.getString("id").toInt()
                val popularity = jsonObject.getString("popularity").toInt()

                storyIdList.add(id)
                storyPopularityList.add(popularity)

                //region Image
                val imageName = jsonObject.getString("image")
                if (imageName == "null") {
                    storyImageList.add(null)
                }
                else {
                    val imageURL = URL(StoryDownloader.URL_IMAGE_STORY + imageName)
                    val connection = imageURL.openConnection()
                    connection.doInput = true
                    connection.connect()
                    val inputStream = connection.getInputStream()
                    val imageBitmap = BitmapFactory.decodeStream(inputStream)

                    //region Convert Bitmap to ByteArray
                    val outputStream = ByteArrayOutputStream()
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    val image = outputStream.toByteArray()
                    imageBitmap.recycle()
                    //endregion

                    storyImageList.add(image)
                }
                //endregion

                val opId = jsonObject.getString("op_id").toInt()
                storyOpIdList.add(opId)
            }
            //endregion

            //region Local Data
            fun getStoryLength(content: String): Float {
                val contentLength = content.length
                val charArray = CharArray(contentLength)
                var count = 0f
                for (i in 0 until contentLength) {
                    charArray[i] = content[i]
                    if (((i > 0) && (charArray[i] != ' ') && (charArray[i - 1] == ' ')) || ((charArray[0] != ' ') && (i == 0))) {
                        count++
                    }
                }

                return count / 200
            }

            fun getLocalData(title: String, content: String, author: String) {
                for (i in 0 until saLength) {
                    val jsonObject = storyArray.getJSONObject(i)

                    val localTitle = jsonObject.getString(title)
                    val localContent = jsonObject.getString(content)
                    val localAuthor = jsonObject.getString(author)
                    val localLength = getStoryLength(localContent)

                    storyTitleList.add(localTitle)
                    storyContentList.add(localContent)
                    storyAuthorList.add(localAuthor)
                    storyLengthList.add(localLength)
                }
            }

            when (DataLoader.appLang) {
                DataLoader.LANG_TURKISH -> {
                    getLocalData("title_tr", "content_tr", "author_tr")
                }
                else -> {
                    getLocalData("title_en", "content_en", "author_en")
                }
            }
            //endregion
        }
        catch (e: JSONException) {
            LogFun.logDataE(e.toString())
        }
    }

    private fun updateStoryDB() {
        val silSize = storyIdList.size
        if (silSize > 0 && silSize == storyImageList.size) {
            LogFun.logDataI("updateStoryDB -> Succeed")

            fun getStory(i: Int): Story {
                return Story(
                        id = storyIdList[i],
                        title = storyTitleList[i],
                        content = storyContentList[i],
                        author = storyAuthorList[i],
                        image = storyImageList[i],
                        popularity = storyPopularityList[i],
                        length = storyLengthList[i],
                        favorite = 0,
                        readed = 0)
            }

            val oldStoryList = DataLoader.creepyoneDB.getStoryList()
            if (oldStoryList.isEmpty()) {
                LogFun.logDataI("(oldStoryList.isEmpty()) -> Insert All")

                var favoriteStoryIdList = StoreFun.readListInt(StoreFun.KEY_FAVORITE_STORY_ID_LIST)
                if (favoriteStoryIdList.isEmpty()) {
                    favoriteStoryIdList = StoreFun.readListInt("favoriteIdList")
                }

                var readedStoryIdList = StoreFun.readListInt(StoreFun.KEY_READED_STORY_ID_LIST)
                if (readedStoryIdList.isEmpty()) {
                    readedStoryIdList = StoreFun.readListInt("readedIdList")
                }

                for (i in 0 until silSize) {
                    val story = getStory(i)
                    val storyId = story.id

                    LogFun.logDataI("Story $storyId -> Insert")

                    if (favoriteStoryIdList.contains(storyId)) {
                        story.favorite = 1
                    }

                    if (readedStoryIdList.contains(storyId)) {
                        story.readed = 1
                    }

                    DataLoader.creepyoneDB.insertStory(story)
                }
            }
            else {
                LogFun.logDataI("(!oldStoryList.isEmpty()) -> Check Operation")

                for (i in 0 until silSize) {
                    val story = getStory(i)
                    val storyId = story.id
                    val oldStory = oldStoryList.find { it.id == storyId}

                    when (storyOpIdList[i]) {
                        DataLoader.ID_OP_UPDATE -> {
                            if (oldStory == null) {
                                LogFun.logDataI("Story $storyId -> Insert")

                                totalNewStory++

                                DataLoader.creepyoneDB.insertStory(story)
                            }
                            else {
                                LogFun.logDataI("Story $storyId -> Update")

                                story.favorite = oldStory.favorite
                                story.readed = oldStory.readed

                                DataLoader.creepyoneDB.updateStory(story, oldStory)
                            }
                        }
                        DataLoader.ID_OP_DELETE -> {
                            if (oldStory != null) {
                                LogFun.logDataI("Story $storyId -> Delete")

                                DataLoader.creepyoneDB.deleteStory(oldStory)
                            }
                        }
                    }
                }
            }
            DataLoader.creepyoneDB.close()

            //region Write Operation Time
            val opTime = statusObject.getString("op_time")
            StoreFun.writeString(StoreFun.KEY_STORY_OP_TIME, opTime)
            //endregion

            dataUpdated = true
        }
        else {
            LogFun.logDataI("updateStoryDB -> Failed")

            dataUpdated = false
        }
    }

    private fun serviceFinish() {
        LogFun.logDataI("StoryDataService{} -> Finished")

        stopSelf()
        StoryDownloader.requesting = false
        onFinish?.invoke()
    }
}

//PureBlack Software / Murat BIYIK