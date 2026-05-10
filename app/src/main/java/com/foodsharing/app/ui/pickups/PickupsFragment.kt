package com.foodsharing.app.ui.pickups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.foodsharing.app.databinding.FragmentPickupsBinding
import com.foodsharing.app.util.Resource
import com.foodsharing.app.util.gone
import com.foodsharing.app.util.toast
import com.foodsharing.app.util.visible

class PickupsFragment : Fragment() {

    private var _binding: FragmentPickupsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PickupsViewModel by viewModels()
    private lateinit var optionsAdapter: PickupOptionsAdapter
    private lateinit var registeredAdapter: RegisteredPickupsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPickupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        optionsAdapter = PickupOptionsAdapter { option ->
            viewModel.joinPickup(option.store.id, option.date)
        }
        binding.rvOptions.adapter = optionsAdapter

        registeredAdapter = RegisteredPickupsAdapter { pickup ->
            viewModel.leavePickup(pickup.store.id, pickup.date)
        }
        binding.rvRegistered.adapter = registeredAdapter

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadPickupOptions()
            viewModel.loadRegisteredPickups()
        }

        viewModel.options.observe(viewLifecycleOwner) { state ->
            binding.swipeRefresh.isRefreshing = false
            when (state) {
                is Resource.Loading -> binding.progressBar.visible()
                is Resource.Success -> {
                    binding.progressBar.gone()
                    optionsAdapter.submitList(state.data)
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    toast(state.message)
                }
            }
        }

        viewModel.registered.observe(viewLifecycleOwner) { state ->
            if (state is Resource.Success) registeredAdapter.submitList(state.data)
        }

        viewModel.joinState.observe(viewLifecycleOwner) { state ->
            if (state is Resource.Error) toast(state.message)
        }

        viewModel.loadPickupOptions()
        viewModel.loadRegisteredPickups()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
