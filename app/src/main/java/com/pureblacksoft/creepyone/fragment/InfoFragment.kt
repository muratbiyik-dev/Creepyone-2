package com.pureblacksoft.creepyone.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pureblacksoft.creepyone.R
import com.pureblacksoft.creepyone.activity.PrefActivity
import com.pureblacksoft.creepyone.databinding.FragmentInfoBinding
import kotlinx.android.synthetic.main.fragment_info.view.*

class InfoFragment : Fragment(R.layout.fragment_info)
{
    companion object {
        //region List Id
        const val ID_INFO_TERMS = 1
        const val ID_INFO_ABOUT = 2
        //endregion

        var currentInfoId = 0
    }

    private lateinit var mContext: Context
    private lateinit var mActivity: PrefActivity
    private lateinit var rootIF: View

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = requireContext()
        mActivity = requireActivity() as PrefActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_info, container, false)
        rootIF = FragmentInfoBinding.bind(view).root

        PrefActivity.currentFragmentId = PrefActivity.ID_FRAGMENT_INFO

        //region Info Control
        when (currentInfoId) {
            ID_INFO_TERMS -> {
                rootIF.tvTitleIF.text = getString(R.string.Terms_Title)
                rootIF.tvContentIF.text = getString(R.string.Terms_Content)
            }
            ID_INFO_ABOUT -> {
                rootIF.tvTitleIF.text = getString(R.string.About_Title)
                rootIF.tvContentIF.text = getString(R.string.About_Content)
            }
        }
        //endregion

        return view
    }
}

//PureBlack Software / Murat BIYIK