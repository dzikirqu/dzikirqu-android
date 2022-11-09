package com.dzikirqu.android.model

import android.content.Context
import android.widget.Toast
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.data.room.dao.getBookmarkDao
import com.dzikirqu.android.util.io
import com.dzikirqu.android.util.main

@Entity(tableName = "ayah")
data class Ayah(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val verse_number:Int,
    @ColumnInfo(name = "text")
    var text: ArrayList<LanguageString> = ArrayList(),
    @ColumnInfo(name = "juz")
    var juz:Int,
    @ColumnInfo(name = "hizb")
    var hizb:Float,
    @ColumnInfo(name = "use_bismillah")
    var use_bismillah:Boolean?=false,
    @ColumnInfo(name = "chapter_id")
    var chapterId:Int
){
    @Ignore
    val wbw:ArrayList<AyahWbw> = arrayListOf()

    companion object{
        fun Ayah.bookmark(context:Context){
            context.apply {
                io {
                    val isBookmarked = getBookmarkDao()
                        .getBookmarkByIdSuspend(id.toString()) != null
                    if (!isBookmarked) {
                        getBookmarkDao().putBookmark(
                            Bookmark(
                                idString = id.toString(),
                                type = BookmarkType.QURAN
                            )
                        )
                        main {
                            Toast.makeText(
                                context,
                                LocaleConstants.AYAH_ADDED_TO_BOOKMARK.locale(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        getBookmarkDao()
                            .deleteBookmark(id.toString(), BookmarkType.QURAN)
                        main {
                            Toast.makeText(
                                context,
                                LocaleConstants.AYAH_REMOVED_FROM_BOOKMARK.locale(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }
}

@Entity(tableName = "ayahLine")
data class AyahLine(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    @ColumnInfo(name = "words")
    val words: List<AyahLineWord> = arrayListOf(),
    @ColumnInfo(name = "verse_number")
    val verse_number: Int? = null,
    @ColumnInfo(name = "verse_key")
    val verse_key:String?=null,
    @ColumnInfo(name = "juz_number")
    val juz_number: Int? = null,
    @ColumnInfo(name = "page")
    var page:Int?=null,
    @ColumnInfo(name = "hizb_number")
    val hizb_number: Int? = null,
)


data class AyahWbw(
    val surah: Int,
    val ayah: Int,
    val index: Int,
    val text: ArrayList<LanguageString>
)

data class AyahLineWord(
    val line_number: Int? = null,
    val id: Int? = null,
    val position: Int? = null,
    var verse_key:String?=null,
    val code_v1: String? = null,
    var page:Int?=null,
    var chapter_number:Int,
    val surah:Surah?=null,
    val is_bismillah:Boolean?=false
)