package com.pureblacksoft.creepyone.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pureblacksoft.creepyone.R
import com.pureblacksoft.creepyone.adapter.StoryAdapter
import com.pureblacksoft.creepyone.databinding.ActivityListBinding
import com.pureblacksoft.creepyone.function.LogFun
import com.pureblacksoft.creepyone.loader.DataLoader
import kotlinx.android.synthetic.main.activity_list.view.*

class ListActivity : AppCompatActivity()
{
    companion object {
        //region List Id
        const val ID_LIST_POPULAR = 1
        const val ID_LIST_FAVORITE = 2
        //endregion

        var currentListId = 0
    }

    private lateinit var rootLA: View
    private lateinit var linearManager: LinearLayoutManager
    private lateinit var storyAdapter: StoryAdapter
    private var currentState: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rootLA = ActivityListBinding.inflate(layoutInflater).root
        setContentView(rootLA)

        //region Toolbar
        rootLA.ivBackLTB.setOnClickListener {
            onBackPressed()
        }
        //endregion

        //region List Control
        when (currentListId) {
            ID_LIST_POPULAR -> {
                rootLA.ivIconLTB.setImageResource(R.drawable.ic_popular_bwhite)
                rootLA.tvTitleLTB.text = getString(R.string.Popular_Stories_Title)
                rootLA.tvNoStoryLA.text = getString(R.string.No_Popular_Story)
            }
            ID_LIST_FAVORITE -> {
                rootLA.ivIconLTB.setImageResource(R.drawable.ic_favorite_bwhite)
                rootLA.tvTitleLTB.text = getString(R.string.Favorite_Stories_Title)
                rootLA.tvNoStoryLA.text = getString(R.string.No_Favorite_Story)
            }
        }
        //endregion

        //region RecyclerView
        linearManager = LinearLayoutManager(this)
        linearManager.orientation = LinearLayoutManager.VERTICAL
        rootLA.recyclerViewLA.layoutManager = linearManager

        val itmDecoration = DividerItemDecoration(this, linearManager.orientation)
        ContextCompat.getDrawable(this, R.drawable.shape_divider)?.let { itmDecoration.setDrawable(it) }
        rootLA.recyclerViewLA.addItemDecoration(itmDecoration)

        setStoryAdapter()
        //endregion
    }

    private fun setStoryAdapter() {
        LogFun.logDataI("setStoryAdapter() LA -> Running")

        when (currentListId) {
            ID_LIST_POPULAR -> {
                storyAdapter = StoryAdapter(DataLoader.creepyoneDB.getPopularStoryList())
            }
            ID_LIST_FAVORITE -> {
                storyAdapter = StoryAdapter(DataLoader.creepyoneDB.getFavoriteStoryList())
            }
        }

        rootLA.recyclerViewLA.adapter = storyAdapter

        dataControl()
        restoreState()
    }

    private fun dataControl() {
        val storyCount = storyAdapter.itemCount

        if (storyCount == 0) {
            rootLA.tvNoStoryLA.visibility = View.VISIBLE
        }
        else {
            rootLA.tvNoStoryLA.visibility = View.GONE
        }

        rootLA.tvCountLTB.text = getString(R.string.Story_Count, storyCount)
    }

    private fun saveState() {
        if (storyAdapter.itemCount != 0) {
            currentState = linearManager.onSaveInstanceState()
        }
    }

    private fun restoreState() {
        if (storyAdapter.itemCount != 0 && currentState != null) {
            linearManager.onRestoreInstanceState(currentState)
        }
    }

    override fun onResume() {
        super.onResume()

        if (StoryActivity.choiceChanged) {
            StoryActivity.choiceChanged = false
            setStoryAdapter()
        }
    }

    override fun onPause() {
        super.onPause()

        saveState()
    }
}

//PureBlack Software / Murat BIYIK