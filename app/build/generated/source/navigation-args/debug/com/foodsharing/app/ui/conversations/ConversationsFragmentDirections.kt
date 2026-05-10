package com.foodsharing.app.ui.conversations

import android.os.Bundle
import androidx.navigation.NavDirections
import com.foodsharing.app.R
import kotlin.Int

public class ConversationsFragmentDirections private constructor() {
  private data class ActionConversationsToChat(
    public val conversationId: Int,
  ) : NavDirections {
    public override val actionId: Int = R.id.actionConversationsToChat

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putInt("conversationId", this.conversationId)
        return result
      }
  }

  public companion object {
    public fun actionConversationsToChat(conversationId: Int): NavDirections =
        ActionConversationsToChat(conversationId)
  }
}
