package com.foodsharing.app.ui.baskets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.foodsharing.app.R
import com.foodsharing.app.databinding.FragmentMyBasketsBinding
import com.foodsharing.app.util.Resource
import com.foodsharing.app.util.gone
import com.foodsharing.app.util.toast
import com.foodsharing.app.util.visible

class MyBasketsFragment : Fragment() {

    private var _binding: FragmentMyBasketsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyBasketsViewModel by viewModels()
    private lateinit var adapter: BasketAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMyBasketsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BasketAdapter(
            onItemClick = { basket ->
                val action = MyBasketsFragmentDirections.actionMyBasketsToDetail(basket.id)
                findNavController().navigate(action)
            },
            onLongClick = { basket ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Delete basket?")
                    .setMessage(basket.description.take(60))
                    .setPositiveButton("Delete") { _, _ -> viewModel.deleteBasket(basket.id) }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        )
        binding.recyclerView.adapter = adapter

        binding.fabAddBasket.setOnClickListener {
            findNavController().navigate(R.id.action_myBaskets_to_addEditBasket)
        }

        binding.swipeRefresh.setOnRefreshListener { viewModel.loadMyBaskets() }

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

        viewModel.deleteState.observe(viewLifecycleOwner) { state ->
            if (state is Resource.Error) toast(state.message)
        }

        viewModel.loadMyBaskets()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
