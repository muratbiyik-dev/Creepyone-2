package com.pureblacksoft.creepyone.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pureblacksoft.creepyone.R
import com.pureblacksoft.creepyone.activity.ListActivity
import com.pureblacksoft.creepyone.activity.MainActivity
import com.pureblacksoft.creepyone.activity.StoryActivity
import com.pureblacksoft.creepyone.adapter.StoryAdapter
import com.pureblacksoft.creepyone.databinding.FragmentSearchBinding
import com.pureblacksoft.creepyone.function.AppFun
import com.pureblacksoft.creepyone.function.LogFun
import com.pureblacksoft.creepyone.loader.DataLoader
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.view.*

class SearchFragment : Fragment(R.layout.fragment_search)
{
    private lateinit var mContext: Context
    private lateinit var mActivity: MainActivity
    private lateinit var rootSF: View
    private lateinit var linearManager: LinearLayoutManager
    private lateinit var storyAdapter: StoryAdapter
    private var currentState: Parcelable? = null
    private var searchText: String? = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = requireContext()
        mActivity = requireActivity() as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        rootSF = FragmentSearchBinding.bind(view).root

        MainActivity.currentFragmentId = MainActivity.ID_FRAGMENT_SEARCH

        //region Toolbar
        mActivity.ivIconMTB.setImageResource(R.drawable.ic_search_bwhite)
        mActivity.tvTitleMTB.text = getString(R.string.Search_Title)
        //endregion

        //region RecyclerView
        linearManager = LinearLayoutManager(mContext)
        linearManager.orientation = LinearLayoutManager.VERTICAL
        rootSF.recyclerViewSF.layoutManager = linearManager

        val itmDecoration = DividerItemDecoration(mContext, linearManager.orientation)
        ContextCompat.getDrawable(mContext, R.drawable.shape_divider)?.let { itmDecoration.setDrawable(it) }
        rootSF.recyclerViewSF.addItemDecoration(itmDecoration)

        setStoryAdapter()
        //endregion

        //region Search
        rootSF.searchViewSF.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                storyAdapter.filter.filter(newText)
                searchText = newText

                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (storyAdapter.itemCount == 0) {
                    rootSF.tvNoResultSF.visibility = View.VISIBLE
                }

                rootSF.searchViewSF.clearFocus()

                return false
            }
        })

        rootSF.searchViewSF.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                rootSF.linearLayoutSF.visibility = View.GONE
                rootSF.tvNoResultSF.visibility = View.GONE
            }
            else if (storyAdapter.itemCount == 0 && rootSF.tvNoResultSF.visibility == View.GONE) {
                rootSF.linearLayoutSF.visibility = View.VISIBLE
            }
        }
        //endregion

        //region Buttons
        rootSF.llPopularStoriesSF.setOnClickListener {
            ListActivity.currentListId = ListActivity.ID_LIST_POPULAR

            val intent = Intent(mContext, ListActivity::class.java)
            mContext.startActivity(intent)
        }

        rootSF.llFavoriteStoriesSF.setOnClickListener {
            ListActivity.currentListId = ListActivity.ID_LIST_FAVORITE

            val intent = Intent(mContext, ListActivity::class.java)
            mContext.startActivity(intent)
        }

        rootSF.llRandomStorySF.setOnClickListener {
            val storyList = DataLoader.creepyoneDB.getStoryList()
            val slSize = storyList.size
            if (slSize == 0) {
                AppFun.showToast(mContext, R.string.No_Story_Toast, Toast.LENGTH_LONG)
            }
            else {
                StoryAdapter.accessedStory = storyList[(0..slSize).random()]

                val intent = Intent(mContext, StoryActivity::class.java)
                mContext.startActivity(intent)
            }
        }
        //endregion

        return view
    }

    private fun setStoryAdapter() {
        LogFun.logDataI("setStoryAdapter() SF -> Running")

        storyAdapter = StoryAdapter(DataLoader.creepyoneDB.getStoryList())
        rootSF.recyclerViewSF.adapter = storyAdapter
        storyAdapter.filter.filter(searchText)

        restoreState()
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