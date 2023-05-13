package com.dicoding.storyapp.ui.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.storyapp.data.repository.RepositoryImpl
import com.dicoding.storyapp.data.response.story.Story
import com.dicoding.storyapp.ui.story.adapter.StoryAdapter
import com.dicoding.storyapp.utils.DataDummy
import com.dicoding.storyapp.utils.MainDispatcherRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var repositoryImpl: RepositoryImpl

    private var dummyStory = DataDummy.generateDummyStories()
    private val expectedStory = MutableLiveData<PagingData<Story>>()

    private lateinit var storyViewModel: StoryViewModel

    @Before
    fun setup(){
        storyViewModel = StoryViewModel(repositoryImpl)
    }

    @Test
    fun `when Get Story Should Not Null and Return Success`() = runTest {
        val data: PagingData<Story> = StoriesPagingSource.snapshot(dummyStory)

        expectedStory.value = data
        Mockito.`when`(repositoryImpl.getStoriesPaging()).thenReturn(expectedStory)

        val actualStory: PagingData<Story> = runBlocking {
            storyViewModel.getStoryPaging().asFlow().first()
        }

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story amount of data is matching as expected`() = runTest {
        val data: PagingData<Story> = StoriesPagingSource.snapshot(dummyStory)

        expectedStory.value = data
        Mockito.`when`(repositoryImpl.getStoriesPaging()).thenReturn(expectedStory)

        val actualStory: PagingData<Story> = runBlocking {
            storyViewModel.getStoryPaging().asFlow().first()
        }

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
    }

    @Test
    fun `when Get Story returns first data successfully`() = runTest {
        val data: PagingData<Story> = StoriesPagingSource.snapshot(dummyStory)

        expectedStory.value = data
        Mockito.`when`(repositoryImpl.getStoriesPaging()).thenReturn(expectedStory)

        val actualStory: PagingData<Story> = runBlocking {
            storyViewModel.getStoryPaging().asFlow().first()
        }

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Should Return Zero If No Data`() = runTest {
        dummyStory = listOf<Story>()
        val data: PagingData<Story> = StoriesPagingSource.snapshot(dummyStory)

        expectedStory.value = data
        Mockito.`when`(repositoryImpl.getStoriesPaging()).thenReturn(expectedStory)

        val actualStory: PagingData<Story> = runBlocking {
            storyViewModel.getStoryPaging().asFlow().first()
        }

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(0, differ.snapshot().items.size)
    }

}

class StoriesPagingSource : PagingSource<Int, LiveData<List<Story>>>() {
    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
