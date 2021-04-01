package com.mertgolcu.basicnote.ui.home.deletenote

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mertgolcu.basicnote.data.BasicNoteRepository
import com.mertgolcu.basicnote.data.Note
import com.mertgolcu.basicnote.di.ApplicationScope
import com.mertgolcu.basicnote.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class DeleteNoteViewModel @ViewModelInject constructor(
    private val repository: BasicNoteRepository,
    @ApplicationScope private val applicationScope: CoroutineScope,
    @Assisted private val state: SavedStateHandle,
) : ViewModel() {

    fun onConfirmClick() = applicationScope.launch {
        val note = state.get<Note>("note")
        if (note != null) {
            when (val response =
                repository.deleteNote(note.id)) {
                is Result.Success -> {
                    Log.d("TAG", "onConfirmClick: ${response.response.message}")
                }
                is Result.Error -> {
                    Log.d("TAG", "onConfirmClick: ${response.exception.message}")
                }
            }
        }
    }


}