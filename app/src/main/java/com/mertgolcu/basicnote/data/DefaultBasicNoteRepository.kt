package com.mertgolcu.basicnote.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mertgolcu.basicnote.api.BaseResponse
import com.mertgolcu.basicnote.api.BasicNoteApi
import com.mertgolcu.basicnote.api.LoginResponseData
import com.mertgolcu.basicnote.utils.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.net.SocketException
import javax.inject.Inject
import javax.inject.Singleton


interface BasicNoteRepository {
    suspend fun login(email: String, password: String): Result<BaseResponse<LoginResponseData>>

    suspend fun register(
        fullName: String,
        email: String,
        password: String
    ): Result<BaseResponse<LoginResponseData>>

    suspend fun forgotPassword(email: String): Result<BaseResponse<Any>>

    fun getMyNotes(): Flow<PagingData<Note>>

    suspend fun deleteNote(id: Int): Result<BaseResponse<Any>>

    suspend fun getMe(): Result<BaseResponse<User>>

    suspend fun updateUser(fullName: String, email: String): Result<BaseResponse<Any>>

    suspend fun updateUserPassword(
        password: String,
        newPassword: String,
        newPasswordConfirmation: String
    ): Result<BaseResponse<Any>>

    suspend fun createNote(title: String, note: String): Result<BaseResponse<Any>>

    suspend fun updateNote(id: Int, title: String, note: String): Result<BaseResponse<Any>>
}


@Singleton
class DefaultBasicNoteRepository @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val basicNoteApi: BasicNoteApi
) : BasicNoteRepository {

    override suspend fun login(
        email: String,
        password: String
    ): Result<BaseResponse<LoginResponseData>> {
        return try {
            val response = basicNoteApi.login(
                email = email,
                password = password
            )
            preferencesManager.updateToken(response.data.access_token)
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e)
        } catch (e: SocketException) {
            Result.Error(e)
        }
    }

    override suspend fun register(
        fullName: String,
        email: String,
        password: String
    ): Result<BaseResponse<LoginResponseData>> {
        return try {
            val response = basicNoteApi.register(
                full_name = fullName,
                email = email,
                password = password
            )
            //token load
            preferencesManager.updateToken(response.data.access_token)
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e)
        }
    }

    override suspend fun forgotPassword(
        email: String
    ): Result<BaseResponse<Any>> {
        return try {
            val response = basicNoteApi.forgotPassword(
                email = email
            )
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e)
        }
    }


    override fun getMyNotes() = Pager(
        config = PagingConfig(
            pageSize = 15,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { NotePagingSource(basicNoteApi) }
    ).flow

    override suspend fun deleteNote(id: Int): Result<BaseResponse<Any>> {
        return try {
            val response = basicNoteApi.deleteNote(
                noteId = id
            )
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e)
        }
    }


    override suspend fun getMe(): Result<BaseResponse<User>> {
        return try {
            val response = basicNoteApi.getMe()
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e)
        }
    }

    override suspend fun updateUser(fullName: String, email: String): Result<BaseResponse<Any>> {
        return try {
            val response = basicNoteApi.updateUser(
                fullName = fullName,
                email = email
            )
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e)
        }
    }

    override suspend fun updateUserPassword(
        password: String,
        newPassword: String,
        newPasswordConfirmation: String
    ): Result<BaseResponse<Any>> {
        return try {
            val response = basicNoteApi.updateUserPassword(
                password = password,
                newPassword = newPassword,
                newPasswordConfirmation = newPasswordConfirmation
            )
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e)
        }
    }

    override suspend fun createNote(title: String, note: String): Result<BaseResponse<Any>> {
        return try {
            val response = basicNoteApi.createNote(
                title = title,
                note = note
            )
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e)
        }
    }

    override suspend fun updateNote(
        id: Int,
        title: String,
        note: String
    ): Result<BaseResponse<Any>> {
        return try {
            val response = basicNoteApi.updateNote(
                noteId = id,
                title = title,
                note = note
            )
            Result.Success(response)
        } catch (e: HttpException) {
            Result.Error(e)
        }
    }

}