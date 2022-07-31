package com.wagyufari.dzikirqu.ui.khatam.presenter

import androidx.lifecycle.lifecycleScope
import com.wagyufari.dzikirqu.data.room.dao.getKhatamDao
import com.wagyufari.dzikirqu.model.*
import com.wagyufari.dzikirqu.ui.main.MainActivity
import com.wagyufari.dzikirqu.util.getFirstDayOfHijriMonth
import com.wagyufari.dzikirqu.util.getHijriMonthName
import com.wagyufari.dzikirqu.util.getLastDayOfHijriMonth
import com.wagyufari.dzikirqu.util.io
import kotlinx.coroutines.launch
import java.util.*

