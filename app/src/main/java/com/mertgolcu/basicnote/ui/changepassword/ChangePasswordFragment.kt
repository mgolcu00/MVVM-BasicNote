package com.mertgolcu.basicnote.ui.changepassword

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mertgolcu.basicnote.R
import com.mertgolcu.basicnote.databinding.FragmentChangePasswordBinding
import com.mertgolcu.basicnote.event.BaseEvent
import com.mertgolcu.basicnote.event.EventType
import com.mertgolcu.basicnote.extensions.addChangeStrokeUIListener
import com.mertgolcu.basicnote.extensions.changeStrokeUI
import com.mertgolcu.basicnote.extensions.hideKeyboard
import com.mertgolcu.basicnote.extensions.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {

    private val viewModel: ChangePasswordViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentChangePasswordBinding.bind(view)

        binding.apply {
            modelView = viewModel

            buttonSave.setOnClickListener {
                viewModel.onSaveClicked()
                requireView().hideKeyboard()
                editTextPassword.clearFocus()
                editTextNewPassword.clearFocus()
                editTextRetypeNewPassword.clearFocus()
            }

            customTitleBar.textViewTitle.text = getString(R.string.change_password)
            customTitleBar.imageButtonBack.setOnClickListener {
                findNavController().popBackStack()
            }
            editTextPassword.addChangeStrokeUIListener()
            editTextNewPassword.addChangeStrokeUIListener()
            editTextRetypeNewPassword.addChangeStrokeUIListener()
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.changePasswordEvent.collect { event ->
                when (event) {
                    is BaseEvent.ShowErrorOrSuccessMessage -> {
                        when (event.type) {
                            EventType.SUCCESS -> {
                                event.msg.showSnackBar(requireView(), R.color.success_green)
                            }
                            EventType.ERROR -> {

                                if (event.msg == "empty") {
                                    binding.apply {
                                        editTextPassword.changeStrokeUI()
                                        editTextNewPassword.changeStrokeUI()
                                        editTextRetypeNewPassword.changeStrokeUI()
                                    }
                                    getString(R.string.fill_required_fields).showSnackBar(
                                        requireView()
                                    )
                                } else {
                                    event.msg.showSnackBar(requireView())
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}