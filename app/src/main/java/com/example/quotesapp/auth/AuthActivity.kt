package com.example.quotesapp.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quotesapp.MainActivity
import com.example.quotesapp.R
import com.example.quotesapp.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity(), AuthNavigator {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.authContainer, AuthWelcomeFragment())
                .commit()
        }
    }

    override fun onSignOptionSelected(mode: AuthMode) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.authContainer, AuthProvidersFragment.newInstance(mode))
            .addToBackStack(null)
            .commit()
    }

    override fun onProviderSelected(mode: AuthMode, provider: AuthProvider) {
        val message = when (mode) {
            AuthMode.SIGN_UP -> when (provider) {
                AuthProvider.GOOGLE -> getString(R.string.auth_google_signup)
                AuthProvider.FACEBOOK -> getString(R.string.auth_facebook_signup)
            }
            AuthMode.SIGN_IN -> when (provider) {
                AuthProvider.GOOGLE -> getString(R.string.auth_google_signin)
                AuthProvider.FACEBOOK -> getString(R.string.auth_facebook_signin)
            }
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
