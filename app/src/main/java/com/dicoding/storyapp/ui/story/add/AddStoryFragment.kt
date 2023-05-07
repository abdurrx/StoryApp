package com.dicoding.storyapp.ui.story.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import com.dicoding.storyapp.databinding.FragmentAddStoryBinding
import com.dicoding.storyapp.ui.MainActivity
import com.dicoding.storyapp.utils.AnimationUtil
import com.dicoding.storyapp.utils.ResultState
import com.dicoding.storyapp.utils.createCustomTempFile
import com.dicoding.storyapp.utils.reduceFileImage
import com.dicoding.storyapp.utils.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class AddStoryFragment : Fragment() {
    private var _fragmentAddStoryBinding: FragmentAddStoryBinding? = null
    private val fragmentAddStoryBinding get() = _fragmentAddStoryBinding!!

    private val addStoryViewModel: AddStoryViewModel by viewModels()

    private lateinit var currentPhotoPath: String

    private var getFile: File? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _fragmentAddStoryBinding = FragmentAddStoryBinding.inflate(inflater, container, false)
        return fragmentAddStoryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        // Button Menu
        fragmentAddStoryBinding.toolbar.title = "Add Story"

        fragmentAddStoryBinding.btnCamera.setOnClickListener { startCamera() }
        fragmentAddStoryBinding.btnGallery.setOnClickListener { startGallery() }
        fragmentAddStoryBinding.btnUpload.setOnClickListener { uploadImage() }

        // Result
        addStoryViewModel.result.observe(viewLifecycleOwner) { status ->
            showLoading(true)
            when (status) {
                is ResultState.Success -> status.data?.let {
                    if(it.error) {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                        showLoading(false)
                    }

                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    requireActivity().finish()
                    startActivity(intent)

                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }

                is ResultState.Error -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }

                else -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        }

        // Animation
        AnimationUtil.playAnimation(
            fragmentAddStoryBinding.ivPhoto,

            fragmentAddStoryBinding.btnCamera,
            fragmentAddStoryBinding.btnGallery,

            fragmentAddStoryBinding.etDescription,

            fragmentAddStoryBinding.btnUpload
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    requireContext(),
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            val myFile = File(currentPhotoPath)

            myFile.let { file ->
                // rotateFile(file)
                reduceFileImage(file)
                getFile = file
                fragmentAddStoryBinding.ivPhoto.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, requireContext())
                val reduce = reduceFileImage(myFile)

                getFile = reduce

                fragmentAddStoryBinding.ivPhoto.setImageURI(uri)
            }
        }
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)
        createCustomTempFile(requireContext()).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.dicoding.storyapp.ui.story.add",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadImage() {
        showLoading(true)
        if (getFile != null) {
            val desc = fragmentAddStoryBinding.etDescription.text.toString()
            if(desc.isEmpty()) {
                showLoading(false)
                fragmentAddStoryBinding.etDescription.error = "Deskripsi tidak boleh kosong!"
                fragmentAddStoryBinding.etDescription.requestFocus()
            }

            addStoryViewModel.addStory(desc, getFile as File)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        fragmentAddStoryBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}