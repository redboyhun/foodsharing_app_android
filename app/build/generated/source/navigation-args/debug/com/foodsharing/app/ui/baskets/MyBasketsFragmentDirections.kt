package com.foodsharing.app.ui.baskets

import android.os.Bundle
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.foodsharing.app.R
import kotlin.Int

public class MyBasketsFragmentDirections private constructor() {
  private data class ActionMyBasketsToDetail(
    public val basketId: Int,
  ) : NavDirections {
    public override val actionId: Int = R.id.actionMyBasketsToDetail

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putInt("basketId", this.basketId)
        return result
      }
  }

  public companion object {
    public fun actionMyBasketsToDetail(basketId: Int): NavDirections =
        ActionMyBasketsToDetail(basketId)

    public fun actionMyBasketsToAddEditBasket(): NavDirections =
        ActionOnlyNavDirections(R.id.action_myBaskets_to_addEditBasket)
  }
}
