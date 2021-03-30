package com.mertgolcu.basicnote.ui.home.deletenote

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.mertgolcu.basicnote.R
import com.mertgolcu.basicnote.ui.home.HomeFragment
import com.mertgolcu.basicnote.utils.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteNoteDialog : DialogFragment() {

    private val viewModel: DeleteNoteViewModel by viewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_note))
            .setMessage(getString(R.string.delete_note_message))
            .setNegativeButton(getString(R.string.cancel), null)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.onConfirmClick()
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    "result",
                    "success"
                )

            }
            .create()


}