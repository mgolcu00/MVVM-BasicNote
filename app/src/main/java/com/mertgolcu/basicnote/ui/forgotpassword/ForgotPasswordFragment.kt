package com.mertgolcu.basicnote.ui.forgotpassword

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mertgolcu.basicnote.R
import com.mertgolcu.basicnote.databinding.ForgotPasswordFragmentBinding
import com.mertgolcu.basicnote.event.ForgotPasswordEvent
import com.mertgolcu.basicnote.event.LoginAndRegisterErrorType
import com.mertgolcu.basicnote.extensions.addChangeStrokeUIListener
import com.mertgolcu.basicnote.extensions.changeStrokeUI
import com.mertgolcu.basicnote.extensions.hideKeyboard
import com.mertgolcu.basicnote.extensions.showSnackBar
import com.mertgolcu.basicnote.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment(R.layout.forgot_password_fragment) {


    private val viewModel: ForgotPasswordViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = ForgotPasswordFragmentBinding.bind(view)

        val loadingDialog = LoadingDialog(requireContext())



        binding.apply {
            modelView = viewModel

            buttonRestPassword.setOnClickListener {
                loadingDialog.showDialog()
                viewModel.onResetClick()
                requireView().hideKeyboard()
                editTextEmail.clearFocus()
            }

//            imageButtonBack.setOnClickListener {
//                findNavController().popBackStack()
//            }

            editTextEmail.addChangeStrokeUIListener()
            customTitleBar.textViewTitle.isVisible = false
            customTitleBar.imageButtonBack.setOnClickListener {
                findNavController().popBackStack()
            }
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                viewModel.forgotPasswordEvent.collect { event ->
                    loadingDialog.dismissDialog()
                    when (event) {
                        is ForgotPasswordEvent.ShowForgotPasswordErrorMessage -> {
                            when (event.msg) {
                                LoginAndRegisterErrorType.EMAIL_BLANK.name -> {
                                    binding.editTextEmail.changeStrokeUI()
                                    getString(R.string.email_empty_error).showSnackBar(requireView())
                                }
                                LoginAndRegisterErrorType.EMAIL_FORMAT.name ->
                                    getString(R.string.email_empty_error).showSnackBar(requireView())
                                else ->
                                    event.msg.showSnackBar(requireView())
                            }
                        }
                        is ForgotPasswordEvent.ShowSendEmailSuccess -> {
                            binding.apply {
                                textViewForgotPasswordTitle.text =
                                    getString(R.string.password_reset_confirmation)
                                textViewForgotPasswordDescription.text = event.msg
                                textViewForgotPasswordDescription.setTextColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.success_green
                                    )
                                )
                                editTextEmail.isVisible = false
                                buttonRestPassword.text = getString(R.string.login)
                                buttonRestPassword.setOnClickListener {
                                    findNavController().popBackStack()
                                }
                                customTitleBar.imageButtonBack.isVisible = false

                            }
                        }
                    }
                }
            }
        }

    }
}