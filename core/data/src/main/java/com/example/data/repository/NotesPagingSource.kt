package com.example.data.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.database.dao.NoteDao
import com.example.model.Note
import kotlinx.coroutines.delay

class NotesPagingSource(private val noteDao: NoteDao) : PagingSource<Int, Note>() {
    override fun getRefreshKey(state: PagingState<Int, Note>): Int? {
        Log.d("tuanminh", "getRefreshKey: ${state.anchorPosition}")
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Note> {
        val page = params.key ?: STARTING_KEY
        val range = params.loadSize
        val offset = page * range

        return try {
            val notes = noteDao.getAllNoteEntity(limit = range, offset).map {
                it.toNote()
            }
            Log.d("tuanminh", "load: $notes - $offset - $range ")

            if (page != 0) delay(1000)
            LoadResult.Page(
                data = notes,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (notes.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Log.d("tuanminh", "load: $e")
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val STARTING_KEY = 0
        const val PAGE_SIZE = 5
    }
}