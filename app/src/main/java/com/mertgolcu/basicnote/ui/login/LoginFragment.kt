package com.mertgolcu.basicnote.ui.login

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mertgolcu.basicnote.R
import com.mertgolcu.basicnote.databinding.FragmentLoginBinding
import com.mertgolcu.basicnote.event.LoginAndRegisterErrorType
import com.mertgolcu.basicnote.event.LoginEvent
import com.mertgolcu.basicnote.extensions.*
import com.mertgolcu.basicnote.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {


    private val viewModel: LoginViewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentLoginBinding.bind(view)
        val loadingDialog = LoadingDialog(requireContext())


        binding.apply {
            modelView = viewModel


            buttonLogin.setOnClickListener {
                loadingDialog.showDialog()
                viewModel.onClickLogin()
                requireView().hideKeyboard()
                editTextEmail.clearFocus()
                editTextPassword.clearFocus()
            }

            editTextEmail.addChangeStrokeUIListener()
            editTextPassword.addChangeStrokeUIListener()
            textViewSignUpNow.movementMethod = LinkMovementMethod.getInstance()
            textViewSignUpNow.text = getString(R.string.register_now).textSpanColor {
                viewModel.onClickSignUpNow()
            }

            textViewForgotPassword.setOnClickListener {
                val action =
                    LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment(viewModel.emailText.value)
                findNavController().navigate(action)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.loginEvent.collect { event ->
                when (event) {
                    is LoginEvent.ShowLoginErrorMessage -> {
                        loadingDialog.dismissDialog()
                        binding.editTextPassword.changeStrokeUI()
                        binding.editTextEmail.changeStrokeUI()
                        when (event.msg) {

                            LoginAndRegisterErrorType.EMAIL_BLANK.name -> getString(R.string.email_empty_error).showSnackBar(
                                requireView()
                            )

                            LoginAndRegisterErrorType.PASSWORD_BLANK.name -> getString(R.string.password_empty_error).showSnackBar(
                                requireView()
                            )

                            LoginAndRegisterErrorType.EMAIL_FORMAT.name ->
                                getString(R.string.email_format_error).showSnackBar(requireView())

                            else ->
                                event.msg!!.showSnackBar(requireView())
                        }
                    }
                    is LoginEvent.LoginSuccess -> {
                        loadingDialog.dismissDialog()
                        event.msg.showSnackBar(requireView(), R.color.success_green)
                        // go to notes
                        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                        findNavController().navigate(action)
                    }
                    is LoginEvent.NavigateToSignUpScreen -> {
                        val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment(
                            event.email,
                            event.password
                        )
                        findNavController().navigate(action)
                    }
                }
            }
        }

    }
}