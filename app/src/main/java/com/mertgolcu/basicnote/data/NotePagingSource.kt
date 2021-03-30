package com.mertgolcu.basicnote.data

import androidx.paging.PagingSource
import com.mertgolcu.basicnote.api.BasicNoteApi
import com.mertgolcu.basicnote.utils.NOTE_STARTING_PAGE_INDEX
import retrofit2.HttpException
import java.io.IOException
import java.util.*


class NotePagingSource(
    private val api: BasicNoteApi,
    private val token: String,
    private val query: String
) : PagingSource<Int, Note>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Note> {
        val position = params.key ?: NOTE_STARTING_PAGE_INDEX
        return try {
            val response = api.getMyNotes(position, "Bearer $token")
            val notes = response.data.data
            LoadResult.Page(
                data = notes,
                prevKey = if (position == NOTE_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (notes.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }


}