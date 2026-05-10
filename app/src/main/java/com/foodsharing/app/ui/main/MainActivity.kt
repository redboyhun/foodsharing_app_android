package com.foodsharing.app.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.foodsharing.app.R
import com.foodsharing.app.databinding.ActivityMainBinding
import com.foodsharing.app.ui.auth.LoginActivity
import com.foodsharing.app.util.AuthEventBus
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val topLevelDestinations = setOf(
            R.id.nearbyBasketsFragment,
            R.id.conversationsFragment,
            R.id.pickupsFragment,
            R.id.profileFragment
        )
        val appBarConfig = AppBarConfiguration(topLevelDestinations)
        setupActionBarWithNavController(navController, appBarConfig)
        binding.bottomNav.setupWithNavController(navController)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                AuthEventBus.sessionExpired.collect {
                    startActivity(
                        Intent(this@MainActivity, LoginActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            putExtra("session_expired", true)
                        }
                    )
                    finish()
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
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
