package com.dzikirqu.android.ui.read.quran.fragments.juz

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.tabs.TabLayoutMediator
import com.dzikirqu.android.BR
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseFragment
import com.dzikirqu.android.base.BaseNavigator
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.data.room.PersistenceDatabase
import com.dzikirqu.android.databinding.FragmentReadQuranBinding
import com.dzikirqu.android.ui.adapters.FragmentPagerAdapter
import com.dzikirqu.android.ui.bsd.khatam.KhatamFooterBSD
import com.dzikirqu.android.ui.bsd.settings.SettingsBSD
import com.dzikirqu.android.ui.bsd.settings.SettingsConstants
import com.dzikirqu.android.ui.read.ReadActivity
import com.dzikirqu.android.ui.read.quran.AyahQuery
import com.dzikirqu.android.ui.read.quran.AyahQueryType
import com.dzikirqu.android.ui.read.quran.ReadQuranNavigator
import com.dzikirqu.android.ui.read.quran.ReadQuranViewModel
import com.dzikirqu.android.ui.read.quran.listed.QuranListFragment
import com.dzikirqu.android.util.Appbar
import com.dzikirqu.android.util.DynamicContentDownloadWorker
import com.dzikirqu.android.util.ViewUtils.hide
import com.dzikirqu.android.util.main
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ReadQuranJuzFragment(val juz: Int? = null) :
    BaseFragment<FragmentReadQuranBinding, ReadQuranViewModel>(), BaseNavigator,
    ReadQuranNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_read_quran
    override val viewModel: ReadQuranViewModel by viewModels()

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner
        viewModel.navigator = this

        juz?.let {
            configureJuz(it)
        }
        setUpTargetMotion()
    }

    fun configureJuz(juz: Int) {
        viewDataBinding?.appbar?.setContent {
            val title = viewModel.title.observeAsState()
            val subtitle = viewModel.subtitle.observeAsState()
            Appbar(rowModifier = Modifier.padding(horizontal = 16.dp,
                vertical = 12.dp)).withBackButton().setTitle(title.value.toString(), fontSize = 14)
                .setSubtitle(subtitle.value.toString(), fontSize = 12).setRightButton(
                    rightButtonImage = listOf(R.drawable.ic_settings),
                    rightButtonHandler = listOf { onClickSettings() })
                .isCentered()
                .build()
        }
        viewModel.title.value = "Juz ${juz}"
        viewModel.juz.observe(viewLifecycleOwner) {
            buildVisibility()
            val selectedSurah = it.reversed().filter { it.juz == juz }.firstOrNull()
            val fragments = it.reversed().map { juz ->
                QuranListFragment.newInstance(AyahQuery(juz.juz.toFloat(), AyahQueryType.Juz),-1)
            }
            viewDataBinding?.verticalPager?.apply {
                adapter = FragmentPagerAdapter(requireActivity() as AppCompatActivity, fragments.toCollection(arrayListOf()))
                setCurrentItem(it.reversed().indexOf(selectedSurah), false)
            }
            TabLayoutMediator(viewDataBinding?.tab!!, viewDataBinding?.verticalPager!!) { tab, position ->
                tab.text = "Juz ${it.reversed()[position].juz}"
            }.attach()
        }
    }

    private fun buildVisibility(){
        viewDataBinding?.recycler?.hide()
        viewDataBinding?.pagedPager?.hide()
    }

    override fun onClickSettings() {
        SettingsBSD(arrayListOf(SettingsConstants.FONTS,SettingsConstants.QURAN)).show(childFragmentManager,"")
    }

    override fun onClickPage() {
//        val splitInstallManager = SplitInstallManagerFactory.create(requireActivity())
//        if (splitInstallManager.installedModules.contains("pagedquran").not()){
//            SplitInstallManagerFactory.create(requireActivity()).getSessionState(0)
//                .addOnSuccessListener {
//                    if (it.status() == SplitInstallSessionStatus.DOWNLOADING){
//                        Toast.makeText(requireActivity(), LocaleConstants.DOWNLOAD_IN_PROGRESS.locale(), Toast.LENGTH_SHORT).show()
//                    } else{
//                        configureInstallation()
//                    }
//                }
//                .addOnFailureListener {
//                    configureInstallation()
//                }
//        } else{
            main{
                val ayahLineDao = PersistenceDatabase.getDatabase(requireActivity()).ayahLineDao()
                val ayahLine = ayahLineDao.getAyahLineByJuzNumber(juz ?: 1)
                viewModel.currentAyah.value?.let { currentAyah->
                    ReadActivity.startPage(requireActivity(), ayahLine.firstOrNull{ it.verse_number == currentAyah }?.page ?: 1)
                }?: kotlin.run {
                    ReadActivity.startPage(requireActivity(), ayahLine.firstOrNull()?.page ?: 1)
                }
            }
//        }
    }


    fun configureInstallation(){
        showDownloadConfirmationDialog {
            WorkManager.getInstance(requireActivity())
                .enqueue(OneTimeWorkRequestBuilder<DynamicContentDownloadWorker>().build())
        }
    }

    override fun onClickLastRead() {
        ReadActivity.startSurah(requireActivity(), Prefs.quranLastRead.surah, Prefs.quranLastRead.ayah)
        finish()
    }

    fun setUpTargetMotion(){
        viewDataBinding?.toggleBottomSheet?.setOnClickListener {
            KhatamFooterBSD.newInstance(viewModel.currentSurah.value?.id, viewModel.currentAyah.value).show(childFragmentManager, "")
        }
    }


    fun showDownloadConfirmationDialog(onPositive:()->Unit){
        AlertDialog.Builder(requireActivity())
            .setTitle(LocaleConstants.ADDITIONAL_DATA_REQUIRED.locale())
            .setMessage(LocaleConstants.TO_ACCESS_THE_PAGED_QURAN_FEATURE_AN_EXTRA_50_FILES_NEEDS_TO_BE_DOWNLOADED.locale())
            .setPositiveButton(LocaleConstants.DOWNLOAD.locale()) { dialog, which ->
                onPositive.invoke()
            }
            .setNegativeButton(LocaleConstants.CANCEL.locale()) { dialog, which ->
                dialog.dismiss()
            }.show()
    }


}


