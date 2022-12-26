package com.dzikirqu.android.ui.dailyreminder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dzikirqu.android.BR
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseActivity
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.data.room.dao.getDailyReminderParentDao
import com.dzikirqu.android.databinding.ActivityDailyReminderBinding
import com.dzikirqu.android.model.DailyReminder.Companion.getStartTimeDate
import com.dzikirqu.android.ui.main.home.DailyReminderCard
import com.dzikirqu.android.util.Appbar
import com.dzikirqu.android.util.verticalSpacer
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DailyReminderActivity : BaseActivity<ActivityDailyReminderBinding, DailyReminderViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_daily_reminder
    override val viewModel: DailyReminderViewModel by viewModels()

    companion object {
        fun start(mContext: Context) {
            mContext.startActivity(Intent(mContext, DailyReminderActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.composeView.setContent {

            val dailyReminders =
                getDailyReminderParentDao().getDailyReminder().observeAsState().value?.sortedBy {
                    it.getStartTimeDate().time
                } ?: listOf()

            Column {
                Appbar().setTitle(
                    LocaleConstants.DAILY_PRAYERS.locale(),
                )
                    .setElevation(3)
                    .withBackButton()
                    .build()
                LazyColumn(Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    item {
                        verticalSpacer(height = 16)
                    }
                    items(dailyReminders) { dailyReminder ->
                        DailyReminderCard(this@DailyReminderActivity, modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp), parent = dailyReminder)
                    }
                    item {
                        verticalSpacer(height = 16)
                    }
                }
            }
        }
    }
}