package com.example.quotesapp.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.quotesapp.R
import com.example.quotesapp.databinding.FragmentAuthProvidersBinding

class AuthProvidersFragment : Fragment() {

    private var _binding: FragmentAuthProvidersBinding? = null
    private val binding get() = _binding!!

    private var navigator: AuthNavigator? = null
    private var mode: AuthMode = AuthMode.SIGN_IN

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as? AuthNavigator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val modeName = savedInstanceState?.getString(ARG_MODE)
            ?: arguments?.getString(ARG_MODE)
        mode = modeName?.let { AuthMode.valueOf(it) } ?: AuthMode.SIGN_IN
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthProvidersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textSubtitle.text = when (mode) {
            AuthMode.SIGN_UP -> getString(R.string.auth_subtitle_signup)
            AuthMode.SIGN_IN -> getString(R.string.auth_subtitle_signin)
        }

        binding.buttonGoogle.setOnClickListener {
            navigator?.onProviderSelected(mode, AuthProvider.GOOGLE)
        }
        binding.buttonFacebook.setOnClickListener {
            navigator?.onProviderSelected(mode, AuthProvider.FACEBOOK)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ARG_MODE, mode.name)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        navigator = null
    }

    companion object {
        private const val ARG_MODE = "mode"

        fun newInstance(mode: AuthMode): AuthProvidersFragment {
            val fragment = AuthProvidersFragment()
            fragment.arguments = bundleOf(ARG_MODE to mode.name)
            return fragment
        }
    }
}
