package com.wagyufari.dzikirqu.ui.bsd.quranmenu

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseDialog
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.data.room.PersistenceDatabase
import com.wagyufari.dzikirqu.data.room.dao.getAyahDao
import com.wagyufari.dzikirqu.data.room.dao.getKhatamDao
import com.wagyufari.dzikirqu.data.room.dao.getQuranLastReadDao
import com.wagyufari.dzikirqu.data.room.dao.getSurahDao
import com.wagyufari.dzikirqu.databinding.QuranMenuBsdBinding
import com.wagyufari.dzikirqu.model.Ayah.Companion.bookmark
import com.wagyufari.dzikirqu.model.QuranLastRead
import com.wagyufari.dzikirqu.model.events.MenuEvent
import com.wagyufari.dzikirqu.model.getOngoing
import com.wagyufari.dzikirqu.model.update
import com.wagyufari.dzikirqu.ui.share.ShareImageActivity
import com.wagyufari.dzikirqu.util.*
import com.wagyufari.dzikirqu.util.StringExt.getArabic
import com.wagyufari.dzikirqu.util.StringExt.getText
import com.wagyufari.dzikirqu.util.ViewUtils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class QuranMenuBSD() :
    BaseDialog<QuranMenuBsdBinding, QuranMenuBSDViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.quran_menu_bsd
    override val viewModel: QuranMenuBSDViewModel by viewModels()

    var surahName = ""

    var surah: Int = 1
    var ayah: Int = 1
    var page: Int? = null

    companion object {

        const val ARG_SURAH = "surah"
        const val ARG_AYAH = "ayah"
        const val ARG_PAGE = "page"

        fun newInstance(surah: Int, ayah: Int, page: Int? = null): QuranMenuBSD {
            return QuranMenuBSD().apply {
                this.arguments = bundleOf(ARG_SURAH to surah, ARG_AYAH to ayah, ARG_PAGE to page)
            }
        }

        fun QuranMenuBSD.getSurah(): Int {
            return arguments?.getInt(ARG_SURAH) ?: 1
        }

        fun QuranMenuBSD.getAyah(): Int {
            return arguments?.getInt(ARG_AYAH) ?: 1
        }

        fun QuranMenuBSD.getPage(): Int? {
            return arguments?.getInt(ARG_PAGE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        surah = getSurah()
        ayah = getAyah()
        page = getPage()

        viewDataBinding?.lastRead?.setOnClickListener {
            getKhatamDao().getKhatam().observe(viewLifecycleOwner) {
                it.getOngoing().let { khatam ->
                    khatam?.iteration?.getOngoing()?.let { lastRead->
                        io {
                            val currentAyahId = getAyahDao().getAyahBySurahIdAndVerseNumber(
                                lastRead.surah,
                                lastRead.ayah
                            )
                                .firstOrNull()?.id ?: 0
                            val selectedAyahId = getAyahDao().getAyahBySurahIdAndVerseNumber(surah, ayah).firstOrNull()?.id ?: 0

                            if (selectedAyahId < currentAyahId){
                                main {
                                    AlertDialog.Builder(requireActivity()).create().apply {
                                        setTitle(LocaleConstants.Confirmation.locale())
                                        setMessage(LocaleConstants.MARKING_THIS_AYAH_AS_LAST_READ_WILL_DECREASE_YOUR_KHATAM_PROGRESS.locale())
                                        setButton(AlertDialog.BUTTON_POSITIVE, LocaleConstants.OKAY.locale()){ dialog, id->
                                            io{
                                                putLastRead()
                                                getKhatamDao().updateKhatam(khatam.update(getSurah(),getAyah(),getPage()))
                                                dismiss()
                                            }
                                        }
                                        setButton(AlertDialog.BUTTON_NEGATIVE, LocaleConstants.CANCEL.locale()){ dialog, id->
                                            dismiss()
                                        }
                                    }.show()
                                }
                            } else{
                                putLastRead()
                                getKhatamDao().updateKhatam(khatam.update(getSurah(),getAyah(),getPage()))
                            }
                        }

                    }
                } ?: kotlin.run {
                    putLastRead()
                }
            }
        }

        viewDataBinding?.copy?.setOnClickListener {
            io {
                getAyahDao()
                    .getAyahBySurahIdAndVerseNumber(surah, ayah).firstOrNull()?.let {
                        main {
                            requireActivity().copy("${it.text.getArabic()}\n${it.text.getText()}\n\n(${surahName}:${ayah})\n\nCopied from DzikirQu")
                            Toast.makeText(
                                requireActivity(),
                                LocaleConstants.COPY_AYAH.locale(),
                                Toast.LENGTH_SHORT
                            ).show()
                            dismiss()
                        }
                    }
            }
        }

        viewDataBinding?.share?.setOnClickListener {
            io {
                val surahName = getSurahDao().getSurahById(surah).firstOrNull()?.name
                val text = getAyahDao().getAyahBySurahIdAndVerseNumber(surah, ayah).first().text
                val arabic = text.getArabic()
                val translation = text.getText()
                main {
                    ShareImageActivity.start(
                        requireActivity(),
                        arabic,
                        translation,
                        "(${surahName}: ${ayah})"
                    )
                    dismiss()
                }
            }
        }

        viewDataBinding?.bookmark?.setOnClickListener {
            lifecycleScope.launch {
                getAyahDao().getAyahBySurahIdAndVerseNumber(surah, ayah).firstOrNull()?.bookmark(requireActivity())
                dismiss()
            }
        }

        io {

            getSurahDao().getSurahById(surah).firstOrNull()?.let {
                main {
                    viewDataBinding?.title?.text = "${it.name}, Ayat ${ayah}"
                    surahName = it.name
                }
            }
            val ayahId = getAyahDao().getAyahBySurahIdAndVerseNumber(ayah, surah).firstOrNull()?.id

            page?.let {
                PersistenceDatabase.getDatabase(requireActivity()).ayahDao().getAyahBySurahId(surah)
                    .filter { it.verse_number == ayah }.firstOrNull()?.let {
                        main {
                            viewDataBinding?.viewTranslation?.show()
                            viewDataBinding?.textTranslation?.text = it.text.getText()
                        }
                    }
            }
            viewModel.isBookmarked.postValue(
                viewModel.dataManager.mBookmarkDatabase.bookmarkDao()
                    .getBookmarkByIdSuspend(ayahId.toString()) != null
            )
        }
    }

    fun putLastRead() {
        Prefs.quranLastRead = QuranLastRead(getSurah(), getAyah(), getPage())
        RxBus.getDefault().send(MenuEvent())
        main {
            Toast.makeText(
                requireActivity(),
                LocaleConstants.SET_AS_LAST_READ.locale(),
                Toast.LENGTH_SHORT
            ).show()
            PraytimeWidget.update(requireActivity())
            dismiss()
        }

        io {
            getQuranLastReadDao().putQuranLastRead(Prefs.quranLastRead)
        }
    }

}