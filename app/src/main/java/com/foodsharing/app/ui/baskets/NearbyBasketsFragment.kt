package com.foodsharing.app.ui.baskets

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.foodsharing.app.R
import com.foodsharing.app.databinding.FragmentNearbyBasketsBinding
import com.foodsharing.app.util.Resource
import com.foodsharing.app.util.gone
import com.foodsharing.app.util.toast
import com.foodsharing.app.util.visible
import com.google.android.gms.location.LocationServices

class NearbyBasketsFragment : Fragment() {

    private var _binding: FragmentNearbyBasketsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NearbyBasketsViewModel by viewModels()
    private lateinit var adapter: BasketAdapter

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { grants ->
        if (grants.values.any { it }) fetchLocation() else loadWithDefaultLocation()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNearbyBasketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BasketAdapter(onItemClick = { basket ->
            val action = NearbyBasketsFragmentDirections.actionNearbyBasketsToDetail(basket.id)
            findNavController().navigate(action)
        })
        binding.recyclerView.adapter = adapter

        binding.swipeRefresh.setOnRefreshListener { checkLocationAndLoad() }

        viewModel.baskets.observe(viewLifecycleOwner) { state ->
            binding.swipeRefresh.isRefreshing = false
            when (state) {
                is Resource.Loading -> binding.progressBar.visible()
                is Resource.Success -> {
                    binding.progressBar.gone()
                    binding.emptyView.visibility = if (state.data.isEmpty()) View.VISIBLE else View.GONE
                    adapter.submitList(state.data)
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    toast(state.message)
                }
            }
        }

        checkLocationAndLoad()
    }

    private fun checkLocationAndLoad() {
        val fine = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
        if (fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED) {
            fetchLocation()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }
    }

    private fun fetchLocation() {
        try {
            val fusedClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    viewModel.loadNearbyBaskets(location.latitude, location.longitude)
                } else {
                    loadWithDefaultLocation()
                }
            }.addOnFailureListener {
                loadWithDefaultLocation()
            }
        } catch (e: SecurityException) {
            loadWithDefaultLocation()
        }
    }

    private fun loadWithDefaultLocation() {
        // Berlin as default
        viewModel.loadNearbyBaskets(52.520008, 13.404954)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
