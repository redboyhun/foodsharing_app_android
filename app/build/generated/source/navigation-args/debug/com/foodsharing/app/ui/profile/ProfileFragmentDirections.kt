package com.foodsharing.app.ui.profile

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.foodsharing.app.R

public class ProfileFragmentDirections private constructor() {
  public companion object {
    public fun actionProfileToSettings(): NavDirections =
        ActionOnlyNavDirections(R.id.action_profile_to_settings)

    public fun actionProfileToMyBaskets(): NavDirections =
        ActionOnlyNavDirections(R.id.action_profile_to_myBaskets)
  }
}
