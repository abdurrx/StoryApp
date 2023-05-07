package com.dicoding.storyapp.ui.story.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyapp.data.response.story.Story
import com.dicoding.storyapp.databinding.ItemStoryBinding
import com.dicoding.storyapp.ui.story.StoryFragmentDirections

class StoryAdapter(private val context: Context, private var stories: List<Story>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {
    class ViewHolder(private val context: Context, private val itemStoryBinding: ItemStoryBinding) : RecyclerView.ViewHolder(itemStoryBinding.root) {
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

    fun submitList(newList: List<Story>){
        val diffResult = DiffUtil.calculateDiff(StoryDiffUtil(stories, newList))
        stories = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val itemStoryBinding = ItemStoryBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(context, itemStoryBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val story = stories[position]
        viewHolder.bind(story.id, story.name, story.photoUrl)
    }

    override fun getItemCount(): Int = stories.size
}

class StoryDiffUtil(
    private val oldList: List<Story>,
    private val newList: List<Story>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldData = oldList[oldItemPosition]
        val newData = newList[newItemPosition]
        return oldData.id == newData.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldData = oldList[oldItemPosition]
        val newData = newList[newItemPosition]
        return oldData == newData
    }
}