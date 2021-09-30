package com.pureblacksoft.creepyone.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pureblacksoft.creepyone.R
import com.pureblacksoft.creepyone.activity.GalleryActivity
import com.pureblacksoft.creepyone.activity.MainActivity
import com.pureblacksoft.creepyone.activity.PrefActivity
import com.pureblacksoft.creepyone.databinding.FragmentArchiveBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_archive.view.*

class ArchiveFragment : Fragment(R.layout.fragment_archive)
{
    private lateinit var mContext: Context
    private lateinit var mActivity: MainActivity
    private lateinit var rootAF: View

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = requireContext()
        mActivity = requireActivity() as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_archive, container, false)
        rootAF = FragmentArchiveBinding.bind(view).root

        MainActivity.currentFragmentId = MainActivity.ID_FRAGMENT_ARCHIVE

        //region Toolbar
        mActivity.ivIconMTB.setImageResource(R.drawable.ic_archive_bwhite)
        mActivity.tvTitleMTB.text = getString(R.string.Archive_Title)
        mActivity.ivButtonMTB.setImageResource(R.drawable.ic_pref_bwhite)
        //endregion

        //region Buttons
        mActivity.ivButtonMTB.setOnClickListener {
            val intent = Intent(mContext, PrefActivity::class.java)
            mContext.startActivity(intent)
        }

        rootAF.llEncyclopediaAF.setOnClickListener {}

        rootAF.llGalleryAF.setOnClickListener {
            val intent = Intent(mContext, GalleryActivity::class.java)
            mContext.startActivity(intent)
        }
        //endregion

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()

        //region Toolbar
        mActivity.ivButtonMTB.setImageResource(0)
        //endregion
    }
}

//PureBlack Software / Murat BIYIK