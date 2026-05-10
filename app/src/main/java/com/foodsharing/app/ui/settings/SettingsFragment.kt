package com.foodsharing.app.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.foodsharing.app.R
import com.foodsharing.app.databinding.FragmentSettingsBinding
import com.foodsharing.app.util.SettingsManager
import com.foodsharing.app.util.toast

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels()

    private val intervalOptions = listOf(15, 30, 60)
    private val intervalLabels get() = intervalOptions.map { "$it min" }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinnerAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, intervalLabels
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerInterval.adapter = spinnerAdapter
        binding.tvIntervalNote.text = getString(R.string.settings_interval_note)

        viewModel.refreshInterval.observe(viewLifecycleOwner) { minutes ->
            val idx = intervalOptions.indexOfFirst { it >= minutes }.takeIf { it >= 0 } ?: 0
            binding.spinnerInterval.setSelection(idx)
        }

        viewModel.basketDistance.observe(viewLifecycleOwner) { distance ->
            if (binding.etBasketDistance.text.isNullOrEmpty()) {
                binding.etBasketDistance.setText(distance.toString())
            }
        }

        viewModel.notifyMessages.observe(viewLifecycleOwner) { binding.switchMessages.isChecked = it }
        viewModel.notifyBaskets.observe(viewLifecycleOwner) { binding.switchBaskets.isChecked = it }
        viewModel.notifyPickups.observe(viewLifecycleOwner) { binding.switchPickups.isChecked = it }

        viewModel.serverUrl.observe(viewLifecycleOwner) { url ->
            binding.rgServer.check(
                if (url.contains("beta")) binding.rbBeta.id else binding.rbProduction.id
            )
        }

        binding.btnSave.setOnClickListener {
            val selectedInterval = intervalOptions[binding.spinnerInterval.selectedItemPosition]
            val serverUrl = if (binding.rbBeta.isChecked) {
                SettingsManager.BETA_SERVER_URL
            } else {
                SettingsManager.DEFAULT_SERVER_URL
            }
            val distance = binding.etBasketDistance.text.toString().toIntOrNull() ?: SettingsManager.DEFAULT_BASKET_DISTANCE
            
            viewModel.saveSettings(
                selectedInterval,
                binding.switchMessages.isChecked,
                binding.switchBaskets.isChecked,
                binding.switchPickups.isChecked,
                serverUrl,
                distance
            )
        }

        viewModel.saved.observe(viewLifecycleOwner) { saved ->
            if (saved == true) {
                toast(getString(R.string.settings_saved))
                findNavController().navigateUp()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
