package com.foodsharing.app.ui.conversations

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Int
import kotlin.jvm.JvmStatic

public data class ChatFragmentArgs(
  public val conversationId: Int,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putInt("conversationId", this.conversationId)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("conversationId", this.conversationId)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): ChatFragmentArgs {
      bundle.setClassLoader(ChatFragmentArgs::class.java.classLoader)
      val __conversationId : Int
      if (bundle.containsKey("conversationId")) {
        __conversationId = bundle.getInt("conversationId")
      } else {
        throw IllegalArgumentException("Required argument \"conversationId\" is missing and does not have an android:defaultValue")
      }
      return ChatFragmentArgs(__conversationId)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): ChatFragmentArgs {
      val __conversationId : Int?
      if (savedStateHandle.contains("conversationId")) {
        __conversationId = savedStateHandle["conversationId"]
        if (__conversationId == null) {
          throw IllegalArgumentException("Argument \"conversationId\" of type integer does not support null values")
        }
      } else {
        throw IllegalArgumentException("Required argument \"conversationId\" is missing and does not have an android:defaultValue")
      }
      return ChatFragmentArgs(__conversationId)
    }
  }
}
