package com.pureblacksoft.creepyone.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.pureblacksoft.creepyone.R
import com.pureblacksoft.creepyone.activity.StoryActivity
import com.pureblacksoft.creepyone.data.Story
import com.pureblacksoft.creepyone.databinding.CardStoryBinding
import com.pureblacksoft.creepyone.function.PrefFun
import kotlinx.android.synthetic.main.card_story.view.*
import java.util.*

class StoryAdapter(private val storyList: MutableList<Story>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>(), Filterable
{
    companion object {
        lateinit var accessedStory: Story
    }

    private lateinit var mContext: Context
    private var filteredStoryList = storyList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.card_story, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = filteredStoryList[position]
        holder.bind(story)
    }

    override fun getItemCount(): Int = filteredStoryList.size

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view)
    {
        private val rootSC = CardStoryBinding.bind(view).root

        fun bind(story: Story) {
            rootSC.tvTitleSC.text = story.title
            rootSC.tvContentSC.text = story.content
            rootSC.tvLengthSC.text = mContext.getString(R.string.Story_Length, story.length)

            if (story.readed == 1) {
                rootSC.tvTitleSC.setTextColor(mContext.getColor(R.color.Story_Title_Readed_TextColor))
            }
            else {
                rootSC.tvTitleSC.setTextColor(mContext.getColor(R.color.Story_Title_TextColor))
            }

            PrefFun.setStoryFont(rootSC.tvTitleSC, rootSC.tvContentSC, rootSC.tvLengthSC, rootSC.tvLengthLabelSC)

            view.setOnClickListener {
                accessedStory = filteredStoryList[adapterPosition]

                val intent = Intent(mContext, StoryActivity::class.java)
                mContext.startActivity(intent)
            }
        }
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val resultList = mutableListOf<Story>()
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    resultList.clear()
                }
                else {
                    for (story in storyList) {
                        if (story.title.toLowerCase(Locale.ROOT).contains(charSearch.toLowerCase(Locale.ROOT))) {
                            resultList.add(story)
                        }
                    }
                }
                filteredStoryList = resultList

                val filterResults = FilterResults()
                filterResults.values = filteredStoryList

                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredStoryList = results?.values as MutableList<Story>
                notifyDataSetChanged()
            }
        }
    }
}

//PureBlack Software / Murat BIYIK