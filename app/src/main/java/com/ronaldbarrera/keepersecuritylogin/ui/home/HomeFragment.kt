package com.ronaldbarrera.keepersecuritylogin.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ronaldbarrera.keepersecuritylogin.R
import com.ronaldbarrera.keepersecuritylogin.databinding.FragmentHomeBinding
import com.ronaldbarrera.keepersecuritylogin.ui.login.LoginViewModel
import com.ronaldbarrera.keepersecuritylogin.ui.login.LoginViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val loginViewModel: LoginViewModel by activityViewModels { LoginViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        loginViewModel.isLoggedIn.observe(viewLifecycleOwner, Observer {
            if(!it) {
                this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
            }
        })
    }
}