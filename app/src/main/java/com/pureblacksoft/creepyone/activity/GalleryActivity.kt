package com.pureblacksoft.creepyone.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pureblacksoft.creepyone.R
import com.pureblacksoft.creepyone.adapter.PictureAdapter
import com.pureblacksoft.creepyone.databinding.ActivityGalleryBinding
import com.pureblacksoft.creepyone.function.LogFun
import com.pureblacksoft.creepyone.loader.DataLoader
import kotlinx.android.synthetic.main.activity_gallery.view.*

class GalleryActivity : AppCompatActivity()
{
    private lateinit var rootGA: View
    private lateinit var linearManager: LinearLayoutManager
    private lateinit var pictureAdapter: PictureAdapter
    private var currentState: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rootGA = ActivityGalleryBinding.inflate(layoutInflater).root
        setContentView(rootGA)

        //region Toolbar
        rootGA.ivBackGTB.setOnClickListener {
            onBackPressed()
        }
        //endregion

        //region RecyclerView
        linearManager = LinearLayoutManager(this)
        linearManager.orientation = LinearLayoutManager.VERTICAL
        rootGA.recyclerViewGA.layoutManager = linearManager

        val itmDecoration = DividerItemDecoration(this, linearManager.orientation)
        ContextCompat.getDrawable(this, R.drawable.shape_divider)?.let { itmDecoration.setDrawable(it) }
        rootGA.recyclerViewGA.addItemDecoration(itmDecoration)

        setPictureAdapter()
        //endregion
    }

    private fun setPictureAdapter() {
        LogFun.logDataI("setPictureAdapter() GA -> Running")

        pictureAdapter = PictureAdapter(DataLoader.creepyoneDB.getPictureStoryList())
        rootGA.recyclerViewGA.adapter = pictureAdapter

        dataControl()
        restoreState()
    }

    private fun dataControl() {
        val pictureCount = pictureAdapter.itemCount

        if (pictureCount == 0) {
            rootGA.tvNoPictureGA.visibility = View.VISIBLE
        }
        else {
            rootGA.tvNoPictureGA.visibility = View.GONE
        }

        rootGA.tvCountGTB.text = getString(R.string.Picture_Count, pictureCount)
    }

    private fun saveState() {
        if (pictureAdapter.itemCount != 0) {
            currentState = linearManager.onSaveInstanceState()
        }
    }

    private fun restoreState() {
        if (pictureAdapter.itemCount != 0 && currentState != null) {
            linearManager.onRestoreInstanceState(currentState)
        }
    }

    override fun onResume() {
        super.onResume()

        if (StoryActivity.choiceChanged) {
            StoryActivity.choiceChanged = false
            setPictureAdapter()
        }
    }

    override fun onPause() {
        super.onPause()

        saveState()
    }
}

//PureBlack Software / Murat BIYIK