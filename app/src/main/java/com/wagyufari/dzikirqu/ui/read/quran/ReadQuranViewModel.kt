package com.wagyufari.dzikirqu.ui.read.quran

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wagyufari.dzikirqu.VersesItem
import com.wagyufari.dzikirqu.base.BaseViewModel
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.AppDataManager
import com.wagyufari.dzikirqu.data.room.dao.AyahDao
import com.wagyufari.dzikirqu.data.room.dao.SurahDao
import com.wagyufari.dzikirqu.model.Ayah
import com.wagyufari.dzikirqu.model.Surah
import com.wagyufari.dzikirqu.model.events.CurrentAyahEvent
import com.wagyufari.dzikirqu.model.events.QuranScrollEvent
import com.wagyufari.dzikirqu.model.identifiers.BismillahObject
import com.wagyufari.dzikirqu.model.identifiers.JuzObject
import com.wagyufari.dzikirqu.model.identifiers.SurahObject
import com.wagyufari.dzikirqu.util.StringExt.getText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReadQuranViewModel @Inject constructor(
    dataManager: AppDataManager,
    val ayahDao: AyahDao,
    val surahDao: SurahDao
) :
    BaseViewModel<ReadQuranNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
        when (obj) {
            is CurrentAyahEvent -> {
                viewModelScope.launch {
                    currentAyah.postValue(obj.ayah)
                    currentSurah.postValue(surahDao.getSurahByIdSingle(obj.surah?:1))
                    readCurrentAyahInPagedMode.postValue(String.format(LocaleConstants.READ_N_IN_PAGED_MODE.locale(), "${currentSurah.value?.name} ${currentAyah.value}"))
                }
            }
            is QuranScrollEvent->{
                isBottomSheetVisible.value = false
            }
        }
    }

    val isBottomSheetVisible = MutableLiveData(false)

    val textKhatamIndex = MutableLiveData("")
    val textKhatamProgress = MutableLiveData("")
    val textKhatamProgressInt = MutableLiveData(0)
    val textKhatamProgressPercentage = MutableLiveData("")
    val textKhatamLastRead = MutableLiveData("")

    val isFooterLocked = MutableLiveData(false)

    fun onClickLock(){
        isFooterLocked.value = isFooterLocked.value?.not()
    }

    val readCurrentAyahInPagedMode = MutableLiveData("Read Al-Fatihah 4 in paged mode")

    val currentSurah = MutableLiveData<Surah>()
    val currentAyah: MutableLiveData<Int> = MutableLiveData(1)

    val surah = liveData(IO) {
        try {
            emit(surahDao.getSurah())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
    val juz = liveData(IO) {
        try {
            emit(dataManager.getJuz())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    val title = MutableLiveData("")
    val subtitle = MutableLiveData("")

    fun getPage(page: Int): ArrayList<VersesItem> {
        return dataManager.getQuranByPage(page)
    }

    suspend fun getAyah(query: AyahQuery): ArrayList<Any> {
        val data = when (query.type) {
            AyahQueryType.Surah -> ayahDao.getAyahBySurahId(query.id.toInt())
            AyahQueryType.Juz -> ayahDao.getAyahByJuz(query.id.toInt(), 9999)
            AyahQueryType.Hizb -> ayahDao.getAyahByHizb(query.id, 9999)
        }
        return arrayListOf<Any>().apply {
            var currentJuz = -1
            data.forEach {
                if (currentJuz != it.juz) {
                    currentJuz = it.juz
                    if (dataManager.isFirstAyahOfJuz(it.juz, it.chapterId, it.verse_number)) {
                        add(JuzObject(it.juz))
                    }
                }
                if (it.verse_number == 1) {
                    add(SurahObject(surahDao.getSurahById(it.chapterId).first()))
                }
                if (it.use_bismillah == true) {
                    add(BismillahObject())
                }
                add(it)
            }
        }
    }

    fun onClickLastRead(){
        navigator?.onClickLastRead()
    }

    fun onClickSettings() {
        navigator?.onClickSettings()
    }

    fun onClickPage() {
        navigator?.onClickPage()
    }

    fun setUpSubtitle(recyclerView: RecyclerView, data: ArrayList<Any>) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (data.isNotEmpty()) {
                    CoroutineScope(IO).launch {
                        setSubtitle(
                            data,
                            (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                        )
                    }
                }
            }
        })
    }

    suspend fun setSubtitle(data: ArrayList<Any>, position: Int): Surah? {
        if (position >= 0) {
            val ayah = data[position]
            if (ayah is Ayah) {
                val surah = surahDao.getSurahById(ayah.chapterId).firstOrNull()
                title.postValue(surah?.name)
                subtitle.postValue(surah?.translation?.getText())
                return surah
            } else {
                setSubtitle(data, position + 1)
            }
        } else {
            setSubtitle(data, position + 1)
        }
        return null
    }
}

@Parcelize
data class AyahQuery(val id: Float, val type: AyahQueryType) : Parcelable

@Parcelize
enum class AyahQueryType : Parcelable {
    Surah,
    Juz,
    Hizb
}
