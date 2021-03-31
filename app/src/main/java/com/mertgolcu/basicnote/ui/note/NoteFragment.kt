package com.mertgolcu.basicnote.ui.note

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mertgolcu.basicnote.R
import com.mertgolcu.basicnote.core.BaseFragment
import com.mertgolcu.basicnote.databinding.FragmentNoteBinding
import com.mertgolcu.basicnote.event.EventType
import com.mertgolcu.basicnote.ext.*
import com.mertgolcu.basicnote.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

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