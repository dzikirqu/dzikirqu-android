package com.wagyufari.dzikirqu.data

import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.constants.ReadModeConstants
import com.wagyufari.dzikirqu.constants.RingType
import com.wagyufari.dzikirqu.model.NoteSortBy
import com.wagyufari.dzikirqu.model.PrayerTime
import com.wagyufari.dzikirqu.model.QuranLastRead
import com.wagyufari.dzikirqu.model.QuranReminderNotificationType

object Prefs {

    const val PREF_KEY_ACCESS_TOKEN = "pref_key_access_token"

    const val PREF_KEY_LANGUAGE = "pref_language"
    const val PREF_KEY_PRAYERTIME = "pref_praytime"
    const val PREF_KEY_USER_COORDINATES = "pref_user_coordinates"
    const val PREF_KEY_USER_CITY = "pref_user_city"
    const val PREF_KEY_QURAN_LAST_READ = "pref_quran_last_read_1"
    const val PREF_KEY_QURAN_BOOKMARK = "pref_key_quran_bookmarks"
    const val PREF_KEY_IS_QURAN_LOADED = "pref_key_is_quran_loaded"
    const val PREF_KEY_IS_HADITH_LOADED = "pref_key_is_hadith_loaded"
    const val PREF_KEY_IS_FIRST_RUN = "pref_key_is_first_run"

    const val PREF_KEY_IS_BOOK_LOADED = "pref_key_is_book_loaded"
    const val PREF_KEY_IS_PRAYER_LOADED = "pref_key_is_book_loaded"
    const val PREF_KEY_IS_SHOW_HIZB = "pref_key_is_show_hizb"

    const val PREF_KEY_ARABIC_TEXT_SIZE = "pref_key_arabic_text_size"
    const val PREF_KEY_ARABIC_FONT = "pref_key_arabic_font"
    const val PREF_KEY_TRANSLATION_TEXT_SIZE = "pref_key_translation_text_size"
    const val PREF_KEY_NOTES_TEXT_SIZE = "pref_key_notes_text_size"
    const val PREF_KEY_TRANSLATION = "pref_key_translation"
    const val PREF_KEY_ARABIC = "pref_key_arabic"

    const val PREF_KEY_MARK_QURAN_LAST_READ_AUTOMATICALLY =
        "pref_key_mark_quran_last_read_automatically"

    const val PREF_KEY_RING_FAJR = "pref_adzan_fajr"
    const val PREF_KEY_RING_DHUHR = "pref_adzan_dhuhr"
    const val PREF_KEY_RING_ASR = "pref_adzan_asr"
    const val PREF_KEY_RING_MAGHRIB = "pref_adzan_maghrib"
    const val PREF_KEY_RING_ISYA = "pref_adzan_isya"

    const val PREF_KEY_COLOR = "pref_key_color"
    const val PREF_KEY_THEME = "pref_key_theme"

    const val PREF_KEY_CALCULATION_METHOD = "pref_key_calculation_method"
    const val PREF_KEY_ASR_JURISTIC = "pref_key_asr_juristic"
    const val PREF_KEY_HIGHER_LATITUDES = "pref_key_higher_latitudes"
    const val PREF_KEY_MUADZIN = "pref_key_muadzin"
    const val PREF_KEY_FAJR_OFFSET = "pref_key_fajr_offset1"
    const val PREF_KEY_DHUHR_OFFSET = "pref_key_dhuhr_offset1"
    const val PREF_KEY_ASR_OFFSET = "pref_key_asr_offset1"
    const val PREF_KEY_MAGHRIB_OFFSET = "pref_key_maghrib_offset1"
    const val PREF_KEY_ISYA_OFFSET = "pref_key_isya_offset1"

    const val PREF_KEY_QURAN_WBW = "pref_key_quran_wbw"

    const val PREF_KEY_DEFAULT_QURAN_READ_MODE = "pref_key_default_quran_read_mode"
    const val PREF_KEY_DEFAULT_HADITH_READ_MODE = "pref_key_default_hadith_read_mode"

    const val PREF_KEY_KHATAM_CALCULATION_METHOD = "pref_key_khatam_calculation_method"
    const val PREF_KEY_KHATAM_REMINDER_TYPE = "pref_key_khatam_reminder_type"
    const val PREF_KEY_KHATAM_NOTIFICATION = "pref_key_khatam_notification"

    const val PREFS_KEY_NOTE_VIEW_MODE = "pref_key_note_view_mode"
    const val PREFS_KEY_NOTE_SORT_BY = "pref_key_note_sort_by"
    const val PREF_KEY_IS_NOTE_SORT_ASC = "pref_is_note_sort_asc"

    const val PREF_KEY_QURAN_FILTER_MODE = "pref_key_quran_filter_mode"

    const val PREF_KEY_NOTE_WYSIWYG_ENABLED = "pref_key_note_wysiwyg_enabled"

    const val PREF_KEY_SUSPENDED_NOTE_CONTENT = "pref_key_suspended_note_content"
    const val PREF_KEY_SUSPENDED_NOTE_ID = "pref_key_suspended_note_id"

    const val PREF_KEY_STATUSBAR_HEIGHT = "pref_key_statusbar_height"

    var statusBarHeight: Int
        get() = Prefs.getInt(PREF_KEY_STATUSBAR_HEIGHT, 0)
        set(value) {
            Prefs.putInt(PREF_KEY_STATUSBAR_HEIGHT, value)
        }

    var suspendedNoteContent: String
        get() = Prefs.getString(PREF_KEY_SUSPENDED_NOTE_CONTENT)
        set(value) {
            Prefs.putString(PREF_KEY_SUSPENDED_NOTE_CONTENT, value)
        }

    var suspendedNoteId: Int
        get() = Prefs.getInt(PREF_KEY_SUSPENDED_NOTE_ID)
        set(value) {
            Prefs.putInt(PREF_KEY_SUSPENDED_NOTE_ID, value)
        }

    var khatamReminderType: QuranReminderNotificationType
        get() = QuranReminderNotificationType.valueOf(
            Prefs.getString(
                PREF_KEY_KHATAM_REMINDER_TYPE,
                QuranReminderNotificationType.EVERY_1_HOUR.name
            )
        )
        set(value) {
            Prefs.putString(PREF_KEY_KHATAM_REMINDER_TYPE, value.name)
        }

    var khatamReminderSound: Int
        get() = Prefs.getInt(PREF_KEY_KHATAM_NOTIFICATION, 0)
        set(value) {
            Prefs.putInt(PREF_KEY_KHATAM_NOTIFICATION, value)
        }

    var quranWbwEnabled: Boolean
        get() = Prefs.getBoolean(PREF_KEY_QURAN_WBW, false)
        set(value) {
            Prefs.putBoolean(PREF_KEY_QURAN_WBW, value)
        }

    var defaultQuranReadMode: String
        get() = Prefs.getString(PREF_KEY_DEFAULT_QURAN_READ_MODE, ReadModeConstants.VERTICAL)
        set(value) {
            Prefs.putString(PREF_KEY_DEFAULT_QURAN_READ_MODE, value)
        }
    var defaultHadithReadMode: String
        get() = Prefs.getString(PREF_KEY_DEFAULT_HADITH_READ_MODE, ReadModeConstants.PAGED)
        set(value) {
            Prefs.putString(PREF_KEY_DEFAULT_HADITH_READ_MODE, value)
        }

    var fajrOffset: Int
        get() = Prefs.getInt(PREF_KEY_FAJR_OFFSET, 0)
        set(value) {
            Prefs.putInt(PREF_KEY_FAJR_OFFSET, value)
        }
    var dhuhrOffset: Int
        get() = Prefs.getInt(PREF_KEY_DHUHR_OFFSET, 0)
        set(value) {
            Prefs.putInt(PREF_KEY_DHUHR_OFFSET, value)
        }
    var asrOffset: Int
        get() = Prefs.getInt(PREF_KEY_ASR_OFFSET, 0)
        set(value) {
            Prefs.putInt(PREF_KEY_ASR_OFFSET, value)
        }
    var maghribOffset: Int
        get() = Prefs.getInt(PREF_KEY_MAGHRIB_OFFSET, 0)
        set(value) {
            Prefs.putInt(PREF_KEY_MAGHRIB_OFFSET, value)
        }
    var isyaOffset: Int
        get() = Prefs.getInt(PREF_KEY_ISYA_OFFSET, 0)
        set(value) {
            Prefs.putInt(PREF_KEY_ISYA_OFFSET, value)
        }

    var muadzin: Int
        get() = Prefs.getInt(PREF_KEY_MUADZIN, R.raw.mansour_zahrany)
        set(value) {
            Prefs.putInt(PREF_KEY_MUADZIN, value)
        }

    var calculationMethod: Int
        // Default Kemenag
        get() = Prefs.getInt(PREF_KEY_CALCULATION_METHOD, 6)
        set(value) {
            Prefs.putInt(PREF_KEY_CALCULATION_METHOD, value)
        }

    var asrJuristic: Int
        // Default Shafii (Standard)
        get() = Prefs.getInt(PREF_KEY_ASR_JURISTIC, 0)
        set(value) {
            Prefs.putInt(PREF_KEY_ASR_JURISTIC, value)
        }

    var higherLatitudes: Int
        // Default AngleBased
        get() = Prefs.getInt(PREF_KEY_HIGHER_LATITUDES, 3)
        set(value) {
            Prefs.putInt(PREF_KEY_HIGHER_LATITUDES, value)
        }


    var colorTheme: Int
        get() = Prefs.getInt(PREF_KEY_COLOR, R.style.AppTheme)
        set(value) {
            Prefs.putInt(PREF_KEY_COLOR, value)
        }

    var appTheme: Int
        get() = Prefs.getInt(PREF_KEY_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        set(value) {
            Prefs.putInt(PREF_KEY_THEME, value)
        }

    var ringFajr: Int
        get() = Prefs.getInt(PREF_KEY_RING_FAJR, RingType.SOUND)
        set(value) {
            Prefs.putInt(PREF_KEY_RING_FAJR, value)
        }
    var ringDhuhr: Int
        get() = Prefs.getInt(PREF_KEY_RING_DHUHR, RingType.SOUND)
        set(value) {
            Prefs.putInt(PREF_KEY_RING_DHUHR, value)
        }
    var ringAsr: Int
        get() = Prefs.getInt(PREF_KEY_RING_ASR, RingType.SOUND)
        set(value) {
            Prefs.putInt(PREF_KEY_RING_ASR, value)
        }
    var ringMaghrib: Int
        get() = Prefs.getInt(PREF_KEY_RING_MAGHRIB, RingType.SOUND)
        set(value) {
            Prefs.putInt(PREF_KEY_RING_MAGHRIB, value)
        }
    var ringIsya: Int
        get() = Prefs.getInt(PREF_KEY_RING_ISYA, RingType.SOUND)
        set(value) {
            Prefs.putInt(PREF_KEY_RING_ISYA, value)
        }

    var isUseTranslation: Boolean
        get() = Prefs.getBoolean(PREF_KEY_TRANSLATION, true)
        set(value) {
            Prefs.putBoolean(PREF_KEY_TRANSLATION, value)
        }

    var isUseArabic: Boolean
        get() = Prefs.getBoolean(PREF_KEY_ARABIC, true)
        set(value) {
            Prefs.putBoolean(PREF_KEY_ARABIC, value)
        }

    var isMarkQuranLastReadAutomatically: Boolean
        get() = Prefs.getBoolean(PREF_KEY_MARK_QURAN_LAST_READ_AUTOMATICALLY, false)
        set(value) {
            Prefs.putBoolean(PREF_KEY_MARK_QURAN_LAST_READ_AUTOMATICALLY, value)
        }

    var quranLastRead: QuranLastRead
        get() = Gson().fromJson(
            Prefs.getString(
                PREF_KEY_QURAN_LAST_READ,
                Gson().toJson(QuranLastRead(1, 1))
            ), QuranLastRead::class.java
        )
        set(value) {
            Prefs.putString(PREF_KEY_QURAN_LAST_READ, Gson().toJson(value))
        }

    var arabicTextSize: Float
        get() = Prefs.getFloat(PREF_KEY_ARABIC_TEXT_SIZE, 24f)
        set(value) {
            Prefs.putFloat(PREF_KEY_ARABIC_TEXT_SIZE, value)
        }

    var arabicFont: String
        get() = Prefs.getString(PREF_KEY_ARABIC_FONT, "fonts/uthman.otf")
        set(value) {
            Prefs.putString(PREF_KEY_ARABIC_FONT, value)
        }

    var translationTextSize: Float
        get() = Prefs.getFloat(PREF_KEY_TRANSLATION_TEXT_SIZE, 16f)
        set(value) {
            Prefs.putFloat(PREF_KEY_TRANSLATION_TEXT_SIZE, value)
        }

    var notesTextSize: Float
        get() = Prefs.getFloat(PREF_KEY_NOTES_TEXT_SIZE, 16f)
        set(value) {
            Prefs.putFloat(PREF_KEY_NOTES_TEXT_SIZE, value)
        }

    var isQuranLoaded: Boolean
        get() = Prefs.getBoolean(PREF_KEY_IS_QURAN_LOADED, false)
        set(value) {
            Prefs.putBoolean(PREF_KEY_IS_QURAN_LOADED, value)
        }

    var isHadithLoaded: Boolean
        get() = Prefs.getBoolean(PREF_KEY_IS_HADITH_LOADED, false)
        set(value) {
            Prefs.putBoolean(PREF_KEY_IS_HADITH_LOADED, value)
        }

    var isFirstRun: Boolean
        get() = Prefs.getBoolean(PREF_KEY_IS_FIRST_RUN, true)
        set(value) {
            Prefs.putBoolean(PREF_KEY_IS_FIRST_RUN, value)
        }

    var isBookLoaded: Boolean
        get() = Prefs.getBoolean(PREF_KEY_IS_BOOK_LOADED, false)
        set(value) {
            Prefs.putBoolean(PREF_KEY_IS_BOOK_LOADED, value)
        }
    var isPrayerLoaded: Boolean
        get() = Prefs.getBoolean(PREF_KEY_IS_PRAYER_LOADED, false)
        set(value) {
            Prefs.putBoolean(PREF_KEY_IS_PRAYER_LOADED, value)
        }

    var isShowHizb: Boolean
        get() = Prefs.getBoolean(PREF_KEY_IS_SHOW_HIZB, false)
        set(value) {
            Prefs.putBoolean(PREF_KEY_IS_SHOW_HIZB, value)
        }

    var language: String
        get() = Prefs.getString(PREF_KEY_LANGUAGE, "english")
        set(value) {
            Prefs.putString(PREF_KEY_LANGUAGE, value)
        }

    var praytime: PrayerTime
        get() = Gson().fromJson(
            Prefs.getString(
                PREF_KEY_PRAYERTIME, Gson().toJson(
                    PrayerTime(
                        null,
                        "00:00",
                        "00:00",
                        "00:00",
                        "00:00",
                        "00:00",
                        "00:00",
                        "00:00"
                    )
                )
            ),
            PrayerTime::class.java
        )
        set(value) {
            Prefs.putString(PREF_KEY_PRAYERTIME, Gson().toJson(value))
        }
    var userCoordinates: LatLng
        get() = Gson().fromJson(
            Prefs.getString(
                PREF_KEY_USER_COORDINATES,
                Gson().toJson(LatLng(0.0, 0.0))
            ), LatLng::class.java
        )
        set(value) {
            Prefs.putString(PREF_KEY_USER_COORDINATES, Gson().toJson(value))
        }
    var userCity: String
        get() = Prefs.getString(PREF_KEY_USER_CITY, "")
        set(value) {
            Prefs.putString(PREF_KEY_USER_CITY, value)
        }

    var noteViewMode: Int
        get() = Prefs.getInt(PREFS_KEY_NOTE_VIEW_MODE, 0)
        set(value) {
            Prefs.putInt(PREFS_KEY_NOTE_VIEW_MODE, value)
        }
    var noteSortBy: Int
        get() = Prefs.getInt(PREFS_KEY_NOTE_SORT_BY, NoteSortBy.UPDATED_DATE)
        set(value) {
            Prefs.putInt(PREFS_KEY_NOTE_SORT_BY, value)
        }
    var isNoteSortAsc: Boolean
        get() = Prefs.getBoolean(PREF_KEY_IS_NOTE_SORT_ASC, false)
        set(value) {
            Prefs.putBoolean(PREF_KEY_IS_NOTE_SORT_ASC, value)
        }
    var quranFilterMode: Int
        get() = Prefs.getInt(PREF_KEY_QURAN_FILTER_MODE, 0)
        set(value) {
            Prefs.putInt(PREF_KEY_QURAN_FILTER_MODE, value)
        }

    var noteWYSIWYGEnabled: Boolean
        get() = Prefs.getBoolean(PREF_KEY_NOTE_WYSIWYG_ENABLED, false)
        set(value) {
            Prefs.putBoolean(PREF_KEY_NOTE_WYSIWYG_ENABLED, value)
        }

    var accessToken: String?
        get() = Prefs.getString(PREF_KEY_ACCESS_TOKEN, null)
        set(value) {
            Prefs.putString(PREF_KEY_ACCESS_TOKEN, value)
        }

}