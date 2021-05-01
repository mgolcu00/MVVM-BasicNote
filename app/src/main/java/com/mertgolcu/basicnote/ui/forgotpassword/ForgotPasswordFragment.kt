package com.mertgolcu.basicnote.ui.forgotpassword

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mertgolcu.basicnote.R
import com.mertgolcu.basicnote.core.BaseFragment
import com.mertgolcu.basicnote.databinding.ForgotPasswordFragmentBinding
import com.mertgolcu.basicnote.ext.addChangeStrokeUIListener
import com.mertgolcu.basicnote.ext.changeStrokeUI
import com.mertgolcu.basicnote.ext.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ForgotPasswordFragment :
    BaseFragment<ForgotPasswordFragmentBinding, ForgotPasswordViewModel>(R.layout.forgot_password_fragment) {


    override val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            modelView = viewModel

            buttonRestPassword.setOnClickListener {
                viewModel.onResetClick()
                requireView().hideKeyboard()
                editTextEmail.changeStrokeUI()
                editTextEmail.clearFocus()
            }

            editTextEmail.addChangeStrokeUIListener()
            customTitleBar.textViewTitle.isVisible = false
            customTitleBar.imageButtonBack.setOnClickListener {
                viewModel.popBackStack()
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.forgotPasswordEvent.collect { event ->

                when (event) {
                    is ForgotPasswordEvent.ShowSendEmailSuccess -> {
                        successUI(event.msg)
                    }
                }
            }
        }

    }


    private fun successUI(msg: String) {
        binding.apply {
            textViewForgotPasswordTitle.text =
                getString(R.string.password_reset_confirmation)
            textViewForgotPasswordDescription.text = msg
            textViewForgotPasswordDescription.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.success_green
                )
            )
            editTextEmail.isVisible = false
            buttonRestPassword.text = getString(R.string.login)
            buttonRestPassword.setOnClickListener {
               viewModel.popBackStack()
            }
            customTitleBar.imageButtonBack.isVisible = false

        }
    }

}