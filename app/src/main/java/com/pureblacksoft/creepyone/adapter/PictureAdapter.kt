package com.pureblacksoft.creepyone.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pureblacksoft.creepyone.R
import com.pureblacksoft.creepyone.activity.StoryActivity
import com.pureblacksoft.creepyone.data.Story
import com.pureblacksoft.creepyone.databinding.CardPictureBinding
import kotlinx.android.synthetic.main.card_picture.view.*

class PictureAdapter(private val pictureStoryList: MutableList<Story>) : RecyclerView.Adapter<PictureAdapter.ViewHolder>()
{
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.card_picture, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = pictureStoryList[position]
        holder.bind(story)
    }

    override fun getItemCount(): Int = pictureStoryList.size

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view)
    {
        private val rootPC = CardPictureBinding.bind(view).root

        fun bind(story: Story) {
            rootPC.tvTitlePC.text = story.title

            val image = story.image
            if (image != null) {
                val imageBitmap = BitmapFactory.decodeByteArray(image, 0, image.size)
                rootPC.ivImagePC.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, imageBitmap.width, imageBitmap.height, false))
            }

            if (story.readed == 1) {
                rootPC.tvTitlePC.setTextColor(mContext.getColor(R.color.Story_Title_Readed_TextColor))
            }
            else {
                rootPC.tvTitlePC.setTextColor(mContext.getColor(R.color.Story_Title_TextColor))
            }

            view.setOnClickListener {
                StoryAdapter.accessedStory = pictureStoryList[adapterPosition]

                val intent = Intent(mContext, StoryActivity::class.java)
                mContext.startActivity(intent)
            }
        }
    }
}

//PureBlack Software / Murat BIYIK