package com.mertgolcu.basicnote.ui.note

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mertgolcu.basicnote.R
import com.mertgolcu.basicnote.databinding.FragmentNoteBinding
import com.mertgolcu.basicnote.event.EventType
import com.mertgolcu.basicnote.event.NoteEvent
import com.mertgolcu.basicnote.extensions.addChangeStrokeUIListener
import com.mertgolcu.basicnote.extensions.changeStrokeUI
import com.mertgolcu.basicnote.extensions.hideKeyboard
import com.mertgolcu.basicnote.extensions.showSnackBar
import com.mertgolcu.basicnote.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class NoteFragment : Fragment(R.layout.fragment_note) {

    private val viewModel: NoteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentNoteBinding.bind(view)
        val loadingDialog = LoadingDialog(requireContext())
        binding.apply {
            modelView = viewModel

            modeUI(this, viewModel.mode!!)
            editTextNoteTitle.addChangeStrokeUIListener()
            editTextNoteText.addChangeStrokeUIListener()
            customTitleBar.imageButtonBack.setOnClickListener {
                requireView().hideKeyboard()
                editTextNoteTitle.clearFocus()
                editTextNoteText.clearFocus()
                findNavController().popBackStack()
            }

            buttonSave.setOnClickListener {
                loadingDialog.showDialog()
                requireView().hideKeyboard()
                editTextNoteTitle.clearFocus()
                editTextNoteText.clearFocus()
                viewModel.onClickSave()
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.noteEvent.collect { event ->
                loadingDialog.dismissDialog()
                when (event) {
                    is NoteEvent.ShowMessageOnSuccessOrError -> {
                        when (event.code) {
                            EventType.ERROR -> {
                                binding.editTextNoteTitle.changeStrokeUI()
                                binding.editTextNoteText.changeStrokeUI()
                                if (event.msg == "empty") getString(R.string.fill_required_fields).showSnackBar(
                                    requireView()
                                )
                                else
                                    event.msg.showSnackBar(requireView())

                            }
                            EventType.SUCCESS -> {
                                event.msg.showSnackBar(requireView(), R.color.success_green)
                                findNavController().popBackStack()
                            }
                        }
                    }
                }
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