package com.foodsharing.app.data.repository

import com.foodsharing.app.data.api.ApiClient
import com.foodsharing.app.data.model.*
import com.foodsharing.app.util.Resource
import com.foodsharing.app.util.httpErrorMessage

class ConversationRepository {

    suspend fun getConversations(): Resource<ConversationsResponse> {
        return try {
            val response = ApiClient.api.getConversations()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(httpErrorMessage(response.code()))
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    suspend fun getConversation(id: Int): Resource<ConversationDetailResponse> {
        return try {
            val response = ApiClient.api.getConversation(id)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(httpErrorMessage(response.code()))
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    suspend fun getMessages(conversationId: Int): Resource<List<ChatMessage>> {
        return try {
            val response = ApiClient.api.getMessages(conversationId)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!.messages)
            } else {
                Resource.Error(httpErrorMessage(response.code()))
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    suspend fun sendMessage(conversationId: Int, body: String): Resource<ChatMessage> {
        return try {
            val response = ApiClient.api.sendMessage(conversationId, SendMessageRequest(body))
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error(httpErrorMessage(response.code()))
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    suspend fun markRead(conversationId: Int): Resource<Unit> {
        return try {
            val response = ApiClient.api.markConversationRead(conversationId, ReadStatusRequest(true))
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(httpErrorMessage(response.code()))
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }
}
