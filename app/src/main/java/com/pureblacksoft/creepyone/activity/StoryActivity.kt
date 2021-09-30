package com.pureblacksoft.creepyone.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.pureblacksoft.creepyone.R
import com.pureblacksoft.creepyone.adapter.StoryAdapter
import com.pureblacksoft.creepyone.data.Story
import com.pureblacksoft.creepyone.databinding.ActivityStoryBinding
import com.pureblacksoft.creepyone.function.AdFun
import com.pureblacksoft.creepyone.function.AppFun
import com.pureblacksoft.creepyone.function.PrefFun
import com.pureblacksoft.creepyone.function.StoreFun
import com.pureblacksoft.creepyone.loader.DataLoader
import kotlinx.android.synthetic.main.activity_story.view.*

class StoryActivity : AppCompatActivity()
{
    companion object {
        var choiceChanged = false
    }

    private lateinit var rootSA: View
    private lateinit var accessedStory: Story
    private var favorite = 0
    private var readed = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rootSA = ActivityStoryBinding.inflate(layoutInflater).root
        setContentView(rootSA)

        //region Get Data
        accessedStory = StoryAdapter.accessedStory

        rootSA.tvTitleSA.text = accessedStory.title
        rootSA.tvContentSA.text = accessedStory.content
        rootSA.tvAuthorSA.text = accessedStory.author
        rootSA.tvLengthSTB.text = getString(R.string.Story_Length, accessedStory.length)

        val image = accessedStory.image
        if (image != null) {
            val imageBitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
            rootSA.ivImageSA.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, imageBitmap.width, imageBitmap.height, false))
        }

        PrefFun.setStoryFont(rootSA.tvTitleSA, rootSA.tvContentSA, rootSA.tvAuthorSA, rootSA.tvAuthorLabelSA)
        //endregion

        //region Toolbar
        rootSA.ivBackSTB.setOnClickListener {
            onBackPressed()
        }

        //region Favorite CheckBox
        if (accessedStory.favorite == 1) {
            favorite = 1
            rootSA.cbFavoriteSTB.isChecked = true
        }

        rootSA.cbFavoriteSTB.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                favorite = 1
                AppFun.showToast(this, R.string.Favorite_Toast, Toast.LENGTH_SHORT)
            }
            else {
                favorite = 0
            }
        }
        //endregion

        //region Readed CheckBox
        if (accessedStory.readed == 1) {
            readed = 1
            rootSA.cbReadedSTB.isChecked = true
            rootSA.tvTitleSA.setTextColor(getColor(R.color.Story_Title_Readed_TextColor))
        }

        rootSA.cbReadedSTB.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                readed = 1
                rootSA.tvTitleSA.setTextColor(getColor(R.color.Story_Title_Readed_TextColor))
                AppFun.showToast(this, R.string.Readed_Toast, Toast.LENGTH_SHORT)
            }
            else {
                readed = 0
                rootSA.tvTitleSA.setTextColor(getColor(R.color.Story_Title_TextColor))
            }
        }
        //endregion
        //endregion

        //region Mobile Ads
        AdFun.loadBannerAd(this, AdFun.getAdSize(this), rootSA.flMobileAdSA_1)
        AdFun.loadBannerAd(this, AdFun.getAdSize(this), rootSA.flMobileAdSA_2)
        //endregion
    }

    override fun onPause() {
        super.onPause()

        //region Favorite Control
        if (accessedStory.favorite != favorite) {
            choiceChanged = true
            DataLoader.creepyoneDB.setStoryFavorite(accessedStory, favorite)
            StoreFun.writeListInt(StoreFun.KEY_FAVORITE_STORY_ID_LIST, DataLoader.creepyoneDB.getFavoriteStoryIdList())
        }
        //endregion

        //region Readed Control
        if (accessedStory.readed != readed) {
            choiceChanged = true
            DataLoader.creepyoneDB.setStoryReaded(accessedStory, readed)
            StoreFun.writeListInt(StoreFun.KEY_READED_STORY_ID_LIST, DataLoader.creepyoneDB.getReadedStoryIdList())
        }
        //endregion
    }
}

//PureBlack Software / Murat BIYIK