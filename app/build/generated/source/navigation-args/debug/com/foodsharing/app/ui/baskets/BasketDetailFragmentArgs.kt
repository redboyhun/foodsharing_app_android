package com.foodsharing.app.ui.baskets

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Int
import kotlin.jvm.JvmStatic

public data class BasketDetailFragmentArgs(
  public val basketId: Int,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putInt("basketId", this.basketId)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("basketId", this.basketId)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): BasketDetailFragmentArgs {
      bundle.setClassLoader(BasketDetailFragmentArgs::class.java.classLoader)
      val __basketId : Int
      if (bundle.containsKey("basketId")) {
        __basketId = bundle.getInt("basketId")
      } else {
        throw IllegalArgumentException("Required argument \"basketId\" is missing and does not have an android:defaultValue")
      }
      return BasketDetailFragmentArgs(__basketId)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): BasketDetailFragmentArgs {
      val __basketId : Int?
      if (savedStateHandle.contains("basketId")) {
        __basketId = savedStateHandle["basketId"]
        if (__basketId == null) {
          throw IllegalArgumentException("Argument \"basketId\" of type integer does not support null values")
        }
      } else {
        throw IllegalArgumentException("Required argument \"basketId\" is missing and does not have an android:defaultValue")
      }
      return BasketDetailFragmentArgs(__basketId)
    }
  }
}
