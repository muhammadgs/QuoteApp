package com.example.quotesapp.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.quotesapp.databinding.FragmentAuthWelcomeBinding

class AuthWelcomeFragment : Fragment() {

    private var _binding: FragmentAuthWelcomeBinding? = null
    private val binding get() = _binding!!

    private var navigator: AuthNavigator? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as? AuthNavigator
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSignUp.setOnClickListener { navigator?.onSignOptionSelected(AuthMode.SIGN_UP) }
        binding.buttonSignIn.setOnClickListener { navigator?.onSignOptionSelected(AuthMode.SIGN_IN) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        navigator = null
    }
}
