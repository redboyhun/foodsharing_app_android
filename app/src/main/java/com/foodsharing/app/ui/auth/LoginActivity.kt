package com.foodsharing.app.ui.auth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.foodsharing.app.databinding.ActivityLoginBinding
import com.foodsharing.app.ui.main.MainActivity
import com.foodsharing.app.util.Resource
import com.foodsharing.app.util.SessionManager
import com.foodsharing.app.util.SettingsManager
import com.foodsharing.app.worker.WorkScheduler
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private val sessionManager by lazy { SessionManager(this) }
    private val settingsManager by lazy { SettingsManager(this) }

    private var logoClickCount = 0
    private var hideJob: Job? = null

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* proceed regardless */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (sessionManager.isLoggedIn()) {
            navigateToMain()
            return
        }

        if (intent.getBooleanExtra("session_expired", false)) {
            Snackbar.make(binding.root, "Your session has expired. Please log in again.", Snackbar.LENGTH_LONG).show()
        }

        requestNotificationPermission()
        setupLogoClicks()
        setupServerSelector()
        setupLoginButton()
        observeLoginState()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun setupLogoClicks() {
        binding.ivLogo.setOnClickListener {
            logoClickCount++
            if (logoClickCount == 5) {
                showServerSelector(true)
                Snackbar.make(binding.root, "Beta server option enabled", Snackbar.LENGTH_SHORT).show()
            } else if (logoClickCount >= 10) {
                showServerSelector(false)
                logoClickCount = 0
                Snackbar.make(binding.root, "Beta server option hidden", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun showServerSelector(show: Boolean) {
        binding.tvSelectServer.isVisible = show
        binding.rgServer.isVisible = show
        
        hideJob?.cancel()
        if (show) {
            // Auto-hide after 1 minute of being enabled
            hideJob = lifecycleScope.launch {
                delay(60000)
                if (binding.rgServer.isVisible) {
                    showServerSelector(false)
                    logoClickCount = 0
                }
            }
        }
    }

    private fun setupServerSelector() {
        CoroutineScope(Dispatchers.Main).launch {
            val savedUrl = settingsManager.serverUrlFlow.first()
            if (savedUrl.contains("beta")) {
                showServerSelector(true)
                binding.rgServer.check(binding.rbBeta.id)
            } else {
                binding.rgServer.check(binding.rbProduction.id)
            }
        }
    }

    private fun setupLoginButton() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val serverUrl = if (binding.rgServer.isVisible && binding.rbBeta.isChecked) {
                SettingsManager.BETA_SERVER_URL
            } else {
                SettingsManager.DEFAULT_SERVER_URL
            }

            if (email.isEmpty() || password.isEmpty()) {
                Snackbar.make(binding.root, "Please enter email and password", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.login(email, password, serverUrl)
        }
    }

    private fun observeLoginState() {
        viewModel.loginState.observe(this) { state ->
            when (state) {
                is Resource.Loading -> {
                    binding.btnLogin.isEnabled = false
                    binding.progressBar.isVisible = true
                }
                is Resource.Success -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        val interval = settingsManager.refreshIntervalFlow.first()
                        WorkScheduler.scheduleAll(applicationContext, interval)
                    }
                    navigateToMain()
                }
                is Resource.Error -> {
                    binding.btnLogin.isEnabled = true
                    binding.progressBar.isVisible = false
                    Snackbar.make(binding.root, state.message, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
