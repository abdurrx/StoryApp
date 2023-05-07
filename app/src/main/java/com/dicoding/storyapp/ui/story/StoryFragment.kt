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
import com.dicoding.storyapp.data.response.story.Story
import com.dicoding.storyapp.data.source.local.UserPreferenceImpl
import com.dicoding.storyapp.databinding.FragmentStoryBinding
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
        fragmentStoryBinding.toolbar.title = "Stories"
        fragmentStoryBinding.toolbar.inflateMenu(R.menu.menu)
        fragmentStoryBinding.toolbar.menu

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
        storyAdapter = StoryAdapter(requireContext(), listOf<Story>())
        fragmentStoryBinding.rvStories.adapter = storyAdapter
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

                    storyAdapter.submitList(it.listStory)

                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()

                    showLoading(false)
                    fragmentStoryBinding.swipeRefreshLayout.isRefreshing = false
                }

                is ResultState.Error -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }

                else -> {
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