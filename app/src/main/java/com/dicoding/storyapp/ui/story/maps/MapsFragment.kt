package com.dicoding.storyapp.ui.story.maps

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.response.story.Story
import com.dicoding.storyapp.databinding.FragmentMapsBinding
import com.dicoding.storyapp.utils.ResultState

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment() {
    private var _fragmentMapsBinding: FragmentMapsBinding? = null
    private val fragmentMapsBinding get() = _fragmentMapsBinding!!

    private val mapsViewModel: MapsViewModel by viewModels()

    private lateinit var mMap: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _fragmentMapsBinding = FragmentMapsBinding.inflate(inflater, container, false)
        return fragmentMapsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(callback)

        mapsViewModel.getStoryMap()

        // Result
        mapsViewModel.result.observe(viewLifecycleOwner) { status ->
            when (status) {
                is ResultState.Success -> status.data?.let {
                    if(it.error) {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }

                    getMyLocation()
                    setMapStyle()

                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                    mapsCallbackFactory(it.listStory)
                }

                is ResultState.Error -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                }

                is ResultState.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun mapsCallbackFactory(stories: List<Story>){
        stories.forEach {
            Log.d("storyMaps", "mapsCallbackFactory: $it")
            it.lat?.let { lat ->
                it.lon?.let { lon ->
                    Log.d("storyMaps", "mapsCallbackFactory: ${it.lat} & ${it.lon}")
                    val coordinate = LatLng(lat.toDouble(), lon.toDouble())
                    mMap.addMarker(MarkerOptions().position(coordinate).title(it.name))?.tag = it.id
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate))
                }
            }
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private companion object {
        private const val TAG = "TAG"
    }
}