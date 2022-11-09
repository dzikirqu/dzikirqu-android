package com.dzikirqu.android.ui.main.home

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.core.graphics.Insets
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dzikirqu.android.BR
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseFragment
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.databinding.FragmentMainHomeBinding
import com.dzikirqu.android.model.PrayerBook
import com.dzikirqu.android.model.events.MainTabEvent
import com.dzikirqu.android.model.events.MainTabType
import com.dzikirqu.android.ui.adapters.PrayerBookAdapter
import com.dzikirqu.android.ui.bsd.prayer.PrayerBSD
import com.dzikirqu.android.ui.bsd.settings.SettingsActivity
import com.dzikirqu.android.ui.main.bookmarks.BookmarkActivity
import com.dzikirqu.android.ui.praytime.PraytimeActivity
import com.dzikirqu.android.ui.search.SearchActivity
import com.dzikirqu.android.util.AlertUtils.showAutostartPermissionAlert
import com.dzikirqu.android.util.BooleanUtil.isXiaomi
import com.dzikirqu.android.util.RxBus
import com.dzikirqu.android.util.ViewUtils.dpToPx
import com.dzikirqu.android.util.ViewUtils.height
import com.dzikirqu.android.util.praytimes.PrayerTimeHelper
import com.dzikirqu.android.util.startAutostartPermissionActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class MainHomeFragment : BaseFragment<FragmentMainHomeBinding, MainHomeViewModel>(),
    MainHomeNavigator,
    PrayerBookAdapter.Callback {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_main_home
    override val viewModel: MainHomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        viewDataBinding?.lifecycleOwner = this

        viewModel.buildPrayerTime()
        viewModel.setUpLastRead()
        configureViews()
    }

    override fun onApplyWindowEvent(insets: Insets) {
        viewDataBinding?.statusBarHeight?.height(insets.top, duration = 0)
    }

    fun onClickSearch(view:View) {
        val popupMenu = PopupMenu(requireActivity(), view)
        popupMenu.menu.add(LocaleConstants.QURAN.locale())
        popupMenu.menu.add(LocaleConstants.PRAYER.locale())
        popupMenu.setOnMenuItemClickListener {
            when (it.title) {
                LocaleConstants.QURAN.locale() -> SearchActivity.startQuran(requireActivity())
                LocaleConstants.PRAYER.locale() -> SearchActivity.startPrayer(requireActivity())
            }
            true
        }
        popupMenu.show()
    }

    override fun onClickSettings() {
        SettingsActivity.start(requireActivity())
    }

    override fun onResume() {
        super.onResume()
        viewModel.time.value = Calendar.getInstance().apply{
            add(Calendar.MINUTE, 1)
        }.time
    }

    override fun onClickGrantPermission() {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    override fun onClickReadQuran() {
        RxBus.getDefault().send(MainTabEvent(MainTabType.QURAN))
    }

    override fun onClickPraytime() {
        requireActivity().startActivity(Intent(requireActivity(), PraytimeActivity::class.java))
    }

    override fun onClickBookmark() {
        BookmarkActivity.startQuran(requireActivity())
    }

    fun configureViews() {
        viewDataBinding?.compose?.setContent {
            Compose()
        }
        viewDataBinding?.scrollView?.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            viewDataBinding?.statusBarBackground?.alpha = viewDataBinding?.scrollView?.scrollY!! / dpToPx(240).toFloat()
        }
    }

    override fun onSelectedItem(book: PrayerBook) {
        PrayerBSD.newInstance(book).show(childFragmentManager, "")
    }

    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true && permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                lifecycleScope.launch {
                    PrayerTimeHelper.getPrayerTime(requireActivity())
                    if (isXiaomi()) {
                        requireActivity().showAutostartPermissionAlert {
                            requireActivity().startAutostartPermissionActivity()
                        }
                    }
                }
            }
        }
}