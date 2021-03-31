package com.mertgolcu.basicnote.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mertgolcu.basicnote.R
import com.mertgolcu.basicnote.core.BaseFragment
import com.mertgolcu.basicnote.databinding.FragmentProfileBinding
import com.mertgolcu.basicnote.event.EventType
import com.mertgolcu.basicnote.ext.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ProfileFragment :
    BaseFragment<FragmentProfileBinding, ProfileViewModel>(R.layout.fragment_profile) {

    override val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            modelView = viewModel
            root.addAllEditTextStrokeUIListener()
            customTitleBar.textViewTitle.text = getString(R.string.profile)
            customTitleBar.imageButtonBack.setOnClickListener {
                viewModel.popBackStack()
            }
            buttonSave.setOnClickListener {
                viewModel.onSaveButtonClick()
                requireView().hideKeyboard()
                editTextEmail.clearFocus()
                editTextFullName.clearFocus()
                root.controlAllEditTextStrokeUI()
            }
            textViewSignOut.setOnClickListener {
                viewModel.onSignOutClicked()
                requireView().hideKeyboard()
            }
            textViewChangePassword.setOnClickListener {
                viewModel.onChangePasswordClick()
                requireView().hideKeyboard()
            }

        }

    }
}

