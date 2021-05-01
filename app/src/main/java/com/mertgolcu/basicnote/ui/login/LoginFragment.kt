package com.mertgolcu.basicnote.ui.login

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mertgolcu.basicnote.R
import com.mertgolcu.basicnote.core.BaseFragment
import com.mertgolcu.basicnote.databinding.FragmentLoginBinding
import com.mertgolcu.basicnote.ext.*
import com.mertgolcu.basicnote.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(R.layout.fragment_login) {


    override val viewModel: LoginViewModel by viewModels()
    //val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            modelView = viewModel


            root.addAllEditTextStrokeUIListener()
            buttonLogin.setOnClickListener {
                requireView().hideKeyboard()
                editTextEmail.clearFocus()
                editTextPassword.clearFocus()
                root.controlAllEditTextStrokeUI()
                viewModel.onClickLogin()
            }

            textViewSignUpNow.movementMethod = LinkMovementMethod.getInstance()
            textViewSignUpNow.text = getString(R.string.register_now).textSpanColor {
                viewModel.onClickSignUpNow()
            }

            textViewForgotPassword.setOnClickListener {
                viewModel.navigateToForgotPassword()
            }
        }

    }

}