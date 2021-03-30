package com.mertgolcu.basicnote.ui.signup

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mertgolcu.basicnote.R
import com.mertgolcu.basicnote.databinding.FragmentSignUpBinding
import com.mertgolcu.basicnote.event.LoginAndRegisterErrorType
import com.mertgolcu.basicnote.event.SignUpEvent
import com.mertgolcu.basicnote.extensions.*
import com.mertgolcu.basicnote.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val viewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSignUpBinding.bind(view)
        val loadingDialog = LoadingDialog(requireContext())
        binding.apply {
            modelView = viewModel

            buttonSignUp.setOnClickListener {
                loadingDialog.showDialog()
                viewModel.onSignUpClick()
                requireView().hideKeyboard()
                editTextEmail.clearFocus()
                editTextFullName.clearFocus()
                editTextPassword.clearFocus()
            }

            editTextEmail.addChangeStrokeUIListener()
            editTextFullName.addChangeStrokeUIListener()
            editTextPassword.addChangeStrokeUIListener()
            textViewLoginNow.movementMethod = LinkMovementMethod.getInstance()
            textViewLoginNow.text = getString(R.string.login_now).textSpanColor {
                viewModel.onLoginNowClick()
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.signUpEvent.collect { event ->
                loadingDialog.dismissDialog()
                when (event) {
                    is SignUpEvent.ShowSignUpErrorMessage -> {
                        binding.editTextPassword.changeStrokeUI()
                        binding.editTextEmail.changeStrokeUI()
                        binding.editTextFullName.changeStrokeUI()
                        when (event.msg) {
                            LoginAndRegisterErrorType.EMAIL_BLANK.name ->
                                getString(R.string.email_empty_error)
                                    .showSnackBar(requireView())

                            LoginAndRegisterErrorType.PASSWORD_BLANK.name -> getString(R.string.password_empty_error)
                                .showSnackBar(requireView())

                            LoginAndRegisterErrorType.FULL_NAME_BLANK.name -> getString(R.string.full_name_empty_error)
                                .showSnackBar(requireView())

                            LoginAndRegisterErrorType.EMAIL_FORMAT.name ->
                                getString(R.string.full_name_empty_error).showSnackBar(requireView())
                            else ->
                                event.msg.showSnackBar(requireView())

                        }
                    }
                    is SignUpEvent.SignUpSuccess -> {
                        event.msg.showSnackBar(requireView(), R.color.success_green)
                        // go to notes
                        val action = SignUpFragmentDirections.actionSignUpFragmentToHomeFragment()
                        findNavController().navigate(action)

                    }
                    is SignUpEvent.NavigateToLoginScreen -> {
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }

}