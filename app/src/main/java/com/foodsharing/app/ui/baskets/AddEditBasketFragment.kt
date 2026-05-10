package com.foodsharing.app.ui.baskets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.foodsharing.app.databinding.FragmentAddEditBasketBinding
import com.foodsharing.app.util.Resource
import com.foodsharing.app.util.toast

class AddEditBasketFragment : Fragment() {

    private var _binding: FragmentAddEditBasketBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddEditBasketViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddEditBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSave.setOnClickListener {
            val description = binding.etDescription.text.toString().trim()
            if (description.isEmpty()) {
                toast("Please enter a description")
                return@setOnClickListener
            }
            
            val lifetime = binding.etLifetime.text.toString().toIntOrNull() ?: 2
            val weight = binding.etWeight.text.toString().toIntOrNull() ?: 2000

            viewModel.createBasket(
                description = description,
                lifetimeInDays = lifetime,
                weightInGrams = weight
            )
        }

        viewModel.saveState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Success -> {
                    toast("Basket created!")
                    findNavController().navigateUp()
                }
                is Resource.Error -> {
                    toast(state.message)
                    binding.btnSave.isEnabled = true
                }
                is Resource.Loading -> binding.btnSave.isEnabled = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
