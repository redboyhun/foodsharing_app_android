package com.foodsharing.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.foodsharing.app.R
import com.foodsharing.app.data.api.ApiClient
import com.foodsharing.app.databinding.FragmentProfileBinding
import com.foodsharing.app.ui.auth.LoginActivity
import com.foodsharing.app.util.Resource
import com.foodsharing.app.util.gone
import com.foodsharing.app.util.toast
import com.foodsharing.app.util.visible

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMyBaskets.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_myBaskets)
        }

        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.action_profile_to_settings)
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }

        viewModel.profile.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> binding.progressBar.visible()
                is Resource.Success -> {
                    binding.progressBar.gone()
                    val profile = state.data
                    binding.tvName.text = profile.name
                    binding.tvEmail.text = profile.email ?: ""
                    binding.tvDescription.text = profile.description ?: ""
                    
                    val avatarUrl = if (!profile.avatar.isNullOrEmpty()) {
                        if (profile.avatar.startsWith("/")) {
                            ApiClient.baseUrl.removeSuffix("/") + profile.avatar
                        } else {
                            profile.avatar
                        }
                    } else {
                        null
                    }

                    Glide.with(this)
                        .load(avatarUrl)
                        .placeholder(R.drawable.ic_profile)
                        .error(R.drawable.ic_profile)
                        .into(binding.ivAvatar)
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    toast(state.message)
                }
            }
        }

        viewModel.logoutState.observe(viewLifecycleOwner) { state ->
            if (state is Resource.Success) {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            } else if (state is Resource.Error) {
                toast(state.message)
            }
        }

        viewModel.loadProfile()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
