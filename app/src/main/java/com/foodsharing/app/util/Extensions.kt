package com.foodsharing.app.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

private val AVATAR_COLORS = listOf(
    0xFF1976D2.toInt(), 0xFF388E3C.toInt(), 0xFFD32F2F.toInt(),
    0xFF7B1FA2.toInt(), 0xFF0288D1.toInt(), 0xFF5D4037.toInt()
)

fun ImageView.loadAvatarWithFallback(url: String?, name: String, baseUrl: String = "") {
    val fullUrl = when {
        url.isNullOrEmpty() -> null
        url.startsWith("/") -> baseUrl.removeSuffix("/") + url
        else -> url
    }
    if (fullUrl != null) {
        Glide.with(context).load(fullUrl).circleCrop().into(this)
    } else {
        val initial = name.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
        val color = AVATAR_COLORS[(name.hashCode() and Int.MAX_VALUE) % AVATAR_COLORS.size]
        val size = 96
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = color
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)
        paint.color = Color.WHITE
        paint.textSize = size * 0.45f
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText(initial, size / 2f, size / 2f - (paint.descent() + paint.ascent()) / 2f, paint)
        setImageBitmap(bitmap)
    }
}

