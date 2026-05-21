package com.foodsharing.app.util

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.foodsharing.app.data.api.ApiClient
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream

@GlideModule
@Suppress("unused")
class FoodsharingGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        Log.d("FoodsharingGlide", "Registering OkHttp components")
        
        // Use a delegating Call.Factory to handle the case where ApiClient is not yet initialized
        val lazyClient = object : Call.Factory {
            override fun newCall(request: Request): Call {
                val client = ApiClient.okHttpClient ?: OkHttpClient()
                return client.newCall(request)
            }
        }
        
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(lazyClient)
        )
    }

    override fun isManifestParsingEnabled(): Boolean = false
}
