package com.mertgolcu.basicnote.api

import com.mertgolcu.basicnote.data.User
import retrofit2.http.*

interface BasicNoteApi {

    @POST("auth/login")
    suspend fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): BaseResponse<LoginResponseData>

    @POST("auth/register")
    suspend fun register(
        @Query("full_name") full_name: String,
        @Query("email") email: String,
        @Query("password") password: String
    ): BaseResponse<LoginResponseData>

    @POST("auth/forgot-password")
    suspend fun forgotPassword(
        @Query("email") email: String
    ): BaseResponse<Any>

    @GET("users/me/notes")
    suspend fun getMyNotes(
        @Query("page") pageNum: Int
    ): BaseResponse<PageResponse>

    @DELETE("notes/{note_id}")
    suspend fun deleteNote(
        @Path("note_id") noteId: Int
    ): BaseResponse<Any>

    @POST("notes")
    suspend fun createNote(
        @Query("title") title: String,
        @Query("note") note: String
    ): BaseResponse<Any>

    @PUT("notes/{note_id}")
    suspend fun updateNote(
        @Path("note_id") noteId: Int,
        @Query("title") title: String,
        @Query("note") note: String
    ): BaseResponse<Any>


    @GET("users/me")
    suspend fun getMe(): BaseResponse<User>


    @PUT("users/me")
    suspend fun updateUser(
        @Query("full_name") fullName: String,
        @Query("email") email: String
    ): BaseResponse<Any>

    @PUT("users/me/password")
    suspend fun updateUserPassword(
        @Query("password") password: String,
        @Query("new_password") newPassword: String,
        @Query("new_password_confirmation") newPasswordConfirmation: String
    ): BaseResponse<Any>

}