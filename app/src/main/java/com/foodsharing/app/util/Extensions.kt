package com.foodsharing.app.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.foodsharing.app.data.api.ApiClient
import com.google.android.material.imageview.ShapeableImageView
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

fun View.visible() { visibility = View.VISIBLE }
fun View.gone() { visibility = View.GONE }

fun Fragment.toast(msg: String) {
    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}

fun formatMessageTime(iso: String?): String {
    if (iso.isNullOrEmpty()) return ""
    return try {
        val formatters = listOf(
            DateTimeFormatter.ISO_DATE_TIME,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        )
        var parsed: LocalDateTime? = null
        for (fmt in formatters) {
            try { parsed = LocalDateTime.parse(iso, fmt); break } catch (_: Exception) {}
        }
        val dt = parsed ?: return iso.take(16).replace("T", " ")
        val now = LocalDateTime.now()
        val hhmm = DateTimeFormatter.ofPattern("HH:mm")
        when {
            dt.toLocalDate() == now.toLocalDate() -> dt.format(hhmm)
            dt.toLocalDate() == now.toLocalDate().minusDays(1) -> "Yesterday ${dt.format(hhmm)}"
            dt.year == now.year -> dt.format(DateTimeFormatter.ofPattern("MMM d"))
            else -> dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
    } catch (_: Exception) {
        iso.take(16).replace("T", " ")
    }
}

fun formatPickupDate(isoDate: String): String {
    return try {
        val odt = OffsetDateTime.parse(isoDate)
        odt.atZoneSameInstant(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("EEEE, yyyy-MM-dd HH:mm", Locale.getDefault()))
    } catch (e: Exception) {
        isoDate.take(16).replace("T", " ")
    }
}

fun toUtcIsoString(isoDate: String): String {
    return try {
        val odt = OffsetDateTime.parse(isoDate)
        odt.withOffsetSameInstant(ZoneOffset.UTC)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
    } catch (e: Exception) {
        isoDate
    }
}

private val AVATAR_COLORS = listOf(
    0xFF1976D2.toInt(), 0xFF388E3C.toInt(), 0xFFD32F2F.toInt(),
    0xFF7B1FA2.toInt(), 0xFF0288D1.toInt(), 0xFF5D4037.toInt()
)

fun createInitialsBitmap(name: String, size: Int = 256): Bitmap {
    val initial = name.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "?"
    val color = AVATAR_COLORS[(name.hashCode() and Int.MAX_VALUE) % AVATAR_COLORS.size]

    // Change this line:
    val bitmap = createBitmap(size, size, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.color = color
    canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)
    paint.color = Color.WHITE
    paint.textSize = size * 0.45f
    paint.textAlign = Paint.Align.CENTER
    canvas.drawText(initial, size / 2f, size / 2f - (paint.descent() + paint.ascent()) / 2f, paint)
    return bitmap
}

fun ImageView.loadAvatarWithFallback(url: String?, name: String, baseUrl: String = ApiClient.baseUrl) {
    val fullUrl = when {
        url.isNullOrEmpty() -> null
        url.startsWith("/") -> {
            val base = if (baseUrl.startsWith("http")) baseUrl else "https://$baseUrl"
            base.removeSuffix("/") + url
        }
        else -> url
    }
    
    val fallback = createInitialsBitmap(name).toDrawable(resources)
    
    if (fullUrl != null) {
        // Reset properties that might interfere with showing a loaded photo
        imageTintList = null
        colorFilter = null
        background = null
        setPadding(0, 0, 0, 0)
        
        if (this is ShapeableImageView) {
            strokeWidth = 0f
        }
        
        Glide.with(context)
            .load(fullUrl)
            .circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .placeholder(fallback)
            .error(fallback)
            .into(this)
    } else {
        setImageDrawable(fallback)
    }
}
