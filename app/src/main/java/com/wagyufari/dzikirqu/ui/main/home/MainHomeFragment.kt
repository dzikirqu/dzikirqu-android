package com.wagyufari.dzikirqu.ui.main.home

import android.Manifest
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.core.graphics.Insets
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseFragment
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.databinding.FragmentMainHomeBinding
import com.wagyufari.dzikirqu.model.PrayerBook
import com.wagyufari.dzikirqu.model.events.MainTabEvent
import com.wagyufari.dzikirqu.model.events.MainTabType
import com.wagyufari.dzikirqu.ui.adapters.PrayerBookAdapter
import com.wagyufari.dzikirqu.ui.bsd.prayer.PrayerBSD
import com.wagyufari.dzikirqu.ui.bsd.settings.SettingsActivity
import com.wagyufari.dzikirqu.ui.main.bookmarks.BookmarkActivity
import com.wagyufari.dzikirqu.ui.praytime.PraytimeActivity
import com.wagyufari.dzikirqu.ui.search.SearchActivity
import com.wagyufari.dzikirqu.util.AlertUtils.showAutostartPermissionAlert
import com.wagyufari.dzikirqu.util.BooleanUtil.isXiaomi
import com.wagyufari.dzikirqu.util.RxBus
import com.wagyufari.dzikirqu.util.ViewUtils.dpToPx
import com.wagyufari.dzikirqu.util.ViewUtils.height
import com.wagyufari.dzikirqu.util.praytimes.PrayerTimeHelper
import com.wagyufari.dzikirqu.util.startAutostartPermissionActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import android.util.Pair as UtilPair


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