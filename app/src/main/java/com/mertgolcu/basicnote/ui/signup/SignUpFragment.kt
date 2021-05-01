package com.mertgolcu.basicnote.ui.signup

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.fragment.app.viewModels
import com.mertgolcu.basicnote.R
import com.mertgolcu.basicnote.core.BaseFragment
import com.mertgolcu.basicnote.databinding.FragmentSignUpBinding
import com.mertgolcu.basicnote.ext.addAllEditTextStrokeUIListener
import com.mertgolcu.basicnote.ext.controlAllEditTextStrokeUI
import com.mertgolcu.basicnote.ext.hideKeyboard
import com.mertgolcu.basicnote.ext.textSpanColor
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpFragment :
    BaseFragment<FragmentSignUpBinding, SignUpViewModel>(R.layout.fragment_sign_up) {

    override val viewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            modelView = viewModel


            root.addAllEditTextStrokeUIListener()
            buttonSignUp.setOnClickListener {
                viewModel.onSignUpClick()
                requireView().hideKeyboard()
                editTextEmail.clearFocus()
                editTextFullName.clearFocus()
                editTextPassword.clearFocus()
                root.controlAllEditTextStrokeUI()
            }

            textViewLoginNow.movementMethod = LinkMovementMethod.getInstance()
            textViewLoginNow.text = getString(R.string.login_now).textSpanColor {
                viewModel.onLoginNowClick()
            }
        }

    }

}