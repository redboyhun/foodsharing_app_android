package com.foodsharing.app.ui.conversations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foodsharing.app.data.model.ConversationsResponse
import com.foodsharing.app.data.repository.ConversationRepository
import com.foodsharing.app.util.Resource
import kotlinx.coroutines.launch

class ConversationsViewModel : ViewModel() {

    private val repository = ConversationRepository()

    private val _conversationsResponse = MutableLiveData<Resource<ConversationsResponse>>()
    val conversationsResponse: LiveData<Resource<ConversationsResponse>> = _conversationsResponse

    fun loadConversations() {
        viewModelScope.launch {
            _conversationsResponse.value = Resource.Loading
            _conversationsResponse.value = repository.getConversations()
        }
    }
}
