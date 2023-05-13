package com.dicoding.storyapp.ui.story.detail

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.FragmentDetailStoryBinding
import com.dicoding.storyapp.utils.AnimationUtil
import com.dicoding.storyapp.utils.ResultState
import com.dicoding.storyapp.utils.formatDateFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailStoryFragment : Fragment() {
    private var _fragmentDetailStoryBinding: FragmentDetailStoryBinding? = null
    private val fragmentDetailStoryBinding get() = _fragmentDetailStoryBinding!!

    private val detailStoryViewModel: DetailStoryViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _fragmentDetailStoryBinding = FragmentDetailStoryBinding.inflate(inflater, container, false)
        return fragmentDetailStoryBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get Detail from Arguments
        detailStoryViewModel.getBy(DetailStoryFragmentArgs.fromBundle(arguments as Bundle).id)

        // Button Menu
        fragmentDetailStoryBinding.toolbar.title = getString(R.string.detail_story)

        // Result
        detailStoryViewModel.result.observe(viewLifecycleOwner) { status ->
            when (status) {
                is ResultState.Success -> status.data?.let {
                    if(it.error) {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }

                    with(it.story) {
                        Glide.with(requireContext())
                            .load(photoUrl)
                            .into(fragmentDetailStoryBinding.ivPhoto)

                        fragmentDetailStoryBinding.tvDate.text = formatDateFormat(createdAt)
                        fragmentDetailStoryBinding.tvName.text = name
                        fragmentDetailStoryBinding.tvDesc.text = description
                    }

                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                }

                is ResultState.Error -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }

                is ResultState.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Animation
        AnimationUtil.playAnimation(
            fragmentDetailStoryBinding.ivPhoto,
            fragmentDetailStoryBinding.tvDate,
            fragmentDetailStoryBinding.tvName,
            fragmentDetailStoryBinding.tvDesc
        )
    }
}