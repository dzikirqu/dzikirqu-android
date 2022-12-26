package com.dzikirqu.android.ui.main.bookmarks.prayer


import androidx.lifecycle.viewModelScope
import com.dzikirqu.android.base.BaseNavigator
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
import com.dzikirqu.android.model.Bookmark
import com.dzikirqu.android.model.BookmarkHighlightUpdate
import com.dzikirqu.android.model.BookmarkType
import com.dzikirqu.android.model.Prayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkPageViewModel @Inject constructor(dataManager: AppDataManager) :
    BaseViewModel<BaseNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
    }

    val quran = dataManager.mBookmarkDatabase.bookmarkDao().getBookmarksByType(BookmarkType.QURAN)
    val prayer = dataManager.mBookmarkDatabase.bookmarkDao().getBookmarksByType(BookmarkType.PRAYER)

    fun addBookmark(prayer: Prayer){
        viewModelScope.launch {
            dataManager.mBookmarkDatabase.bookmarkDao().putBookmark(
                Bookmark(
                    idString = prayer.id,
                    type = BookmarkType.PRAYER
                )
            )
        }
    }

    fun removeBookmark(prayer: Prayer){
        viewModelScope.launch {
            dataManager.mBookmarkDatabase.bookmarkDao().deleteBookmark(prayer.id, BookmarkType.PRAYER)
        }
    }

    fun removeBookmark(ayahId:String){
        viewModelScope.launch {
            dataManager.mBookmarkDatabase.bookmarkDao().deleteBookmark(ayahId, BookmarkType.QURAN)
        }
    }

    fun updateHighlight(bookmark: Bookmark){
        viewModelScope.launch {
            dataManager.mBookmarkDatabase.bookmarkDao().updateHighlight(BookmarkHighlightUpdate(bookmark.id ?: 0,
                !bookmark.highlighted))
        }
    }

}