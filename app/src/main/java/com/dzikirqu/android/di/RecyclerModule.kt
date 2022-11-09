package com.dzikirqu.android.di

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzikirqu.android.ui.adapters.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@Module
@InstallIn(ActivityComponent::class)
object
RecyclerModule {

    @Provides
    internal fun provideLinearLayoutManager(activity: Activity): LinearLayoutManager {
        return LinearLayoutManager(activity)
    }

    @Provides
    internal fun provideBookAdapter(): PrayerBookAdapter {
        return PrayerBookAdapter()
    }
    @Provides
    internal fun provideNoteAdapter(): NoteAdapter {
        return NoteAdapter()
    }

    @Provides
    internal fun provideKhatamIterationAdapter(): KhatamIterationAdapter {
        return KhatamIterationAdapter()
    }


    @Provides
    internal fun provideHadithAdapter(): HadithBookAdapter {
        return HadithBookAdapter()
    }

    @Provides
    internal fun providePrayerAdapter(): PrayerAdapter {
        return PrayerAdapter()
    }

    @Provides
    internal fun providePrayerReadAdapter(): PrayerReadAdapter {
        return PrayerReadAdapter()
    }
    @Provides
    internal fun providePrayerTypeGridAdapter(): PrayerTypeGridAdapter {
        return PrayerTypeGridAdapter()
    }

    @Provides
    internal fun provideSurahAdapter(): SurahAdapter {
        return SurahAdapter()
    }

    @Provides
    internal fun provideJuzAdapter(): JuzAdapter {
        return JuzAdapter()
    }

    @Provides
    internal fun provideQuranListedAdapter(): QuranListedAdapter {
        return QuranListedAdapter()
    }

    @Provides
    internal fun provideFlyerPagingAdapter(): FlyerPagingAdapter {
        return FlyerPagingAdapter()
    }

    @Provides
    internal fun provideFlyerAdapter(): FlyerAdapter {
        return FlyerAdapter()
    }


    @Provides
    internal fun provideBookmarkAdapter(): BookmarkAdapter {
        return BookmarkAdapter()
    }

    @Provides
    internal fun provideSearchQuranAdapter(): SearchQuranAdapter {
        return SearchQuranAdapter()
    }

    @Provides
    internal fun provideJumpQuranAdapter(): JumpQuranAdapter {
        return JumpQuranAdapter()
    }

    @Provides
    internal fun provideQuranLastReadAdapter(): QuranLastReadAdapter {
        return QuranLastReadAdapter()
    }

    @Provides
    internal fun provideHadithReadAdapter(): HadithReadAdapter {
        return HadithReadAdapter()
    }

    @Provides
    internal fun provideHadithChapterAdapter(): HadithChapterAdapter {
        return HadithChapterAdapter()
    }

    @Provides
    internal fun provideHadithSubChapterAdapter(): HadithSubChapterAdapter {
        return HadithSubChapterAdapter()
    }


}