package com.pureblacksoft.creepyone.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.AdapterView
import com.pureblacksoft.creepyone.R
import com.pureblacksoft.creepyone.adapter.ButtonAdapter
import com.pureblacksoft.creepyone.databinding.DialogManageBinding
import com.pureblacksoft.creepyone.function.StoreFun
import kotlinx.android.synthetic.main.dialog_manage.view.*

class ManageDialog(context: Context, activity: Activity) : Dialog(activity), AdapterView.OnItemSelectedListener
{
    companion object {
        //region Filter Id
        const val ID_FILTER_ALL = 0
        const val ID_FILTER_UNREAD = 1
        //endregion

        //region Sort Id
        const val ID_SORT_DATE_NEW = 0
        const val ID_SORT_DATE_OLD = 1
        const val ID_SORT_LENGTH_ASC = 2
        const val ID_SORT_LENGTH_DESC = 3
        //endregion

        var currentFilterId = ID_FILTER_ALL
        var currentSortId = ID_SORT_DATE_NEW

        var onCancel: (() -> Unit)? = null
        var onApply: (() -> Unit)? = null
    }

    private val mContext = context
    private lateinit var rootMD: View
    private var selectedFilterId = ID_FILTER_ALL
    private var selectedSortId = ID_SORT_DATE_NEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)

        rootMD = DialogManageBinding.inflate(layoutInflater).root
        setContentView(rootMD)

        //region Spinners
        //region Filter Spinner
        rootMD.sFilterMD.onItemSelectedListener = this

        val filterList = mutableListOf<String>()
        filterList.add(mContext.getString(R.string.Filter_All))
        filterList.add(mContext.getString(R.string.Filter_Unread))

        val filterAdapter = ButtonAdapter(mContext, filterList)
        rootMD.sFilterMD.adapter = filterAdapter
        //endregion

        //region Sort Spinner
        rootMD.sSortMD.onItemSelectedListener = this

        val sortList = mutableListOf<String>()
        sortList.add(mContext.getString(R.string.Sort_Date_New))
        sortList.add(mContext.getString(R.string.Sort_Date_Old))
        sortList.add(mContext.getString(R.string.Sort_Length_Asc))
        sortList.add(mContext.getString(R.string.Sort_Length_Desc))

        val sortAdapter = ButtonAdapter(mContext, sortList)
        rootMD.sSortMD.adapter = sortAdapter
        //endregion
        //endregion

        //region Buttons
        rootMD.tvFilterMD.setOnClickListener {
            rootMD.sFilterMD.performClick()
        }

        rootMD.tvSortMD.setOnClickListener {
            rootMD.sSortMD.performClick()
        }

        rootMD.tvCamcelMD.setOnClickListener {
            onCancel?.invoke()
        }

        rootMD.tvApplyMD.setOnClickListener {
            currentFilterId = selectedFilterId
            currentSortId = selectedSortId

            StoreFun.writeInt(StoreFun.KEY_CURRENT_FILTER, currentFilterId)
            StoreFun.writeInt(StoreFun.KEY_CURRENT_SORT, currentSortId)

            onApply?.invoke()
        }
        //endregion
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (rootMD.sFilterMD.selectedItemPosition) {
            0 -> {
                selectedFilterId = ID_FILTER_ALL
                rootMD.tvFilterMD.text = mContext.getString(R.string.Filter_All)
            }
            1 -> {
                selectedFilterId = ID_FILTER_UNREAD
                rootMD.tvFilterMD.text = mContext.getString(R.string.Filter_Unread)
            }
        }

        when (rootMD.sSortMD.selectedItemPosition) {
            0 -> {
                selectedSortId = ID_SORT_DATE_NEW
                rootMD.tvSortMD.text = mContext.getString(R.string.Sort_Date_New)
            }
            1 -> {
                selectedSortId = ID_SORT_DATE_OLD
                rootMD.tvSortMD.text = mContext.getString(R.string.Sort_Date_Old)
            }
            2 -> {
                selectedSortId = ID_SORT_LENGTH_ASC
                rootMD.tvSortMD.text = mContext.getString(R.string.Sort_Length_Asc)
            }
            3 -> {
                selectedSortId = ID_SORT_LENGTH_DESC
                rootMD.tvSortMD.text = mContext.getString(R.string.Sort_Length_Desc)
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun show() {
        super.show()

        //region Filter/Sort Control
        when (currentFilterId) {
            ID_FILTER_ALL -> rootMD.sFilterMD.setSelection(0)
            ID_FILTER_UNREAD -> rootMD.sFilterMD.setSelection(1)
        }

        when (currentSortId) {
            ID_SORT_DATE_NEW -> rootMD.sSortMD.setSelection(0)
            ID_SORT_DATE_OLD -> rootMD.sSortMD.setSelection(1)
            ID_SORT_LENGTH_ASC -> rootMD.sSortMD.setSelection(2)
            ID_SORT_LENGTH_DESC -> rootMD.sSortMD.setSelection(3)
        }
        //endregion
    }
}

//PureBlack Software / Murat BIYIK