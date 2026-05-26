package com.foodsharing.app.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.foodsharing.app.R
import com.foodsharing.app.databinding.ActivityMainBinding
import com.foodsharing.app.ui.auth.LoginActivity
import com.foodsharing.app.util.AuthEventBus
import com.foodsharing.app.util.NotificationHelper
import com.foodsharing.app.util.SessionManager
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sessionManager: SessionManager

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Permission result handled by the system; notifications will work if granted
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Sync bottom nav indicator for top-level destinations; do NOT use
        // setupWithNavController — it enables save/restoreState (Nav 2.4+) which
        // re-opens deep sub-fragments instead of the section root on tab tap.
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val topLevelIds = setOf(
                R.id.nearbyBasketsFragment,
                R.id.conversationsFragment,
                R.id.pickupsFragment,
                R.id.profileFragment
            )
            if (destination.id in topLevelIds) {
                binding.bottomNav.selectedItemId = destination.id
            }
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            if (navController.currentDestination?.id != item.itemId) {
                navController.navigate(
                    item.itemId,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.nearbyBasketsFragment, inclusive = false, saveState = false)
                        .setLaunchSingleTop(true)
                        .setRestoreState(false)
                        .build()
                )
            }
            true
        }

        binding.bottomNav.setOnItemReselectedListener { /* stay on current root */ }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                AuthEventBus.sessionExpired.collect {
                    NotificationHelper.showSessionExpiredNotification(this@MainActivity)
                    redirectToLogin()
                }
            }
        }

        // Handle notification deep-links
        intent.getStringExtra("navigate_to")?.let { destination ->
            when (destination) {
                "conversations" -> navController.navigate(R.id.conversationsFragment)
                "baskets" -> navController.navigate(R.id.nearbyBasketsFragment)
                "pickups" -> navController.navigate(R.id.pickupsFragment)
            }
        }

        checkNotificationPermission()
    }

    override fun onResume() {
        super.onResume()
        if (!sessionManager.isLoggedIn()) {
            NotificationHelper.showSessionExpiredNotification(this)
            redirectToLogin()
        }
    }

    private fun redirectToLogin() {
        startActivity(
            Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra("session_expired", true)
            }
        )
        finish()
    }

    private fun checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
