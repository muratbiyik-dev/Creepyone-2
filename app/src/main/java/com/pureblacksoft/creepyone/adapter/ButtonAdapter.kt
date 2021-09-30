package com.pureblacksoft.creepyone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.pureblacksoft.creepyone.R
import com.pureblacksoft.creepyone.databinding.CardButtonBinding
import kotlinx.android.synthetic.main.card_button.view.*

class ButtonAdapter(private val context: Context, private val stringList: MutableList<String>) : BaseAdapter()
{
    override fun getItemId(i: Int): Long = 0

    override fun getItem(i: Int): Any? = null

    override fun getCount(): Int = stringList.size

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val newView = inflater.inflate(R.layout.card_button, parent, false)
        val rootBC = CardButtonBinding.bind(newView).root
        rootBC.tvTitleBC.text = stringList[position]

        return newView
    }
}

//PureBlack Software / Murat BIYIK