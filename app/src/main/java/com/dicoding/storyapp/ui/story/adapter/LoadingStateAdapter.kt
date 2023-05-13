package com.dicoding.storyapp.ui.story.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.storyapp.databinding.ItemLoadingBinding

class LoadingStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingStateViewHolder {
        val itemLoadingBinding = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(itemLoadingBinding, retry)
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadingStateViewHolder(private val itemLoadingBinding: ItemLoadingBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(itemLoadingBinding.root) {

        init {
            itemLoadingBinding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                itemLoadingBinding.errorMsg.text = loadState.error.localizedMessage
            }

            itemLoadingBinding.progressBar.isVisible = loadState is LoadState.Loading
            itemLoadingBinding.retryButton.isVisible = loadState is LoadState.Error
            itemLoadingBinding.errorMsg.isVisible = loadState is LoadState.Error
        }
    }
}