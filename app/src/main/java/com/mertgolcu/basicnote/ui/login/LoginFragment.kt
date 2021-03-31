package com.mertgolcu.basicnote.ui.login

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mertgolcu.basicnote.R
import com.mertgolcu.basicnote.core.BaseFragment
import com.mertgolcu.basicnote.databinding.FragmentLoginBinding
import com.mertgolcu.basicnote.event.LoginAndRegisterErrorType
import com.mertgolcu.basicnote.ext.*
import com.mertgolcu.basicnote.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlin.reflect.KClass

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(R.layout.fragment_login) {


    override val viewModel: LoginViewModel by viewModels()
    //val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //   val binding = FragmentLoginBinding.bind(view)
        val loadingDialog = LoadingDialog(requireContext())


        binding.apply {

            modelView = viewModel



            buttonLogin.setOnClickListener {
                requireView().hideKeyboard()
                editTextEmail.clearFocus()
                editTextPassword.clearFocus()
                editTextPassword.changeStrokeUI()
                editTextEmail.changeStrokeUI()
                viewModel.onClickLogin()
            }

            editTextEmail.addChangeStrokeUIListener()
            editTextPassword.addChangeStrokeUIListener()
            textViewSignUpNow.movementMethod = LinkMovementMethod.getInstance()
            textViewSignUpNow.text = getString(R.string.register_now).textSpanColor {
                viewModel.onClickSignUpNow()
            }

            textViewForgotPassword.setOnClickListener {
                viewModel.navigateToForgotPassword()
            }
        }

/*        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.loginEvent.collect { event ->
                when (event) {
                    is LoginViewEvent.ShowLoginErrorMessage -> {
                        loadingDialog.dismissDialog()

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
                    is LoginViewEvent.LoginSuccess -> {
                        loadingDialog.dismissDialog()
                        event.msg.showSnackBar(requireView(), R.color.success_green)
                        // go to notes
                        val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                        findNavController().navigate(action)
                    }
                    is LoginViewEvent.NavigateToSignUpScreen -> {
                        val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment(
                            event.email,
                            event.password
                        )
                        findNavController().navigate(action)
                    }
                }
            }
        }*/

    }


}