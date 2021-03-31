package com.mertgolcu.basicnote.ui.changepassword

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.mertgolcu.basicnote.R
import com.mertgolcu.basicnote.core.BaseFragment
import com.mertgolcu.basicnote.databinding.FragmentChangePasswordBinding
import com.mertgolcu.basicnote.ext.addAllEditTextStrokeUIListener
import com.mertgolcu.basicnote.ext.controlAllEditTextStrokeUI
import com.mertgolcu.basicnote.ext.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment :
    BaseFragment<FragmentChangePasswordBinding, ChangePasswordViewModel>
        (R.layout.fragment_change_password) {

    override val viewModel: ChangePasswordViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            modelView = viewModel

            root.addAllEditTextStrokeUIListener()
            buttonSave.setOnClickListener {
                viewModel.onSaveClicked()
                requireView().hideKeyboard()
                editTextPassword.clearFocus()
                editTextNewPassword.clearFocus()
                editTextRetypeNewPassword.clearFocus()
                root.controlAllEditTextStrokeUI()
            }
            customTitleBar.textViewTitle.text = getString(R.string.change_password)
            customTitleBar.imageButtonBack.setOnClickListener {
                viewModel.popBackStack()
            }
        }
    }


}