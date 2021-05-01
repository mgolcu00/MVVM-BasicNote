package com.mertgolcu.basicnote.ui.note

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.mertgolcu.basicnote.R
import com.mertgolcu.basicnote.core.BaseFragment
import com.mertgolcu.basicnote.databinding.FragmentNoteBinding
import com.mertgolcu.basicnote.ext.addAllEditTextStrokeUIListener
import com.mertgolcu.basicnote.ext.hideKeyboard
import com.mertgolcu.basicnote.utils.SHOW_NOTE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : BaseFragment<FragmentNoteBinding, NoteViewModel>(R.layout.fragment_note) {

    override val viewModel: NoteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            modelView = viewModel

            modeUI(this, viewModel.mode!!)
            root.addAllEditTextStrokeUIListener()
            customTitleBar.imageButtonBack.setOnClickListener {
                requireView().hideKeyboard()
                editTextNoteTitle.clearFocus()
                editTextNoteText.clearFocus()
                viewModel.popBackStack()
            }

            buttonSave.setOnClickListener {
                requireView().hideKeyboard()
                editTextNoteTitle.clearFocus()
                editTextNoteText.clearFocus()
                viewModel.onClickSave()
            }

        }

    }

    private fun modeUI(binding: FragmentNoteBinding, mode: String) {
        binding.apply {
            customTitleBar.textViewTitle.text = mode
            buttonSave.isVisible = mode != SHOW_NOTE
            editTextNoteText.isFocusable = mode != SHOW_NOTE
            editTextNoteTitle.isFocusable = mode != SHOW_NOTE
        }
    }
}