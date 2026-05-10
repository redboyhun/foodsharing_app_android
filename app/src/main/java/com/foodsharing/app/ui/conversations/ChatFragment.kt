package com.foodsharing.app.ui.conversations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.foodsharing.app.databinding.FragmentChatBinding
import com.foodsharing.app.util.*
import com.foodsharing.app.util.Resource
import com.foodsharing.app.util.gone
import com.foodsharing.app.util.toast
import com.foodsharing.app.util.visible

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChatViewModel by viewModels()
    private val args: ChatFragmentArgs by navArgs()
    private lateinit var adapter: ChatAdapter
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        adapter = ChatAdapter(sessionManager.getUserId())
        binding.recyclerView.adapter = adapter

        binding.btnSend.setOnClickListener {
            val body = binding.etMessage.text.toString().trim()
            if (body.isEmpty()) return@setOnClickListener
            binding.etMessage.text?.clear()
            viewModel.sendMessage(body)
        }

        viewModel.conversationDetail.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> binding.progressBar.visible()
                is Resource.Success -> {
                    binding.progressBar.gone()
                    val conversation = state.data.conversation
                    val messages = conversation.messages?.sortedBy { it.sentAt } ?: emptyList()
                    val profiles = state.data.profiles ?: emptyList()
                    
                    adapter.setProfiles(profiles)
                    adapter.submitList(messages)
                    
                    if (messages.isNotEmpty()) {
                        binding.recyclerView.scrollToPosition(messages.size - 1)
                    }
                    
                    // Set title
                    val otherParticipant = profiles.find { 
                        it.id != sessionManager.getUserId() && conversation.members?.contains(it.id) == true 
                    }
                    requireActivity().title = conversation.title ?: otherParticipant?.name ?: "Chat"
                }
                is Resource.Error -> {
                    binding.progressBar.gone()
                    toast(state.message)
                }
            }
        }

        viewModel.sendState.observe(viewLifecycleOwner) { state ->
            if (state is Resource.Error) toast(state.message)
        }

        viewModel.loadConversation(args.conversationId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
