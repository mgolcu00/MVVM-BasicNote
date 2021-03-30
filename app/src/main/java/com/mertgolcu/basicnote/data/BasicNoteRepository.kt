package com.mertgolcu.basicnote.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.mertgolcu.basicnote.api.BaseResponse
import com.mertgolcu.basicnote.api.BasicNoteApi
import com.mertgolcu.basicnote.api.LoginResponseData
import com.mertgolcu.basicnote.utils.Result
import retrofit2.HttpException
import java.net.SocketException
import javax.inject.Inject
import javax.inject.Singleton


interface IRepository {
    suspend fun login(email: String, password: String): Result<BaseResponse<LoginResponseData>>
}
//single<IRepository> {BasicNoteRepository(get(),get())}
//viewModel (val repo : IRepositroy)
//repo.login(...,...,...)

@Singleton
class BasicNoteRepository @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val basicNoteApi: BasicNoteApi
) : IRepository {


    override suspend fun login(
        email: String,
        password: String
    ): Result<BaseResponse<LoginResponseData>> {
        return try {
            val response = basicNoteApi.login(email, password)
            preferencesManager.updateToken(response.data.access_token)
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e)
        } catch (e: SocketException) {
            Result.Error(e)
        }
    }

    suspend fun register(
        fullName: String,
        email: String,
        password: String
    ): Result<BaseResponse<LoginResponseData>> {
        return try {
            val response = basicNoteApi.register(fullName, email, password)
            //token load
            preferencesManager.updateToken(response.data.access_token)
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e)
        }
    }

    suspend fun forgotPassword(
        email: String
    ): Result<BaseResponse<Any>> {
        return try {
            val response = basicNoteApi.forgotPassword(email)
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e)
        }
    }


    fun getMyNotes(token: String, query: String) = Pager(
        config = PagingConfig(
            pageSize = 15,
            maxSize = 100,
            enablePlaceholders = false

        ),
        pagingSourceFactory = { NotePagingSource(basicNoteApi, token, query) }
    ).flow

    suspend fun deleteNote(note: Note, token: String): Result<BaseResponse<Any>> {
        return try {
            val response = basicNoteApi.deleteNote(note.id, "Bearer $token")
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e)
        }
    }

    suspend fun getMe(token: String): Result<BaseResponse<User>> {
        return try {
            val response = basicNoteApi.getMe("Bearer $token")
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e)
        }
    }

    suspend fun updateUser(token: String, user: User): Result<BaseResponse<User>> {
        return try {
            val response = basicNoteApi.updateUser(
                token = "Bearer $token",
                fullName = user.fullName,
                email = user.email
            )
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e)
        }
    }

    suspend fun updateUserPassword(
        token: String,
        password: String,
        newPassword: String,
        newPasswordConfirmation: String
    ): Result<BaseResponse<Any>> {
        return try {
            val response = basicNoteApi.updateUserPassword(
                token = "Bearer $token",
                password = password,
                newPassword = newPassword,
                newPasswordConfirmation = newPasswordConfirmation
            )
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e)
        }
    }

    suspend fun createNote(token: String, note: Note): Result<BaseResponse<Note>> {
        return try {
            val response = basicNoteApi.createNote(
                token = "Bearer $token",
                title = note.title,
                note = note.note
            )
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e)
        }
    }

    suspend fun updateNote(token: String, note: Note): Result<BaseResponse<Note>> {
        return try {
            val response = basicNoteApi.updateNote(
                noteId = note.id,
                token = "Bearer $token",
                title = note.title,
                note = note.note
            )
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e)
        }
    }

}