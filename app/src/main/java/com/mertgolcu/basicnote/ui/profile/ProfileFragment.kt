package com.mertgolcu.basicnote.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mertgolcu.basicnote.R
import com.mertgolcu.basicnote.databinding.FragmentProfileBinding
import com.mertgolcu.basicnote.event.EventType
import com.mertgolcu.basicnote.event.ProfileEvent
import com.mertgolcu.basicnote.extensions.addChangeStrokeUIListener
import com.mertgolcu.basicnote.extensions.changeStrokeUI
import com.mertgolcu.basicnote.extensions.hideKeyboard
import com.mertgolcu.basicnote.extensions.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val binding = FragmentProfileBinding.bind(view)

        binding.apply {
            modelView = viewModel

            customTitleBar.textViewTitle.text = getString(R.string.profile)
            customTitleBar.imageButtonBack.setOnClickListener {
                findNavController().popBackStack()
            }
            editTextEmail.addChangeStrokeUIListener()
            editTextFullName.addChangeStrokeUIListener()
            buttonSave.setOnClickListener {
                viewModel.onSaveButtonClick()
                requireView().hideKeyboard()
                editTextEmail.clearFocus()
                editTextFullName.clearFocus()
            }
            textViewSignOut.setOnClickListener {
                viewModel.onSignOutClicked()
                requireView().hideKeyboard()
            }
            textViewChangePassword.setOnClickListener {
                val action =
                    ProfileFragmentDirections.actionProfileFragmentToChangePasswordFragment()
                findNavController().navigate(action)
            }

        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.profileEvent.collect { event ->
                when (event) {
                    is ProfileEvent.ShowMessageOnSuccessOrError -> {
                        when (event.code) {
                            EventType.ERROR -> {
                                if (event.msg == "empty") {
                                    binding.editTextEmail.changeStrokeUI()
                                    binding.editTextFullName.changeStrokeUI()
                                    getString(R.string.fill_required_fields).showSnackBar(
                                        requireView()
                                    )
                                } else
                                    event.msg.showSnackBar(requireView())
                            }
                            EventType.SUCCESS -> {
                                event.msg.showSnackBar(requireView(), R.color.success_green)
                            }

                        }
                    }
                    ProfileEvent.NavigateLoginForSignOut -> {
                        val action =
                            ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }
}

