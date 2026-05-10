package com.foodsharing.app.ui.baskets

import android.os.Bundle
import androidx.navigation.NavDirections
import com.foodsharing.app.R
import kotlin.Int

public class NearbyBasketsFragmentDirections private constructor() {
  private data class ActionNearbyBasketsToDetail(
    public val basketId: Int,
  ) : NavDirections {
    public override val actionId: Int = R.id.actionNearbyBasketsToDetail

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putInt("basketId", this.basketId)
        return result
      }
  }

  public companion object {
    public fun actionNearbyBasketsToDetail(basketId: Int): NavDirections =
        ActionNearbyBasketsToDetail(basketId)
  }
}
