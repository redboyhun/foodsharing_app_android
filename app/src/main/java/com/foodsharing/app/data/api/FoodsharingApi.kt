package com.foodsharing.app.data.api

import com.foodsharing.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface FoodsharingApi {

    // Auth
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<Unit>

    @POST("api/logout")
    suspend fun logout(): Response<Unit>

    // Current user
    @GET("api/users/current/details")
    suspend fun getCurrentUser(): Response<UserDetails>

    // Baskets
    @GET("api/baskets/nearby")
    suspend fun getNearbyBaskets(
        @Query("distance") distance: Int = 3
    ): Response<List<Basket>>

    @GET("api/users/current/baskets")
    suspend fun getMyBaskets(): Response<List<Basket>>

    @GET("api/baskets/{basketId}")
    suspend fun getBasket(@Path("basketId") basketId: Int): Response<Basket>

    @POST("api/baskets")
    suspend fun createBasket(@Body request: CreateBasketRequest): Response<Basket>

    @PATCH("api/baskets/{basketId}")
    suspend fun updateBasket(
        @Path("basketId") basketId: Int,
        @Body request: UpdateBasketRequest
    ): Response<Basket>

    @DELETE("api/baskets/{basketId}")
    suspend fun deleteBasket(@Path("basketId") basketId: Int): Response<Unit>

    @POST("api/baskets/{basketId}/requests")
    suspend fun requestBasket(
        @Path("basketId") basketId: Int,
        @Body message: OptionalMessage
    ): Response<Unit>

    @DELETE("api/baskets/{basketId}/requests")
    suspend fun cancelBasketRequest(@Path("basketId") basketId: Int): Response<Unit>

    // Conversations
    @GET("api/conversations")
    suspend fun getConversations(): Response<ConversationsResponse>

    @GET("api/conversations/{id}")
    suspend fun getConversation(@Path("id") id: Int): Response<ConversationDetailResponse>

    @GET("api/conversations/{id}/messages")
    suspend fun getMessages(@Path("id") id: Int): Response<MessageCollection>

    @POST("api/conversations/{id}/messages")
    suspend fun sendMessage(
        @Path("id") id: Int,
        @Body message: SendMessageRequest
    ): Response<Message>

    @PUT("api/conversations/{id}/read-status")
    suspend fun markConversationRead(
        @Path("id") id: Int,
        @Query("isRead") isRead: Boolean
    ): Response<Unit>

    // Pickups
    @GET("api/users/current/pickups/options")
    suspend fun getPickupOptions(): Response<List<PickupOption>>

    @GET("api/users/{userId}/pickups/registered")
    suspend fun getRegisteredPickups(@Path("userId") userId: Int): Response<List<PickupOption>>

    @GET("api/users/{userId}/pickups/history")
    suspend fun getPickupHistory(@Path("userId") userId: Int): Response<List<PickupOption>>

    @POST("api/stores/{storeId}/pickups/{pickupDate}/users/current")
    suspend fun joinPickup(
        @Path("storeId") storeId: Int,
        @Path("pickupDate") pickupDate: String //pickup date should be UTC like 2026-06-16T08:00:00.000Z
    ): Response<Unit>

    @DELETE("api/stores/{storeId}/pickups/{pickupDate}/users/{userId}")
    suspend fun leavePickup(
        @Path("storeId") storeId: Int,
        @Path("pickupDate") pickupDate: String,
        @Path("userId") userId: Int
    ): Response<Unit>

    // Stores
    @GET("api/users/{userId}/stores")
    suspend fun getUserStores(@Path("userId") userId: Int): Response<List<StoreInfo>>
}
