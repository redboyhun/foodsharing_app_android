package com.foodsharing.app.ui.conversations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsharing.app.data.model.ConversationDetailResponse
import com.foodsharing.app.data.model.Message
import com.foodsharing.app.data.repository.ConversationRepository
import com.foodsharing.app.util.Resource
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val repository = ConversationRepository()

    private val _conversationDetail = MutableLiveData<Resource<ConversationDetailResponse>>()
    val conversationDetail: LiveData<Resource<ConversationDetailResponse>> = _conversationDetail

    private val _sendState = MutableLiveData<Resource<Message>>()
    val sendState: LiveData<Resource<Message>> = _sendState

    var conversationId: Int = -1

    fun loadConversation(conversationId: Int) {
        this.conversationId = conversationId
        viewModelScope.launch {
            _conversationDetail.value = Resource.Loading
            _conversationDetail.value = repository.getConversation(conversationId)
            repository.markRead(conversationId)
        }
    }

    fun sendMessage(body: String) {
        if (conversationId == -1) return
        viewModelScope.launch {
            _sendState.value = Resource.Loading
            val result = repository.sendMessage(conversationId, body)
            _sendState.value = result
            if (result is Resource.Success) loadConversation(conversationId)
        }
    }
}
