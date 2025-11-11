package com.example.quotesapp.auth

interface AuthNavigator {
    fun onSignOptionSelected(mode: AuthMode)
    fun onProviderSelected(mode: AuthMode, provider: AuthProvider)
}
