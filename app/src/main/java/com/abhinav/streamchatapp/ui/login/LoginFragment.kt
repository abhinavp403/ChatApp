package com.abhinav.streamchatapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.abhinav.streamchatapp.R
import com.abhinav.streamchatapp.databinding.FragmentLoginBinding
import com.abhinav.streamchatapp.ui.BindingFragment
import com.abhinav.streamchatapp.ui.login.LoginViewModel.LogInEvent.ErrorInputTooShort
import com.abhinav.streamchatapp.ui.login.LoginViewModel.LogInEvent.ErrorLogIn
import com.abhinav.streamchatapp.ui.login.LoginViewModel.LogInEvent.Success
import com.abhinav.streamchatapp.util.Constants.MIN_USERNAME_LENGTH
import com.abhinav.streamchatapp.util.navigateSafely
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginFragment : BindingFragment<FragmentLoginBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentLoginBinding::inflate

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConfirm.setOnClickListener {
            setUpConnectingUiState()
            viewModel.connectUser(binding.etUsername.text.toString())
        }

        binding.etUsername.addTextChangedListener {
            binding.etUsername.error = null
        }

        subscribeToEvents()
    }

    private fun subscribeToEvents() {
        lifecycleScope.launchWhenStarted {
            viewModel.loginEvent.collect { event ->
                when(event) {
                    is ErrorInputTooShort -> {
                        setUpIdleUiState()
                        binding.etUsername.error = getString(R.string.error_username_too_short, MIN_USERNAME_LENGTH)
                    }
                    is ErrorLogIn -> {
                        setUpIdleUiState()
                        Toast.makeText(context, event.error, Toast.LENGTH_SHORT).show()
                    }
                    is Success -> {
                        setUpIdleUiState()
                        findNavController().navigateSafely(R.id.action_loginFragment_to_channelFragment)
                    }
                }
            }
        }
    }

    private fun setUpConnectingUiState() {
        binding.progressBar.isVisible = true
        binding.btnConfirm.isEnabled = false
    }

    private fun setUpIdleUiState() {
        binding.progressBar.isVisible = false
        binding.btnConfirm.isEnabled = true
    }
}