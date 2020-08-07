package com.ronaldbarrera.keepersecuritylogin.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.ronaldbarrera.keepersecuritylogin.R
import com.ronaldbarrera.keepersecuritylogin.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        binding.lifecycleOwner = this
        binding.loginViewModel = loginViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inputlayout_email.editText?.doAfterTextChanged {
            loginViewModel.loginDataChanged(
                it.toString(),
                inputlayout_password.editText?.text.toString()
            )
        }

        inputlayout_password.editText?.doAfterTextChanged {
            loginViewModel.loginDataChanged(
                inputlayout_email.editText?.text.toString(),
                it.toString()
            )
        }

        inputlayout_email.editText?.setOnFocusChangeListener { _, b ->
            if (!b) {
                val emailError = loginViewModel.loginFormState.value?.emailError
                inputlayout_email.error = when (emailError) {
                    null -> null
                    else -> {
                        getString(emailError)
                    }
                }
            }
        }

        loginViewModel.loginFormState.observe(viewLifecycleOwner, Observer {
            inputlayout_email.error = when (it.emailError) {
                null -> null
                else -> getString(it.emailError)
            }

            inputlayout_password.error = when (it.passwordError) {
                null -> null
                else -> getString(it.passwordError)
            }
        })
    }
}