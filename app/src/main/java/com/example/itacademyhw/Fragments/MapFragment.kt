package com.example.itacademyhw.Fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.example.itacademyhw.Extensions.applyInsetsWithAppBar
import com.example.itacademyhw.LocationService
import com.example.itacademyhw.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MapFragment : Fragment() {
    private var _binding: FragmentMapBinding? = null
    private val binding
        get() = requireNotNull(_binding) {
            "View was destroyed"
        }
    private var googleMap: GoogleMap? = null
    private var locationListener: LocationSource.OnLocationChangedListener? = null
    private val locationService by lazy {
        LocationService(requireContext())
    }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val permissionManager = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isEnabled ->
        if (isEnabled) {
            setLocationEnabled(isEnabled)

            viewLifecycleOwner.lifecycleScope.launch {
                locationService.getLocation()?.let(::moveCameraToLocation)
            }
            locationService
                .locationFlow
                .onEach { location ->
                    locationListener?.onLocationChanged(location)
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMapBinding.inflate(inflater, container, false)
            .also { binding ->
                _binding = binding
            }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetInclude.bottomSheet)
        setBottomSheetVisibility(false)

        permissionManager.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        binding.mapView.getMapAsync { googleMap ->

            googleMap.apply {
                uiSettings.isCompassEnabled = true
                uiSettings.isZoomControlsEnabled = true
                uiSettings.isMyLocationButtonEnabled = true
            }

            googleMap.isMyLocationEnabled =
                ContextCompat.checkSelfPermission(
                    view.context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

            googleMap.setLocationSource(object : LocationSource {
                override fun activate(listener: LocationSource.OnLocationChangedListener) {
                    locationListener = listener
                }

                override fun deactivate() {
                    locationListener = null
                }
            })

            val firstMarker = LatLng(53.53, 27.34)
            googleMap.addMarker(
                MarkerOptions()
                    .position(firstMarker)
                    .title("This is first marker")
            )

            googleMap.setOnMarkerClickListener { marker ->
                onMarkerClicked(marker)
                false
            }

            googleMap.setOnMapClickListener {
                setBottomSheetVisibility(false)
            }

            this.googleMap = googleMap
        }

        binding.mapView.onCreate(savedInstanceState)

        binding.appBar.applyInsetsWithAppBar { view, insets ->
            view.updatePadding(left = insets.left, right = insets.right, top = insets.top)
            insets
        }
    }

    override fun onResume() {
        binding.mapView.onResume()
        super.onResume()
    }

    override fun onPause() {
        binding.mapView.onPause()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        binding.mapView.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        binding.mapView.onDestroy()
        _binding = null
        super.onDestroyView()
    }

    @SuppressLint("MissingPermission")
    private fun setLocationEnabled(enabled: Boolean) {
        googleMap?.isMyLocationEnabled = enabled
        googleMap?.uiSettings?.isMyLocationButtonEnabled = enabled
    }

    private fun moveCameraToLocation(location: Location) {
        var currentLoc = LatLng(location.latitude, location.longitude)
        googleMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(currentLoc, 40f)
        )
    }

    private fun setBottomSheetVisibility(isVisible: Boolean) {
        val updatedState = if (isVisible) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.state = updatedState
        binding.bottomSheetInclude.bottomSheet.isVisible = isVisible
    }

    private fun onMarkerClicked(marker: Marker) {
        binding.bottomSheetInclude.cityName.text = marker.title
        binding.bottomSheetInclude.longitude.text = marker.position.longitude.toString()
        binding.bottomSheetInclude.latitude.text = marker.position.latitude.toString()

        setBottomSheetVisibility(true)
    }

}