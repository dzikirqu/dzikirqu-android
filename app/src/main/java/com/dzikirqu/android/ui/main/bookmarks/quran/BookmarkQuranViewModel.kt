package com.dzikirqu.android.ui.main.bookmarks.quran


import androidx.lifecycle.viewModelScope
import com.dzikirqu.android.base.BaseNavigator
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.AppDataManager
import com.dzikirqu.android.model.BookmarkType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkQuranViewModel @Inject constructor(dataManager: AppDataManager) :
    BaseViewModel<BaseNavigator>(dataManager) {

    override fun onEvent(obj: Any) {
    }

    val bookmarks = dataManager.mBookmarkDatabase.bookmarkDao().getBookmarksByType(BookmarkType.QURAN)

    val quranType = dataManager.mBookmarkDatabase.bookmarkGroupDao()

    fun removeBookmark(ayahId:String){
        viewModelScope.launch {
            dataManager.mBookmarkDatabase.bookmarkDao().deleteBookmark(ayahId, BookmarkType.QURAN)
        }
    }

}