package com.ronaldbarrera.keepersecuritylogin.ui.login

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.ronaldbarrera.keepersecuritylogin.R
import com.ronaldbarrera.keepersecuritylogin.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by activityViewModels { LoginViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
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

        loginViewModel.loginResult.observe(viewLifecycleOwner, Observer {
            if (it.error != null) {
                Snackbar.make(view, it.error.toString(), Snackbar.LENGTH_SHORT).show()
            }
            if (it.success != null) {
                Snackbar.make(view, "Logged in", Snackbar.LENGTH_LONG).show()
                this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
            }
        })

        button_login.setOnClickListener {
            loginViewModel.login(inputlayout_email.editText?.text.toString(), inputlayout_password.editText?.text.toString())
            closeKeyBoard()
        }
    }

    private fun closeKeyBoard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}