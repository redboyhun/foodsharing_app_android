package com.foodsharing.app.ui.conversations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.foodsharing.app.databinding.FragmentConversationsBinding
import com.foodsharing.app.util.*
import com.foodsharing.app.util.Resource
import com.foodsharing.app.util.gone
import com.foodsharing.app.util.toast
import com.foodsharing.app.util.visible

class ConversationsFragment : Fragment() {

    private var _binding: FragmentConversationsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ConversationsViewModel by viewModels()
    private lateinit var adapter: ConversationsAdapter
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentConversationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        val currentUserId = sessionManager.getUserId()

        adapter = ConversationsAdapter(currentUserId) { conversation ->
            val action = ConversationsFragmentDirections.actionConversationsToChat(conversation.id)
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = adapter
        binding.swipeRefresh.setOnRefreshListener { viewModel.loadConversations() }

        viewModel.conversationsResponse.observe(viewLifecycleOwner) { state ->
            binding.swipeRefresh.isRefreshing = false
            when (state) {
                is Resource.Loading -> binding.progressBar.visible()
                is Resource.Success -> {
                    binding.progressBar.gone()
                    val conversations = state.data.conversations
                    val profiles = state.data.profiles ?: emptyList()
                    
                    binding.emptyView.visibility = if (conversations.isEmpty()) View.VISIBLE else View.GONE
                    
                    // Map data to UI models for efficient DiffUtil calculation
                    val uiModels = conversations.map { conversation ->
                        var title = conversation.title
                        if (title.isNullOrBlank()) {
                            val otherMemberId = conversation.members?.find { it != currentUserId }
                            title = profiles.find { it.id == otherMemberId }?.name
                        }
                        ConversationUiModel(
                            conversation = conversation,
                            displayTitle = title ?: "Conversation"
                        )
                    }
                    adapter.submitList(uiModels)
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    toast(state.message)
                }
            }
        }

        viewModel.loadConversations()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
