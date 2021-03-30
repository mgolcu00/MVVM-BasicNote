package com.mertgolcu.basicnote.ui.home.deletenote

import android.util.Log
import android.widget.Toast
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.mertgolcu.basicnote.api.BaseResponse
import com.mertgolcu.basicnote.data.BasicNoteRepository
import com.mertgolcu.basicnote.data.Note
import com.mertgolcu.basicnote.data.PreferencesManager
import com.mertgolcu.basicnote.di.ApplicationScope
import com.mertgolcu.basicnote.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.math.log


class DeleteNoteViewModel @ViewModelInject constructor(
    private val repository: BasicNoteRepository,
    @ApplicationScope private val applicationScope: CoroutineScope,
    @Assisted private val state: SavedStateHandle,
    private val preferences: PreferencesManager,
) : ViewModel() {

    //val note = state.get<Note>("note")

//    val result = state.get<String>("result")



    private val tokenPreferences = preferences.tokenPreferencesFlow
    fun onConfirmClick() = applicationScope.launch {
        val note = state.get<Note>("note")
        when (val response =
            repository.deleteNote(note!!, getToken())) {
            is Result.Success -> {
                Log.d("TAG", "onConfirmClick: ${response.response.message}")
            }
            is Result.Error -> {
                Log.d("TAG", "onConfirmClick: ${response.exception.message}")
            }
        }
    }


    private suspend fun getToken(): String {
        val t: String = tokenPreferences.first().token.toString()
        return t
    }
}