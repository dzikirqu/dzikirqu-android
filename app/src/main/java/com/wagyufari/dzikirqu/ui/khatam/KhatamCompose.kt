package com.wagyufari.dzikirqu.ui.khatam

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.room.dao.getAyahLineDao
import com.wagyufari.dzikirqu.data.room.dao.getSurahDao
import com.wagyufari.dzikirqu.model.Khatam
import com.wagyufari.dzikirqu.model.KhatamStateConstants
import com.wagyufari.dzikirqu.model.QuranLastRead
import com.wagyufari.dzikirqu.ui.bsd.StartReadQuranBSD
import com.wagyufari.dzikirqu.ui.v2.theme.lato
import com.wagyufari.dzikirqu.util.horizontalSpacer
import com.wagyufari.dzikirqu.util.nonRippleClickable
import com.wagyufari.dzikirqu.util.verticalSpacer
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.PeriodType
import org.joda.time.format.PeriodFormatterBuilder
import java.util.*

@Composable
fun KhatamActivity.CircularProgress(currentProgress: Float) {

    val progressAnimation by animateFloatAsState(
        targetValue = currentProgress / 100f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
    )
    val textAnimation by animateFloatAsState(
        targetValue = currentProgress,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
    )

    ConstraintLayout {
        val (progress, progressBackground, progressText) = createRefs()
        CircularProgressIndicator(modifier = Modifier
            .constrainAs(progressBackground) {}
            .size(128.dp),
            progress = 1f,
            color = colorResource(id = R.color.colorPrimary).copy(0.2f),
            strokeWidth = 12.dp)
        CircularProgressIndicator(modifier = Modifier
            .constrainAs(progress) {}
            .size(128.dp),
            progress = progressAnimation,
            color = colorResource(id = R.color.colorPrimary),
            strokeWidth = 12.dp)
        Row(modifier = Modifier.constrainAs(progressText) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }, verticalAlignment = Alignment.CenterVertically){
            Text(
                text = "${textAnimation.toInt()}",
                fontFamily = lato,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold, color = colorResource(id = R.color.textPrimary))
            Text(
                text = "%",
                fontFamily = lato,
                fontSize = 16.sp, color = colorResource(id = R.color.textTertiary))
        }
    }
}

@Composable
fun KhatamActivity.DaysLeft(khatam: Khatam) {
    val startDate = DateTime(Calendar.getInstance().time)
    val endDate = DateTime(khatam.endDate);
    val period = Period(startDate, endDate, PeriodType.dayTime());
    val formatter = PeriodFormatterBuilder()
        .appendDays().appendSuffix("", "")
        .toFormatter()

    Column(modifier = Modifier
        .clip(RoundedCornerShape(6.dp))
        .background(colorResource(id = R.color.colorPrimary).copy(1f)),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Image(modifier = Modifier.size(16.dp),
                painter = painterResource(id = R.drawable.ic_timer_filled),
                contentDescription = null,
                colorFilter = ColorFilter.tint(
                    colorResource(id = R.color.white)))
            horizontalSpacer(8.dp)
            Text(
                text = formatter.print(period) + " ${LocaleConstants.DAYS_LEFT.locale()}",
                fontFamily = lato,
                fontSize = 14.sp,
                color = colorResource(
                    id = R.color.white))
        }
    }
}

@Composable
fun KhatamActivity.KhatamIterationCard(onClick: () -> Unit, lastRead: QuranLastRead, index: Int) {

    val value =
        getAyahLineDao().getAyahLineByKeyLive("${lastRead.surah}:${lastRead.ayah}")
            .observeAsState().value?.firstOrNull()?.page
            ?: 1
    val surahName =
        getSurahDao().getSurahByIdLive(lastRead.surah).observeAsState().value?.name

    val maxValue = 604
    val isActive = lastRead.state == KhatamStateConstants.ACTIVE
    val currentProgress = (value * 100) / maxValue

    val progressAnimation by animateFloatAsState(
        targetValue = currentProgress.toFloat(),
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
    )

    Card(Modifier
        .fillMaxWidth()
        .padding(12.dp)
        .nonRippleClickable {
            onClick()
        },
        backgroundColor = colorResource(id = R.color.card),
        elevation = 2.dp,
        shape = RoundedCornerShape(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth()) {
                Text(modifier = Modifier.weight(1f),
                    text = "Khatam #${index + 1}",
                    fontFamily = lato,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(
                        id = if (isActive) R.color.textPrimary else R.color.textTertiary))
                Card(modifier = Modifier,
                    backgroundColor = colorResource(id = if (isActive) R.color.colorPrimary else R.color.neutral_300),
                    elevation = 0.dp,
                    shape = RoundedCornerShape(4.dp)) {
                    Text(modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp),
                        text = if (isActive) LocaleConstants.ACTIVE.locale() else LocaleConstants.INACTIVE.locale(),
                        fontFamily = lato,
                        fontSize = 12.sp,
                        color = colorResource(
                            id = if (isActive) R.color.white else R.color.textTertiary))
                }
            }
            verticalSpacer(8)
            Row {
                Text(
                    modifier = Modifier.weight(1f),
                    text = LocaleConstants.PAGE.locale() + " " + String.format(LocaleConstants.OF.locale(),
                        value,
                        maxValue),
                    fontFamily = lato,
                    fontSize = 14.sp,
                    color = colorResource(
                        id = if (isActive) R.color.textPrimary else R.color.textTertiary))
                Text(
                    modifier = Modifier,
                    text = "${progressAnimation.toInt()}%",
                    fontFamily = lato,
                    fontSize = 14.sp,
                    color = colorResource(
                        id = R.color.textTertiary))
            }
            verticalSpacer(height = 8.dp)
            Box {
                LinearProgressIndicator(modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .height(16.dp),
                    progress = progressAnimation / 100f,
                    color = colorResource(
                        id = if(isActive) R.color.colorPrimary else R.color.neutral_300),
                    backgroundColor = colorResource(id = R.color.neutral_300).copy(0.7f))
            }
            verticalSpacer(8)
            Row {
                Text(
                    modifier = Modifier,
                    text = LocaleConstants.LAST_READ_COLON.locale(),
                    fontFamily = lato,
                    fontSize = 14.sp,
                    color = colorResource(
                        id = if (isActive) R.color.textPrimary else R.color.textTertiary))
                Text(
                    modifier = Modifier,
                    text = " $surahName ${lastRead.ayah}",
                    fontFamily = lato,
                    fontSize = 14.sp,
                    color = colorResource(
                        id = R.color.textTertiary))
            }
            if (isActive) {
                verticalSpacer(height = 8)
                Button(modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        StartReadQuranBSD.newInstantInstance(surah = lastRead.surah,
                            ayah = lastRead.ayah,
                            page = lastRead.page).show(supportFragmentManager, null)
                    },
                    shape = RoundedCornerShape(4.dp),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 8.dp,
                        disabledElevation = 0.dp
                    ),
                    colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.colorPrimary))) {
                    Text(modifier = Modifier.padding(0.dp),
                        text = LocaleConstants.READ_NOW.locale().uppercase(),
                        fontFamily = lato,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(
                            id = R.color.white))
                }
            }
        }
    }
}