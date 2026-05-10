package com.foodsharing.app.ui.baskets

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.foodsharing.app.R
import com.foodsharing.app.data.api.ApiClient
import com.foodsharing.app.databinding.FragmentBasketDetailBinding
import com.foodsharing.app.util.Resource
import com.foodsharing.app.util.gone
import com.foodsharing.app.util.toast
import com.foodsharing.app.util.visible

class BasketDetailFragment : Fragment() {

    private var _binding: FragmentBasketDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BasketDetailViewModel by viewModels()
    private val args: BasketDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBasketDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.basket.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> binding.progressBar.visible()
                is Resource.Success -> {
                    binding.progressBar.gone()
                    val basket = state.data
                    binding.tvDescription.text = basket.description
                    binding.tvCreator.text = basket.creator?.name ?: ""
                    binding.tvDistance.text = basket.distance?.let { "%.1f km".format(it) } ?: ""

                    val imageUrl = when {
                        !basket.picture.isNullOrEmpty() -> basket.picture
                        !basket.pictures.isNullOrEmpty() -> {
                            val path = basket.pictures[0]
                            if (path.startsWith("/")) {
                                ApiClient.baseUrl.removeSuffix("/") + path
                            } else {
                                path
                            }
                        }
                        else -> null
                    }

                    if (imageUrl != null) {
                        Glide.with(this).load(imageUrl).into(binding.ivBasketPicture)
                        binding.ivBasketPicture.setOnClickListener { showFullScreenImage(imageUrl) }
                    } else {
                        binding.ivBasketPicture.setOnClickListener(null)
                    }

                    binding.btnRequest.setOnClickListener {
                        val message = binding.etMessage.text.toString().trim()
                        if (message.isEmpty()) {
                            binding.tilMessage.error = getString(R.string.error_message_required)
                        } else {
                            binding.tilMessage.error = null
                            viewModel.requestBasket(basket.id, message)
                        }
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    toast(state.message)
                }
            }
        }

        viewModel.requestState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Success -> {
                    toast(getString(R.string.basket_request_sent))
                    findNavController().navigateUp()
                }
                is Resource.Error -> toast(state.message)
                else -> {}
            }
        }

        viewModel.loadBasket(args.basketId)
    }

    private fun showFullScreenImage(url: String) {
        val dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val imageView = ImageView(requireContext()).apply {
            scaleType = ImageView.ScaleType.FIT_CENTER
            setOnClickListener { dialog.dismiss() }
        }
        dialog.setContentView(imageView)
        Glide.with(this).load(url).into(imageView)
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
