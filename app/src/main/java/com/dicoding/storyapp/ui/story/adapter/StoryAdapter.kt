package com.dicoding.storyapp.ui.story.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyapp.data.response.story.Story
import com.dicoding.storyapp.databinding.ItemStoryBinding
import com.dicoding.storyapp.ui.story.StoryFragmentDirections

class StoryAdapter(private val context: Context) : PagingDataAdapter<Story, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    inner class ViewHolder(private val context: Context, private val itemStoryBinding: ItemStoryBinding) : RecyclerView.ViewHolder(itemStoryBinding.root) {
        fun bind(id: String, name: String, photoUrl: String) {
            with(itemStoryBinding) {
                tvItemName.text = name

                Glide.with(context)
                    .load(photoUrl)
                    .into(ivItemPhoto)

                cvStory.setOnClickListener {
                    navigateToDetail(id, it)
                }
            }
        }

        private fun navigateToDetail(id: String, view: View){
            val action = StoryFragmentDirections.actionStoryFragmentToDetailStoryFragment(id)
            view.findNavController().navigate(action)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemStoryBinding = ItemStoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(context, itemStoryBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val story = getItem(position)
        story?.let { viewHolder.bind(it.id, it.name, it.photoUrl) }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}