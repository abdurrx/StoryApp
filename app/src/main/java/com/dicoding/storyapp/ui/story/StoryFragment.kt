package com.dicoding.storyapp.ui.story

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.repository.RepositoryImpl
import com.dicoding.storyapp.data.source.local.UserPreferenceImpl
import com.dicoding.storyapp.databinding.FragmentStoryBinding
import com.dicoding.storyapp.ui.story.adapter.LoadingStateAdapter
import com.dicoding.storyapp.ui.story.adapter.StoryAdapter
import com.dicoding.storyapp.utils.ResultState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StoryFragment : Fragment() {
    private var _fragmentStoryBinding: FragmentStoryBinding? = null
    private val fragmentStoryBinding get() = _fragmentStoryBinding!!

    private val storyViewModel: StoryViewModel by viewModels()
    private lateinit var storyAdapter: StoryAdapter

    @Inject
    lateinit var userPreferenceImpl: UserPreferenceImpl

    @Inject
    lateinit var repositoryImpl: RepositoryImpl

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _fragmentStoryBinding = FragmentStoryBinding.inflate(inflater, container, false)
        return fragmentStoryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val token = userPreferenceImpl.getToken()
        if (token.isEmpty()) {
            val action = StoryFragmentDirections.actionStoryFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        // Button Menu
        fragmentStoryBinding.toolbar.title = getString(R.string.stories)
        fragmentStoryBinding.toolbar.inflateMenu(R.menu.menu)
        fragmentStoryBinding.toolbar.menu

        // Maps
        fragmentStoryBinding.toolbar.menu.findItem(R.id.menu_map).setOnMenuItemClickListener {
            val action = StoryFragmentDirections.actionStoryFragmentToMapsFragment()
            findNavController().navigate(action)
            true
        }

        // Logout
        fragmentStoryBinding.toolbar.menu.findItem(R.id.menu_logout).setOnMenuItemClickListener {
            repositoryImpl.deleteToken()

            val action = StoryFragmentDirections.actionStoryFragmentToLoginFragment()
            findNavController().navigate(action)
            true
        }

        // Floating Action Button
        fragmentStoryBinding.fabAdd.setOnClickListener {
            val action = StoryFragmentDirections.actionStoryFragmentToAddStoryFragment()
            it.findNavController().navigate(action)
        }

        // Recycler View
        fragmentStoryBinding.rvStories.layoutManager = LinearLayoutManager(requireContext())

        // Adapter
        storyAdapter = StoryAdapter(requireContext())
        fragmentStoryBinding.rvStories.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )

        storyViewModel.getAllStory()

        // Swipe Refresh
        fragmentStoryBinding.swipeRefreshLayout.setOnRefreshListener {
            Log.d("Refresh", "Refresh Data")
            val action = findNavController().currentDestination?.id

            action?.let {
                findNavController().navigate(it)
            }
        }

        // Result
        storyViewModel.result.observe(viewLifecycleOwner) { status ->
            when (status) {
                is ResultState.Success -> status.data?.let {
                    if(it.error) {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                        fragmentStoryBinding.progressBar.visibility = View.GONE
                    }

                    storyViewModel.getStoryPaging().observe(viewLifecycleOwner) {
                        storyAdapter.submitData(lifecycle, it)
                        val snapshot = storyAdapter.snapshot()
                        Log.d("Result", "storyResult: ${snapshot.size}")
                    }

                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()

                    showLoading(false)
                    fragmentStoryBinding.swipeRefreshLayout.isRefreshing = false
                }

                is ResultState.Error -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }

                is ResultState.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    showLoading(true)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        fragmentStoryBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}