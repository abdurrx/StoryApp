package com.dicoding.storyapp.data.source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.storyapp.data.repository.Repository
import com.dicoding.storyapp.data.response.story.Story
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class StoryPagingSource @Inject constructor(private val repository: Repository) : PagingSource<Int, Story>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX

            val response = repository.getStories(page = position).drop(1).first()

            val stories = response.data?.listStory ?: listOf()

            val prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1
            val nextKey = if (stories.isEmpty()) null else position + 1

            LoadResult.Page(
                data = stories,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}