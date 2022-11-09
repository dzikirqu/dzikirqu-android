package com.dzikirqu.android.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.data.room.PersistenceDatabase
import com.dzikirqu.android.data.room.dao.*
import com.dzikirqu.android.databinding.ActivitySplashBinding
import com.dzikirqu.android.model.*
import com.dzikirqu.android.ui.main.MainActivity
import com.dzikirqu.android.ui.note.detail.NoteDetailActivity
import com.dzikirqu.android.ui.onboarding.OnBoardActivity
import com.dzikirqu.android.ui.onboarding.fragments.OnBoardLoadingActivity
import com.dzikirqu.android.ui.read.ReadActivity
import com.dzikirqu.android.util.*
import com.dzikirqu.android.util.ViewUtils.getProgressDialog
import java.net.URI

class SplashActivity : AppCompatActivity() {

    lateinit var mBinding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Prefs.colorTheme)
        super.onCreate(savedInstanceState)
        mBinding = ActivitySplashBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            Prefs.statusBarHeight = insets.top
            WindowInsetsCompat.CONSUMED
        }
        configureDeeplink()
        refreshLocalData()
    }

    fun refreshLocalData() {
        val bookDao = getBookDao()
        val prayerDao = getPrayerDao()
        val prayerDataDao = getPrayerDataDao()
        val notePropertyDao = getNotePropertyDao()
        val dailyReminderDao = getDailyReminderDao()
        val dailyReminderParentDao = getDailyReminderParentDao()
        val bookmarkDao = getBookmarkDao()
        io {
            dailyReminderParentDao.deleteAllReminder()
            dailyReminderDao.deleteAllReminder()
            DailyReminder.configureDefault(this)

            val notes = notePropertyDao.getNotePropertiesSuspend().toHashSet()
            notePropertyDao.deleteAll()
            notes.forEach {
                notePropertyDao.putNoteProperty(it)
            }
            notePropertyDao.deleteNull()

            bookDao.deleteBooks()
            bookDao.putBooks(
                Gson().fromJson<ArrayList<PrayerBook>>(
                    FileUtils.getJsonStringFromAssets(this, "json/prayer/book.json"),
                    object : TypeToken<ArrayList<PrayerBook>>() {}.type
                )
            )
            prayerDao.deletePrayer()
            prayerDao.putPrayer(
                Gson().fromJson<ArrayList<Prayer>>(
                    FileUtils.getJsonStringFromAssets(this, "json/prayer/prayer.json"),
                    object : TypeToken<ArrayList<Prayer>>() {}.type
                )
            )
            prayerDataDao.deletePrayerData()
            prayerDataDao.putPrayerData(
                Gson().fromJson<ArrayList<PrayerData>>(
                    FileUtils.getJsonStringFromAssets(this, "json/prayer/prayerData.json"),
                    object : TypeToken<ArrayList<PrayerData>>() {}.type
                )
            )

            if (bookmarkDao.getHighlightsSuspend().isEmpty()) {
                bookmarkDao.putBookmark(Bookmark(
                    idString = "28",
                    type = BookmarkType.PRAYER,
                    highlighted = true,
                ))
                bookmarkDao.putBookmark(Bookmark(
                    type = BookmarkType.PRAYER,
                    highlighted = true,
                    idString = "5"
                ))
                bookmarkDao.putBookmark(Bookmark(
                    idString = "29",
                    type = BookmarkType.PRAYER,
                    highlighted = true,
                ))
                bookmarkDao.putBookmark(Bookmark(
                    idString = "34",
                    type = BookmarkType.PRAYER,
                    highlighted = true
                ))
            }
        }
    }

    fun configureDeeplink() {
        val data: Uri? = intent?.data
        data?.let {
            val url = it.toString()
            if (URI(url).path.contains("quran")) {
                // https://dzikirqu.com/quran/19/4
                if (url.getPath2FromUrlNullable() != null) {
                    ReadActivity.startSurah(this, url.getPath1FromUrl(), url.getPath2FromUrl())
                    finish()
                    // https://dzikirqu.com/quran/19/
                } else if (url.getPath1FromUrlNullable() != null) {
                    ReadActivity.startSurah(this, url.getPath1FromUrl())
                    finish()
                }
            } else if (URI(url).path.contains("prayer")) {
                if (url.getPath1FromUrlNullable() != null) {
                    ReadActivity.startPrayer(this, url.getPath1FromUrl())
                    finish()
                }
            } else if (URI(url).path.contains("riyadussalihin")) {
                if (url.getPath1FromUrlNullable() != null) {
                    ReadActivity.startHadith(this, "riyadussalihin", url.getPath1FromUrl())
                    finish()
                }
            } else if (URI(url).path.contains("nawawi40")) {
                if (url.getPath1FromUrlNullable() != null) {
                    ReadActivity.startHadith(this, "nawawi40", url.getPath1FromUrl())
                    finish()
                }
            } else if (URI(url).path.contains("note")) {
                val dialog = getProgressDialog("Loading...")
                dialog.show()
                getNoteFromReference(url.getPath3StringFromUrl(), {
                    startActivity(NoteDetailActivity.newIntent(this, it))
                    finish()
                    dialog.dismiss()
                }, {
                    dialog.dismiss()
                    finish()
                })
            }
        } ?: kotlin.run {
            io {
                configureLoadedStatus()
                if (Prefs.isFirstRun) {
                    startActivity(Intent(this, OnBoardActivity::class.java))
                    finish()
                } else if (!Prefs.isBookLoaded && !Prefs.isPrayerLoaded && !Prefs.isQuranLoaded && !Prefs.isHadithLoaded) {
                    startActivity(Intent(this, OnBoardLoadingActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }

    suspend fun configureLoadedStatus() {
        if (PersistenceDatabase.getDatabase(this).prayerBookDao().getBooksSuspend().isEmpty()) {
            Prefs.isBookLoaded = false
        }

        if (PersistenceDatabase.getDatabase(this).prayerDao().getPrayerSuspend().isEmpty()) {
            Prefs.isPrayerLoaded = false
        }

        if (PersistenceDatabase.getDatabase(this).ayahDao().getAyah().isEmpty()) {
            Prefs.isQuranLoaded = false
        }

        if (PersistenceDatabase.getDatabase(this).hadithDataDao().getHadith().isEmpty()) {
            Prefs.isHadithLoaded = false
        }
    }

}