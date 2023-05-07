package com.dicoding.storyapp.ui.auth.register

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
import com.dicoding.storyapp.databinding.FragmentRegisterBinding
import com.dicoding.storyapp.utils.AnimationUtil
import com.dicoding.storyapp.utils.ResultState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _fragmentRegisterBinding: FragmentRegisterBinding? = null
    private val fragmentRegisterBinding get() = _fragmentRegisterBinding!!

    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _fragmentRegisterBinding = FragmentRegisterBinding.inflate(inflater, container, false)
        return fragmentRegisterBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentRegisterBinding.btnRegister.isEnabled = false

        textChanged()

        // Register
        fragmentRegisterBinding.btnRegister.setOnClickListener {
            val name = fragmentRegisterBinding.etName.text.toString()
            val email = fragmentRegisterBinding.etEmail.text.toString()
            val password = fragmentRegisterBinding.etPassword.text.toString()

            registerViewModel.register(name, email, password)
            Log.d("Login", "setAction: $name, $email, $password")

            showLoading(true)
        }

        // Login
        fragmentRegisterBinding.btnLogin.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            it.findNavController().navigate(action)
        }

        // Result
        registerViewModel.result.observe(viewLifecycleOwner) { status ->
            showLoading(true)

            when (status) {
                is ResultState.Success -> status.data?.let {
                    if(it.error) {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                    }

                    // Back to Login Fragment
                    val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
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
            fragmentRegisterBinding.tvTitle,

            fragmentRegisterBinding.tvName,
            fragmentRegisterBinding.etName,

            fragmentRegisterBinding.tvEmail,
            fragmentRegisterBinding.etEmail,

            fragmentRegisterBinding.tvPassword,
            fragmentRegisterBinding.etPassword,

            fragmentRegisterBinding.tvConfirm,
            fragmentRegisterBinding.etConfirm,

            fragmentRegisterBinding.btnRegister
        )
    }

    private fun textChanged() {
        fragmentRegisterBinding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showButton()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        fragmentRegisterBinding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showButton()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        fragmentRegisterBinding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showButton()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        fragmentRegisterBinding.etConfirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showButton()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun showLoading(isLoading: Boolean) {
        fragmentRegisterBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun isValidEmail(str: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }

    private fun isValidPassword(str: String): Boolean {
        return str.length >= 8
    }

    private fun showButton() {
        fragmentRegisterBinding.btnRegister.isEnabled =
            fragmentRegisterBinding.tvName.text != null &&
                fragmentRegisterBinding.etEmail.text != null && isValidEmail(fragmentRegisterBinding.etEmail.text.toString()) &&
                    fragmentRegisterBinding.etPassword.text != null && isValidPassword(fragmentRegisterBinding.etPassword.text.toString()) &&
                        fragmentRegisterBinding.etConfirm.text != null && isValidPassword(fragmentRegisterBinding.etConfirm.text.toString()) &&
                            fragmentRegisterBinding.etPassword.text.toString() == fragmentRegisterBinding.etConfirm.text.toString()
    }
}