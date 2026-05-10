package com.foodsharing.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    val email: String,
    val password: String,
    val remember_me: Boolean = false
)

@JsonClass(generateAdapter = true)
data class GeoLocation(
    val lat: Double,
    val lon: Double
)

@JsonClass(generateAdapter = true)
data class Profile(
    val id: Int,
    val name: String,
    val avatar: String? = null,
    val email: String? = null,
    val description: String? = null,
    val address: String? = null,
    val postcode: String? = null,
    val city: String? = null,
    val lat: Double? = null,
    val lon: Double? = null,
    @Json(name = "isSleeping") val isSleeping: Boolean? = null
)

@JsonClass(generateAdapter = true)
data class CurrentUserResponse(
    val id: Int,
    val name: String,
    val avatar: String? = null,
    val email: String? = null
)

@JsonClass(generateAdapter = true)
data class Basket(
    val id: Int,
    val description: String,
    @Json(name = "contactTypes") val contactTypes: List<Int>? = null,
    val lat: Double? = null,
    val lon: Double? = null,
    val location: GeoLocation? = null,
    val picture: String? = null,
    val pictures: List<String>? = null,
    val status: Int? = null,
    @Json(name = "createdAt") val createdAt: String? = null,
    @Json(name = "updatedAt") val updatedAt: String? = null,
    @Json(name = "foodType") val foodType: Int? = null,
    @Json(name = "lifetimeInDays") val lifetimeInDays: Int? = null,
    val weight: Double? = null,
    @Json(name = "requestCount") val requestCount: Int? = null,
    val creator: BasketCreator? = null,
    val distance: Double? = null,
    @Json(name = "distanceInKm") val distanceInKm: Double? = null,
    val until: Long? = null
)

@JsonClass(generateAdapter = true)
data class BasketCreator(
    val id: Int,
    val name: String,
    val avatar: String? = null,
    @Json(name = "isSleeping") val isSleeping: Boolean? = null
)

@JsonClass(generateAdapter = true)
data class BasketsResponse(
    val baskets: List<Basket>
)

@JsonClass(generateAdapter = true)
data class BasketResponse(
    val basket: Basket
)

@JsonClass(generateAdapter = true)
data class CreateBasketRequest(
    val description: String,
    @Json(name = "contactTypes") val contactTypes: List<Int> = listOf(1),
    val lat: Double = null,
    val lon: Double = null,
    // @Json(name = "foodType") val foodType: Int? = null,
    @Json(name = "lifetimeInDays") val lifetimeInDays: Int = 1,
    val weightInGrams: Int = 2000,
    val pictures: List<String> = emptyList()
)

@JsonClass(generateAdapter = true)
data class UpdateBasketRequest(
    val description: String? = null,
    @Json(name = "contactTypes") val contactTypes: List<Int>? = null,
    val lat: Double? = null,
    val lon: Double? = null,
    @Json(name = "foodType") val foodType: Int? = null,
    @Json(name = "lifetimeInDays") val lifetimeInDays: Int? = null,
    val weight: Double? = null
)

@JsonClass(generateAdapter = true)
data class Conversation(
    val id: Int,
    val title: String? = null,
    @Json(name = "lastMessage") val lastMessage: ChatMessage? = null,
    @Json(name = "unreadMessages") val unreadMessages: Int = 0,
    val members: List<Int>? = null,
    @Json(name = "storeId") val storeId: Int? = null,
    val messages: List<ChatMessage>? = null
)

@JsonClass(generateAdapter = true)
data class ConversationMember(
    val id: Int,
    val name: String,
    val avatar: String? = null
)

@JsonClass(generateAdapter = true)
data class ConversationsResponse(
    val conversations: List<Conversation>,
    val profiles: List<Profile>? = null
)

@JsonClass(generateAdapter = true)
data class ConversationDetailResponse(
    val conversation: Conversation,
    val profiles: List<Profile>? = null
)

@JsonClass(generateAdapter = true)
data class ChatMessage(
    val id: Int? = null,
    val body: String,
    @Json(name = "authorId") val authorId: Int? = null,
    @Json(name = "authorName") val authorName: String? = null,
    @Json(name = "authorAvatar") val authorAvatar: String? = null,
    @Json(name = "sentAt") val sentAt: String? = null,
    @Json(name = "isRead") val isRead: Boolean = false
)

@JsonClass(generateAdapter = true)
data class MessageCollection(
    val messages: List<ChatMessage>
)

@JsonClass(generateAdapter = true)
data class SendMessageRequest(
    val body: String
)

@JsonClass(generateAdapter = true)
data class PickupUser(
    val id: Int,
    val name: String,
    val avatar: String? = null
)

@JsonClass(generateAdapter = true)
data class PickupOption(
    val date: String,
    val store: StoreInfo,
    val slots: Int,
    val occupiedSlots: List<PickupUser> = emptyList(),
    val isConfirmed: Boolean? = null,
    val description: String? = null
)

@JsonClass(generateAdapter = true)
data class PickupOptionsResponse(
    val pickups: List<PickupOption>
)

@JsonClass(generateAdapter = true)
data class RegisteredPickup(
    val date: String,
    val store: StoreInfo,
    val slots: Int,
    val occupiedSlots: List<PickupUser> = emptyList(),
    val isConfirmed: Boolean? = null,
    val description: String? = null
)

@JsonClass(generateAdapter = true)
data class RegisteredPickupsResponse(
    val pickups: List<RegisteredPickup>
)

@JsonClass(generateAdapter = true)
data class StoreInfo(
    val id: Int,
    val name: String,
    val address: String? = null,
    val lat: Double? = null,
    val lon: Double? = null
)

@JsonClass(generateAdapter = true)
data class StoresResponse(
    val stores: List<StoreInfo>
)

@JsonClass(generateAdapter = true)
data class IDList(
    val ids: List<Int>
)

@JsonClass(generateAdapter = true)
data class OptionalMessage(
    val message: String? = null
)

@JsonClass(generateAdapter = true)
data class ReadStatusRequest(
    @Json(name = "isRead") val isRead: Boolean
)
