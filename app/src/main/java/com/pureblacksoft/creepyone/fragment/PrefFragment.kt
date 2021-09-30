package com.pureblacksoft.creepyone.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.pureblacksoft.creepyone.R
import com.pureblacksoft.creepyone.activity.PrefActivity
import com.pureblacksoft.creepyone.databinding.FragmentPrefBinding
import com.pureblacksoft.creepyone.function.PrefFun
import com.pureblacksoft.creepyone.function.StoreFun
import kotlinx.android.synthetic.main.fragment_pref.view.*

class PrefFragment : Fragment(R.layout.fragment_pref), AdapterView.OnItemSelectedListener
{
    private lateinit var mContext: Context
    private lateinit var mActivity: PrefActivity
    private lateinit var rootPF: View

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = requireContext()
        mActivity = requireActivity() as PrefActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pref, container, false)
        rootPF = FragmentPrefBinding.bind(view).root

        PrefActivity.currentFragmentId = PrefActivity.ID_FRAGMENT_PREF

        //region Spinners
        //region Font Spinner
        rootPF.sFontPF.onItemSelectedListener = this

        val fontList = mutableListOf<String>()
        fontList.add(getString(R.string.Font_Arial))
        fontList.add(getString(R.string.Font_Georgia))
        fontList.add(getString(R.string.Font_Comfortaa))
        fontList.add(getString(R.string.Font_Montserrat))

        val fontAdapter = ArrayAdapter(mContext, R.layout.card_selection, fontList)
        rootPF.sFontPF.adapter = fontAdapter

        when (PrefFun.currentFont) {
            PrefFun.ID_FONT_ARIAL -> rootPF.sFontPF.setSelection(0)
            PrefFun.ID_FONT_GEORGIA -> rootPF.sFontPF.setSelection(1)
            PrefFun.ID_FONT_COMFORTAA -> rootPF.sFontPF.setSelection(2)
            PrefFun.ID_FONT_MONTSERRAT -> rootPF.sFontPF.setSelection(3)
        }
        //endregion

        //region Font Size Spinner
        rootPF.sFontSizePF.onItemSelectedListener = this

        val fontSizeList = mutableListOf<String>()
        fontSizeList.add(getString(R.string.Font_Size_S))
        fontSizeList.add(getString(R.string.Font_Size_M))
        fontSizeList.add(getString(R.string.Font_Size_L))
        fontSizeList.add(getString(R.string.Font_Size_XL))

        val fontSizeAdapter = ArrayAdapter(mContext, R.layout.card_selection, fontSizeList)
        rootPF.sFontSizePF.adapter = fontSizeAdapter

        when (PrefFun.currentFontSize) {
            PrefFun.ID_FONT_SIZE_S -> rootPF.sFontSizePF.setSelection(0)
            PrefFun.ID_FONT_SIZE_M -> rootPF.sFontSizePF.setSelection(1)
            PrefFun.ID_FONT_SIZE_L -> rootPF.sFontSizePF.setSelection(2)
            PrefFun.ID_FONT_SIZE_XL -> rootPF.sFontSizePF.setSelection(3)
        }
        //endregion
        //endregion

        //region Buttons
        rootPF.tvTermsPF.setOnClickListener {
            InfoFragment.currentInfoId = InfoFragment.ID_INFO_TERMS
            mActivity.comitFragment(InfoFragment())
        }

        rootPF.tvAboutPF.setOnClickListener {
            InfoFragment.currentInfoId = InfoFragment.ID_INFO_ABOUT
            mActivity.comitFragment(InfoFragment())
        }
        //endregion

        return view
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (rootPF.sFontPF.selectedItemPosition) {
            0 -> PrefFun.currentFont = PrefFun.ID_FONT_ARIAL
            1 -> PrefFun.currentFont = PrefFun.ID_FONT_GEORGIA
            2 -> PrefFun.currentFont = PrefFun.ID_FONT_COMFORTAA
            3 -> PrefFun.currentFont = PrefFun.ID_FONT_MONTSERRAT
        }

        when (rootPF.sFontSizePF.selectedItemPosition) {
            0 -> PrefFun.currentFontSize = PrefFun.ID_FONT_SIZE_S
            1 -> PrefFun.currentFontSize = PrefFun.ID_FONT_SIZE_M
            2 -> PrefFun.currentFontSize = PrefFun.ID_FONT_SIZE_L
            3 -> PrefFun.currentFontSize = PrefFun.ID_FONT_SIZE_XL
        }

        StoreFun.writeInt(StoreFun.KEY_CURRENT_FONT, PrefFun.currentFont)
        StoreFun.writeInt(StoreFun.KEY_CURRENT_FONT_SIZE, PrefFun.currentFontSize)

        PrefFun.setStoryFont(rootPF.tvTitlePF, rootPF.tvContentPF, rootPF.tvLengthPF, rootPF.tvLengthLabelPF)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}

//PureBlack Software / Murat BIYIK