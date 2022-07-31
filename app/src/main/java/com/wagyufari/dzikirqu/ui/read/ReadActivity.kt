package com.wagyufari.dzikirqu.ui.read

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.data.room.PersistenceDatabase
import com.wagyufari.dzikirqu.model.*
import com.wagyufari.dzikirqu.ui.read.hadith.ReadHadithFragment
import com.wagyufari.dzikirqu.ui.read.prayer.ReadPrayerFragment
import com.wagyufari.dzikirqu.ui.read.quran.fragments.hizb.ReadQuranHizbFragment
import com.wagyufari.dzikirqu.ui.read.quran.fragments.juz.ReadQuranJuzFragment
import com.wagyufari.dzikirqu.ui.read.quran.fragments.paged.ReadQuranPagedFragment
import com.wagyufari.dzikirqu.ui.read.quran.fragments.surah.ReadQuranSurahFragment
import com.wagyufari.dzikirqu.util.FileUtils
import com.wagyufari.dzikirqu.util.ViewUtils.getProgressDialog
import com.wagyufari.dzikirqu.util.io
import com.wagyufari.dzikirqu.util.main
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReadActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_TYPE = "type"
        private const val EXTRA_ID = "id"
        private const val EXTRA_ID2 = "id2"
        private const val EXTRA_OBJECT = "object"
        private const val EXTRA_OBJECT2 = "object2"
        private const val EXTRA_OBJECT3 = "object3"

        fun startPrayer(context: Context, prayer: Prayer) {
            context.startActivity(Intent(
                context, ReadActivity::class.java
            ).apply {
                putExtra(EXTRA_TYPE, ReadType.Prayer)
                putExtra(EXTRA_ID, prayer.id)
            })
        }

        fun startPrayer(context: Context, prayer: Int) {
            context.startActivity(Intent(
                context, ReadActivity::class.java
            ).apply {
                putExtra(EXTRA_TYPE, ReadType.Prayer)
                putExtra(EXTRA_ID, prayer.toString())
            })
        }

        fun startSurah(context: Context, surah: Surah) {
            context.startActivity(Intent(
                context, ReadActivity::class.java
            ).apply {
                putExtra(EXTRA_TYPE, ReadType.Surah)
                putExtra(EXTRA_ID, surah.id)
            })
        }

        fun newSurahIntent(context: Context, surah_id: Int? = 1, ayah: Int? = 0): Intent {
            return Intent(
                context, ReadActivity::class.java
            ).apply {
                putExtra(EXTRA_TYPE, ReadType.Surah)
                putExtra(EXTRA_ID, surah_id)
                putExtra(EXTRA_ID2, ayah)
            }
        }

        fun startSurah(context: Context, surah_id: Int? = 1, ayah: Int? = 0, isNoteDeeplink:Boolean?=false) {
            context.startActivity(Intent(
                context, ReadActivity::class.java
            ).apply {
                putExtra(EXTRA_TYPE, ReadType.Surah)
                putExtra(EXTRA_ID, surah_id)
                putExtra(EXTRA_ID2, ayah)
                putExtra(EXTRA_OBJECT, isNoteDeeplink)
            })
        }

        fun startJuz(context: Context, juz: Juz) {
            context.startActivity(Intent(
                context, ReadActivity::class.java
            ).apply {
                putExtra(EXTRA_TYPE, ReadType.Juz)
                putExtra(EXTRA_ID, juz.juz)
            })
        }

        fun startHizb(context: Context, hizb: Hizb) {
            context.startActivity(Intent(
                context, ReadActivity::class.java
            ).apply {
                putExtra(EXTRA_TYPE, ReadType.Hizb)
                putExtra(EXTRA_ID, hizb.hizb)
            })
        }

        fun startHadith(context: Context, hadith: Hadith, isNoteDeeplink: Boolean?=false) {
            context.startActivity(Intent(context, ReadActivity::class.java).apply {
                putExtra(EXTRA_TYPE, ReadType.Hadith)
                putExtra(EXTRA_OBJECT, hadith)
                putExtra(EXTRA_OBJECT2, isNoteDeeplink)
            })
        }

        fun startHadith(context:Context, hadithId:String, hadithIndex:Int){
            context.startActivity(Intent(context, ReadActivity::class.java).apply {
                putExtra(EXTRA_TYPE, ReadType.HadithSubChapter)
                putExtra(EXTRA_OBJECT, Hadith(
                    id = hadithId,
                    name = hadithId.locale(),
                    author = "",
                    description = arrayListOf(),
                    count = 1,
                    metadata = null
                ))
                putExtra(EXTRA_OBJECT2, HadithSubChapter(
                    title = arrayListOf(),
                    startIndex = hadithIndex,
                    endIndex = hadithIndex,
                    chapterIndex = null
                ))
            })
        }

        fun startHadith(context: Context, hadith: Hadith?, chapter: HadithChapter?, isNoteDeeplink:Boolean?=false) {
            context.startActivity(Intent(context, ReadActivity::class.java).apply {
                putExtra(EXTRA_TYPE, ReadType.HadithChapter)
                putExtra(EXTRA_OBJECT, hadith)
                putExtra(EXTRA_OBJECT2, chapter)
                putExtra(EXTRA_OBJECT3, isNoteDeeplink)
            })
        }

        fun startHadith(context: Context, hadith: Hadith?, chapter: HadithSubChapter?, isNoteDeeplink:Boolean?=false) {
            if (chapter?.startIndex == null) return
            context.startActivity(Intent(context, ReadActivity::class.java).apply {
                putExtra(EXTRA_TYPE, ReadType.HadithSubChapter)
                putExtra(EXTRA_OBJECT, hadith)
                putExtra(EXTRA_OBJECT2, chapter)
                putExtra(EXTRA_OBJECT3, isNoteDeeplink)
            })
        }

        fun startPage(context: Context, page: Int) {
            val ayahLineDao = PersistenceDatabase.getDatabase(context).ayahLineDao()
            context.io {
                if (ayahLineDao.getCount() == 6236) {
                    context.main {
                        startQuranReadPage(context, page)
                    }
                } else {
                    context.main {
                        val dialog = context.getProgressDialog(
                            LocaleConstants.PREPARING_QURAN_DATA.locale()
                        )
                        dialog.show()
                        if (ayahLineDao.getAyahLine().isEmpty()){
                            for (i in 1..604) {
                                ayahLineDao.putAyahLine(
                                    Gson().fromJson<ArrayList<AyahLine>>(
                                        FileUtils.getJsonStringFromAssets(
                                            context,
                                            "json/quran/paged/page$i.json"
                                        ) {},
                                        object : TypeToken<ArrayList<AyahLine>>() {}.type,
                                    ).map {
                                        it.apply {
                                            this.page = i
                                        }
                                    })
                            }
                        }
                        dialog.dismiss()
                        startQuranReadPage(context, page)
                    }
                }
            }
        }

        private fun startQuranReadPage(context: Context, page: Int) {
            context.startActivity(Intent(
                context, ReadActivity::class.java
            ).apply {
                putExtra(EXTRA_TYPE, ReadType.Page)
                putExtra(EXTRA_ID, page)
            })
        }

    }

    private lateinit var type: ReadType

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Prefs.colorTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)

        type = intent.getSerializableExtra(EXTRA_TYPE) as ReadType

        var fragment: Fragment? = null
        when (type) {
            ReadType.Prayer -> {
                fragment = ReadPrayerFragment.newInstance(intent.getStringExtra(EXTRA_ID).toString())
            }
            ReadType.Surah -> {
                fragment = ReadQuranSurahFragment(
                    surahId = intent.getIntExtra(EXTRA_ID, 1),
                    ayah = intent.getIntExtra(EXTRA_ID2, -1),
                    isNoteDeeplink = intent.getBooleanExtra(EXTRA_OBJECT, false)
                )
            }
            ReadType.Juz -> {
                fragment = ReadQuranJuzFragment(juz = intent.getIntExtra(EXTRA_ID, 1))
            }
            ReadType.Hizb -> {
                fragment = ReadQuranHizbFragment(hizb = intent.getFloatExtra(EXTRA_ID, 1f))

            }
            ReadType.Page -> {
                fragment = ReadQuranPagedFragment(page = intent.getIntExtra(EXTRA_ID, 1))

            }
            ReadType.Hadith -> {
                fragment = ReadHadithFragment.newInstance(
                    hadith = intent.getParcelableExtra(
                        EXTRA_OBJECT
                    ),
                    isNoteDeeplink = intent.getBooleanExtra(EXTRA_OBJECT2, false)
                )
            }
            ReadType.HadithChapter -> {
                val hadith = intent.getParcelableExtra<Hadith>(EXTRA_OBJECT)
                val chapter = intent.getParcelableExtra<HadithChapter>(EXTRA_OBJECT2)
                fragment = ReadHadithFragment.newInstance(
                    hadith = hadith,
                    chapter = chapter,
                    isNoteDeeplink = intent.getBooleanExtra(EXTRA_OBJECT3, false)
                )
            }
            ReadType.HadithSubChapter -> {
                val hadith = intent.getParcelableExtra<Hadith>(EXTRA_OBJECT)
                val chapter = intent.getParcelableExtra<HadithSubChapter>(EXTRA_OBJECT2)
                fragment = ReadHadithFragment.newInstance(
                    hadith = hadith,
                    chapter = chapter,
                    isNoteDeeplink = intent.getBooleanExtra(EXTRA_OBJECT3, false)
                )
            }
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.container,fragment)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        Prefs.isMarkQuranLastReadAutomatically = false
    }

}

enum class ReadType {
    Prayer,
    Surah,
    Juz,
    Hizb,
    Page,
    Hadith,
    HadithChapter,
    HadithSubChapter
}