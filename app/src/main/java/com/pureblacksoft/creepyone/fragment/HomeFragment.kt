package com.pureblacksoft.creepyone.fragment

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pureblacksoft.creepyone.R
import com.pureblacksoft.creepyone.activity.MainActivity
import com.pureblacksoft.creepyone.activity.StoryActivity
import com.pureblacksoft.creepyone.adapter.StoryAdapter
import com.pureblacksoft.creepyone.databinding.FragmentHomeBinding
import com.pureblacksoft.creepyone.dialog.ManageDialog
import com.pureblacksoft.creepyone.function.LogFun
import com.pureblacksoft.creepyone.function.StoreFun
import com.pureblacksoft.creepyone.loader.DataLoader
import com.pureblacksoft.creepyone.loader.StoryDownloader
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment(R.layout.fragment_home)
{
    companion object {
        private var currentPosition = 0
        private var currentState: Parcelable? = null
    }

    private lateinit var mContext: Context
    private lateinit var mActivity: MainActivity
    private lateinit var rootHF: View
    private lateinit var linearManager: LinearLayoutManager
    private lateinit var storyAdapter: StoryAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = requireContext()
        mActivity = requireActivity() as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        rootHF = FragmentHomeBinding.bind(view).root

        MainActivity.currentFragmentId = MainActivity.ID_FRAGMENT_HOME

        //region Read Values
        currentPosition = StoreFun.readInt(StoreFun.KEY_CURRENT_POSITION)
        //endregion

        //region Toolbar
        mActivity.ivIconMTB.setImageResource(R.drawable.ic_home_bwhite)
        mActivity.tvTitleMTB.text = getString(R.string.Home_Title)
        //endregion

        //region RecyclerView
        linearManager = LinearLayoutManager(mContext)
        linearManager.orientation = LinearLayoutManager.VERTICAL
        rootHF.recyclerViewHF.layoutManager = linearManager

        val itmDecoration = DividerItemDecoration(mContext, linearManager.orientation)
        ContextCompat.getDrawable(mContext, R.drawable.shape_divider)?.let { itmDecoration.setDrawable(it) }
        rootHF.recyclerViewHF.addItemDecoration(itmDecoration)

        setStoryAdapter()
        //endregion

        //region Filter/Sort Control
        fun setManageButton() {
            if (ManageDialog.currentFilterId != ManageDialog.ID_FILTER_ALL || ManageDialog.currentSortId != ManageDialog.ID_SORT_DATE_NEW) {
                mActivity.ivButtonMTB.setImageResource(R.drawable.ic_manage_white)
            }
            else {
                mActivity.ivButtonMTB.setImageResource(R.drawable.ic_manage_bwhite)
            }
        }

        setManageButton()
        //endregion

        //region Events
        val manageDialog = ManageDialog(mContext, mActivity)

        ManageDialog.onCancel = {
            manageDialog.dismiss()
        }

        ManageDialog.onApply = {
            manageDialog.dismiss()

            currentPosition = 0
            currentState = null

            setManageButton()
            setStoryAdapter()
        }

        MainActivity.onSuccessfulUpdate = {
            saveState()
            setStoryAdapter()
        }

        MainActivity.onFailedUpdate = {
            dataControl()
        }
        //endregion

        //region Buttons
        mActivity.ivButtonMTB.setOnClickListener {
            manageDialog.show()
        }

        mActivity.bottomNavMA.setOnNavigationItemReselectedListener {
            if (it.itemId == R.id.itmHome) {
                linearManager.smoothScrollToPosition(rootHF.recyclerViewHF, null, 0)
                mActivity.syncData()
            }
        }
        //endregion

        return view
    }

    private fun setStoryAdapter() {
        LogFun.logDataI("setStoryAdapter() HF -> Running")

        storyAdapter = StoryAdapter(DataLoader.creepyoneDB.getManagedStoryList(ManageDialog.currentFilterId, ManageDialog.currentSortId))
        rootHF.recyclerViewHF.adapter = storyAdapter

        dataControl()
        restoreState()
    }

    private fun dataControl() {
        if (DataLoader.creepyoneDB.getStoryList().size == 0) {
            if (DataLoader.checkingServer || StoryDownloader.requesting) {
                rootHF.llDownloadingHF.visibility = View.VISIBLE
                rootHF.llFailedDownloadHF.visibility = View.GONE
            }
            else {
                rootHF.llDownloadingHF.visibility = View.GONE
                rootHF.llFailedDownloadHF.visibility = View.VISIBLE

                if (DataLoader.totalServerFail >= 5) {
                    rootHF.tvFailedDownloadHF.text = getString(R.string.Download_Fail_Server)
                }

                rootHF.tvDownloadAgain.setOnClickListener {
                    rootHF.llFailedDownloadHF.visibility = View.GONE
                    rootHF.llDownloadingHF.visibility = View.VISIBLE
                    mActivity.syncData()
                }
            }
        }
        else {
            rootHF.llDownloadingHF.visibility = View.GONE
            rootHF.llFailedDownloadHF.visibility = View.GONE

            if (storyAdapter.itemCount == 0 && ManageDialog.currentFilterId == ManageDialog.ID_FILTER_UNREAD) {
                rootHF.tvNoUnreadStoryHF.visibility = View.VISIBLE
            }
            else {
                rootHF.tvNoUnreadStoryHF.visibility = View.GONE
            }
        }
    }

    private fun saveState() {
        if (storyAdapter.itemCount != 0) {
            currentPosition = linearManager.findFirstCompletelyVisibleItemPosition()
            currentState = linearManager.onSaveInstanceState()
        }
    }

    private fun restoreState() {
        if (storyAdapter.itemCount != 0) {
            if (currentPosition != 0) {
                linearManager.scrollToPosition(currentPosition)
            }

            if (currentState != null) {
                linearManager.onRestoreInstanceState(currentState)
            }
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

        //region Write Values
        StoreFun.writeInt(StoreFun.KEY_CURRENT_POSITION, currentPosition)
        //endregion
    }

    override fun onDestroyView() {
        super.onDestroyView()

        mActivity.ivButtonMTB.setImageResource(0)
        mActivity.bottomNavMA.setOnNavigationItemReselectedListener(null)
    }
}

//PureBlack Software / Murat BIYIK