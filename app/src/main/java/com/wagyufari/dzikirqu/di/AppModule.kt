package com.wagyufari.dzikirqu.di

import android.app.Application
import android.content.Context
import com.wagyufari.dzikirqu.data.ApiService
import com.wagyufari.dzikirqu.data.AppDataManager
import com.wagyufari.dzikirqu.data.QuranService
import com.wagyufari.dzikirqu.data.room.BookmarkDatabase
import com.wagyufari.dzikirqu.data.room.NotesDatabase
import com.wagyufari.dzikirqu.data.room.PersistenceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context = application

    @Provides
    @Singleton
    internal fun provideDataManager(context:Context, apiService: ApiService, persistenceDatabase: PersistenceDatabase, bookmarkDatabase: BookmarkDatabase, noteDatabase: NotesDatabase): AppDataManager {
        return AppDataManager(
            context,
            apiService,
            persistenceDatabase,
            bookmarkDatabase,
            noteDatabase
        )
    }
    @Provides
    @Singleton
    internal fun provideApiHelper(): ApiService = ApiService.create()

    @Provides
    @Singleton
    internal fun provideQuranHelper(): QuranService = QuranService.create()

    @Singleton
    @Provides
    fun providePersistence(@ApplicationContext appContext: Context) = PersistenceDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideBookmarks(@ApplicationContext appContext: Context) = BookmarkDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideNotes(@ApplicationContext appContext: Context) = NotesDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideBookDao(db: PersistenceDatabase) = db.prayerBookDao()

    @Singleton
    @Provides
    fun provideAyahLineDao(db: PersistenceDatabase) = db.ayahLineDao()
    @Singleton
    @Provides
    fun providePrayerDao(db: PersistenceDatabase) = db.prayerDao()
    @Singleton
    @Provides
    fun providePrayerDataDao(db: PersistenceDatabase) = db.prayerDataDao()
    @Singleton
    @Provides
    fun provideAyahDao(db: PersistenceDatabase) = db.ayahDao()
    @Singleton
    @Provides
    fun provideSurahDao(db: PersistenceDatabase) = db.surahDao()

    @Singleton
    @Provides
    fun provideHadithDataDao(db: PersistenceDatabase) = db.hadithDataDao()

    @Singleton
    @Provides
    fun provideBookmarkDao(db: BookmarkDatabase) = db.bookmarkDao()

}