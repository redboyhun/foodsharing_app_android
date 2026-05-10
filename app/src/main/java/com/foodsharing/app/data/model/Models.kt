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
data class UserDetails(
    val id: Int,
    val firstname: String,
    val lastname: String,
    val photo: String? = null,
    val email: String? = null,
    val aboutMePublic: String? = null,
    val address: String? = null,
    val postcode: String? = null,
    val city: String? = null,
    val coordinates: GeoLocation? = null,
    @Json(name = "isSleeping") val isSleeping: Boolean? = null,
    val foodsaver: Boolean? = null,
    val isVerified: Boolean? = null,
    val regionId: Int? = null,
    val regionName: String? = null,
    val role: Int? = null,
    val gender: Int? = null,
    val mobile: String? = null,
    val landline: String? = null,
    val birthday: String? = null,
    val aboutMeIntern: String? = null,
    val position: String? = null,
    val regions: List<String>? = null,
    val groups: List<String>? = null,
    val mailboxId: Int? = null
) {
    val displayName: String get() = "$firstname $lastname".trim()
}

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
    @Json(name = "created") val createdAt: String? = null,
    @Json(name = "updated") val updatedAt: String? = null,
    @Json(name = "lifeTimeInDays") val lifetimeInDays: Int? = null,
    @Json(name = "weightInGrams") val weightInGrams: Int? = null,
    @Json(name = "requestCount") val requestCount: Int? = null,
    val creator: BasketCreator? = null,
    val until: String? = null
)

@JsonClass(generateAdapter = true)
data class BasketCreator(
    val id: Int,
    val name: String,
    val avatar: String? = null,
    @Json(name = "isSleeping") val isSleeping: Boolean? = null
)


@JsonClass(generateAdapter = true)
data class CreateBasketRequest(
    val description: String,
    @Json(name = "contactTypes") val contactTypes: List<Int> = listOf(1),
    val lat: Double? = null,
    val lon: Double? = null,
    @Json(name = "lifeTimeInDays") val lifetimeInDays: Int = 1,
    val weightInGrams: Int = 2000,
    val pictures: List<String> = emptyList()
)

@JsonClass(generateAdapter = true)
data class UpdateBasketRequest(
    val description: String? = null,
    @Json(name = "contactTypes") val contactTypes: List<Int>? = null,
    val lat: Double? = null,
    val lon: Double? = null,
    @Json(name = "lifeTimeInDays") val lifetimeInDays: Int? = null,
    @Json(name = "weightInGrams") val weightInGrams: Int? = null
)

@JsonClass(generateAdapter = true)
data class Message(
    val id: Int? = null,
    val body: String,
    val sentAt: String? = null,
    val authorId: Int? = null
)

@JsonClass(generateAdapter = true)
data class Conversation(
    val id: Int,
    val title: String? = null,
    val lastMessage: Message? = null,
    @Json(name = "unreadMessages") val unreadMessages: Int = 0,
    val members: List<Int>? = null,
    @Json(name = "storeId") val storeId: Int? = null,
    val messages: List<Message>? = null
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
    val messages: List<Message>,
    val profiles: List<Profile>? = null
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
data class RegisteredPickup(
    val date: String,
    val store: StoreInfo,
    val slots: Int,
    val occupiedSlots: List<PickupUser> = emptyList(),
    val isConfirmed: Boolean? = null,
    val description: String? = null
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
data class OptionalMessage(
    val message: String? = null
)

