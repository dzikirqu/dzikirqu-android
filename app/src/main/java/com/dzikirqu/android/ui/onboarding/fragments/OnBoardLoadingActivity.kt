package com.dzikirqu.android.ui.onboarding.fragments

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.dzikirqu.android.BR
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseActivity
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.databinding.ActivityOnBoardLoadingBinding
import com.dzikirqu.android.ui.main.MainActivity
import com.dzikirqu.android.ui.onboarding.fragments.viewmodels.OnBoardLoadingViewModel
import com.dzikirqu.android.util.*
import com.dzikirqu.android.util.AlertUtils.showAutostartPermissionAlert
import com.dzikirqu.android.util.BooleanUtil.isLocationGranted
import com.dzikirqu.android.util.BooleanUtil.isXiaomi
import com.dzikirqu.android.util.ViewUtils.animToY
import com.dzikirqu.android.util.ViewUtils.fadeShow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardLoadingActivity :
    BaseActivity<ActivityOnBoardLoadingBinding, OnBoardLoadingViewModel>() {

    override val bindingVariable: Int get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_on_board_loading
    override val viewModel: OnBoardLoadingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.lifecycleOwner = this
        configureData()
    }

    fun configureData() {
        viewModel.isBookLoaded.observe(this) { result ->
            if (result != ResultEnum.Success) return@observe
            viewDataBinding.progressPrayer.fadeShow()
            viewDataBinding.progressPrayer.animToY(0f, duration = 1000)
            Prefs.isBookLoaded = true
        }
        viewModel.isPrayerLoaded.observe(this) { result ->
            if (result != ResultEnum.Success) return@observe
            viewDataBinding.progressQuran.fadeShow()
            viewDataBinding.progressQuran.animToY(0f, duration = 1000)
            Prefs.isPrayerLoaded = true
        }
        viewModel.isQuranLoaded.observe(this) { result ->
            if (result != ResultEnum.Success) return@observe
            viewDataBinding.progressHadith.fadeShow()
            viewDataBinding.progressHadith.animToY(0f, duration = 1000)
            Prefs.isQuranLoaded = true
        }
        viewModel.isHadithLoaded.observe(this) { result ->
            if (result != ResultEnum.Success) return@observe

            Prefs.isHadithLoaded = true
            Prefs.isFirstRun = false
            io {
                putDefaultBookmarks()
                main {
                    if (Prefs.isFirstRun && isXiaomi() && isLocationGranted()) {
                        showAutostartPermissionAlert {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                            startAutostartPermissionActivity()
                        }
                    } else {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }

}

