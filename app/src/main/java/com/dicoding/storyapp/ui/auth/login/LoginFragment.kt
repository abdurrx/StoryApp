package com.dicoding.storyapp.ui.auth.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.dicoding.storyapp.databinding.FragmentLoginBinding
import com.dicoding.storyapp.utils.AnimationUtil
import com.dicoding.storyapp.utils.ResultState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _fragmentLoginBinding: FragmentLoginBinding? = null
    private val fragmentLoginBinding get() = _fragmentLoginBinding!!

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _fragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        return fragmentLoginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentLoginBinding.btnLogin.isEnabled = false

        fragmentLoginBinding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showButton()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        fragmentLoginBinding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showButton()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Login
        fragmentLoginBinding.btnLogin.setOnClickListener {
            val email = fragmentLoginBinding.etEmail.text.toString()
            val password = fragmentLoginBinding.etPassword.text.toString()

            loginViewModel.login(email, password)
            Log.d("Login", "setAction: $email, $password")

            showLoading(true)
        }

        // Register
        fragmentLoginBinding.btnRegister.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            it.findNavController().navigate(action)
        }

        // Result
        loginViewModel.result.observe(viewLifecycleOwner) { status ->
            showLoading(true)

            when (status) {
                is ResultState.Success -> status.data?.let {
                    if(it.error) {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }

                    // Into Story Fragment
                    val action = LoginFragmentDirections.actionLoginFragmentToStoryFragment()
                    findNavController().navigate(action)

                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()

                    showLoading(false)
                }

                is ResultState.Error -> {
                    Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }

                else -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    showLoading(true)
                }
            }
        }

        // Animation
        AnimationUtil.playAnimation(
            fragmentLoginBinding.tvTitle,

            fragmentLoginBinding.tvEmail,
            fragmentLoginBinding.etEmail,

            fragmentLoginBinding.tvPassword,
            fragmentLoginBinding.etPassword,

            fragmentLoginBinding.btnLogin
        )
    }

    private fun showLoading(isLoading: Boolean) {
        fragmentLoginBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun isValidEmail(str: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }

    private fun isValidPassword(str: String): Boolean {
        return str.length >= 8
    }

    private fun showButton() {
        fragmentLoginBinding.btnLogin.isEnabled =
            fragmentLoginBinding.etEmail.text != null && isValidEmail(fragmentLoginBinding.etEmail.text.toString()) &&
                fragmentLoginBinding.etPassword.text != null && isValidPassword(fragmentLoginBinding.etPassword.text.toString())
    }
}