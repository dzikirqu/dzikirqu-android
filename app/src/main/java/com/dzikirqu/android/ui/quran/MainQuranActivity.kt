package com.dzikirqu.android.ui.quran

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzikirqu.android.BR
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseActivity
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.constants.ReadModeConstants
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.databinding.DialogQuranReadModeBinding
import com.dzikirqu.android.databinding.FragmentMainQuranBinding
import com.dzikirqu.android.model.QuranLastRead
import com.dzikirqu.android.ui.jump.JumpQuranActivity
import com.dzikirqu.android.ui.khatam.KhatamActivity
import com.dzikirqu.android.ui.main.bookmarks.BookmarkActivity
import com.dzikirqu.android.ui.main.quran.MainQuranNavigator
import com.dzikirqu.android.ui.main.quran.MainQuranViewModel
import com.dzikirqu.android.ui.main.quran.fragment.SurahFragment
import com.dzikirqu.android.ui.read.ReadActivity
import com.dzikirqu.android.ui.search.SearchActivity
import com.dzikirqu.android.util.Appbar
import com.dzikirqu.android.util.openPagedQuran
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainQuranActivity : BaseActivity<FragmentMainQuranBinding, MainQuranViewModel>(),
    MainQuranNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_main_quran
    override val viewModel: MainQuranViewModel by viewModels()

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    companion object {

        const val EXTRA_IS_NOTE_DEEPLINK = "arg_is_note_deeplink"

        fun newIntent(context: Context, isNoteDeeplink: Boolean): Intent {
            return Intent(context, MainQuranActivity::class.java).apply{
                putExtra(EXTRA_IS_NOTE_DEEPLINK, isNoteDeeplink)
            }
        }

        fun MainQuranActivity.isNoteDeeplink(): Boolean {
            return intent.getBooleanExtra(EXTRA_IS_NOTE_DEEPLINK, false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.lifecycleOwner = this
        viewModel.navigator = this
        setUpViews()
    }

    fun setUpViews() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.surah, SurahFragment.newInstance(isNoteDeeplink()))
            .commit()

        viewDataBinding?.jump?.setOnClickListener {
            startActivity(JumpQuranActivity.newIntent(this, isNoteDeeplink()))
        }
        viewDataBinding?.appbar?.setContent {
            Appbar(backgroundColor = colorResource(id = android.R.color.transparent)).setTitle(
                LocaleConstants.QURAN.locale(),
                fontWeight = FontWeight.Black,
                fontSize = 24
            ).setRightButton(
                rightButtonImage = listOf(
                    R.drawable.ic_bookmarks,
                    R.drawable.ic_target
                ), rightButtonHandler = listOf({
                    BookmarkActivity.startQuran(this@MainQuranActivity)
                }, {
                    KhatamActivity.start(this)
                })
            ).setSearch(LocaleConstants.SEARCH_QURAN.locale()){
                startActivity(SearchActivity.newQuranIntent(this))
            }
                .setElevation(0)
                .build()
        }

//        viewDataBinding?.search?.onClickDrawableListener({
//            startActivityWithTransition(
//                SearchActivity.newQuranIntent(this),
//                viewDataBinding?.search
//            )
//        }, {
//            MainQuranFilterBSD().show(supportFragmentManager, "")
//        })
//
//        viewDataBinding.search.isVisible = !isNoteDeeplink()

        viewDataBinding.surah.isVisible = Prefs.quranFilterMode == 0
        viewDataBinding.juz.isVisible = Prefs.quranFilterMode == 1
    }

    override fun onSettingsEvent() {
        super.onSettingsEvent()
        viewDataBinding.surah.isVisible = Prefs.quranFilterMode == 0
        viewDataBinding.juz.isVisible = Prefs.quranFilterMode == 1
    }


    fun openLastRead(lastRead: QuranLastRead) {
        if (lastRead.isSavedFromPage == true) {
            this.openPagedQuran(lastRead)
            return
        }
        when (Prefs.defaultQuranReadMode) {
            ReadModeConstants.VERTICAL -> {
                ReadActivity.startSurah(this, lastRead.surah, lastRead.ayah)
            }
            ReadModeConstants.PAGED -> {
                this.openPagedQuran(lastRead)
            }
        }
    }

    fun showQuranModeDialog(lastRead: QuranLastRead) {
        this.window.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        );
        val dialog = Dialog(this)
        dialog.setContentView(
            DialogQuranReadModeBinding.inflate(LayoutInflater.from(this))
                .apply {
                    vertical.setOnClickListener {
                        Prefs.defaultQuranReadMode = ReadModeConstants.VERTICAL
                        ReadActivity.startSurah(
                            this@MainQuranActivity,
                            lastRead.surah,
                            lastRead.ayah
                        )
                        dialog.dismiss()
                    }
                    paged.setOnClickListener {
                        openPagedQuran(lastRead)
                        Prefs.defaultQuranReadMode = ReadModeConstants.PAGED
                        dialog.dismiss()
                    }
                }.root
        )
        dialog.show()
    }

    override fun onNotePropertySelectedEvent() {
        finish()
    }

}