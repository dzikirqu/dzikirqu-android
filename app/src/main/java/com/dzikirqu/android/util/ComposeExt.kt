package com.dzikirqu.android.util

import android.view.View
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.dzikirqu.android.ui.v2.theme.lato
import com.dzikirqu.android.R

@Composable
fun thumb() {
    Row(
        Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Spacer(
            modifier = Modifier
                .height(5.dp)
                .width(38.dp)
                .background(
                    colorResource(id = R.color.neutral_500),
                    shape = RoundedCornerShape(8.dp)
                )
        )
    }
}

@Composable
fun Modifier.nonRippleClickable(onClick:()->Unit):Modifier{
    return clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null
    ){
        onClick()
    }
}

class Appbar(
    val cardModifier: Modifier? = Modifier,
    val rowModifier: Modifier? = Modifier.padding(16.dp),
    val backgroundColor: Color? = null,
) {
    private var useBackButton: Boolean? = false
    private var title: String = ""
    private var titleTextSize: Int? = 20
    private var titleFontWeight: FontWeight? = FontWeight.Bold
    private var titleFontColor: Int? = R.color.primaryLight_white
    private var subtitle: String? = null
    private var subtitleTextSize: Int? = 20
    private var subtitleHandler: (() -> Unit)? = null
    private var rightButtonTitle: List<String> = listOf()
    private var rightButtonImage: List<Any> = listOf()
    private var rightButtonHandler: List<(View?) -> Unit> = listOf()
    private var rightButtonTint: Int? = R.color.primaryLight_white
    private var padding: Int? = null

    private var searchTitle: String? = null
    private var searchDrawableRight: Int? = null
    private var searchHandler: () -> Unit = {}
    private var searchDrawableRightHandler: () -> Unit? = {}
    private var searchBottomPadding: Int = 16
    private var isSearchCentered:Boolean = false

    private var elevation: Int? = null
    private var isCentered: Boolean = false

    fun setTitle(
        title: String,
        fontSize: Int? = 20,
        fontWeight: FontWeight? = FontWeight.Bold,
        fontColor: Int? = R.color.primaryLight_white
    ): Appbar {
        this.title = title
        this.titleTextSize = fontSize
        this.titleFontWeight = fontWeight
        this.titleFontColor = fontColor
        return this
    }

    fun isCentered(): Appbar {
        this.isCentered = true
        return this
    }

    fun setSubtitle(subtitle: String, fontSize: Int? = 16, handler: (() -> Unit)? = null): Appbar {
        this.subtitle = subtitle
        this.subtitleTextSize = fontSize
        this.subtitleHandler = handler
        return this
    }

    fun withBackButton(): Appbar {
        this.useBackButton = true
        return this
    }

    fun setElevation(elevation: Int): Appbar {
        this.elevation = elevation
        return this
    }

    fun setRightButton(title: String? = null, image: Int? = null, handler: (View?) -> Unit, tintColor:Int?=R.color.primaryLight_white): Appbar {
        title?.let {
            this.rightButtonTitle = listOf(title)
        }
        image?.let {
            this.rightButtonImage = listOf(image)
        }
        this.rightButtonHandler = listOf(handler)

        this.rightButtonTint = tintColor

        return this
    }

    fun setRightButton(
        rightButtonTitle: List<String>? = listOf(),
        rightButtonImage: List<Any>? = listOf(),
        rightButtonHandler: List<(View?) -> Unit> = listOf(),
        rightButtonImageTint: Int? = R.color.primaryLight_white
    ): Appbar {
        if (rightButtonTitle != null) {
            this.rightButtonTitle = rightButtonTitle
        }
        if (rightButtonImage != null) {
            this.rightButtonImage = rightButtonImage
        }
        this.rightButtonHandler = rightButtonHandler
        rightButtonImageTint?.let {
            this.rightButtonTint = it
        }
        return this
    }

    fun setPadding(padding: Int): Appbar {
        this.padding = padding
        return this
    }

    fun setSearch(
        title: String,
        drawableRight: Int? = null,
        drawableRightHandler: () -> Unit? = {},
        isCentered:Boolean?=false,
        bottomPadding:Int?=16,
        handler: () -> Unit,
    ): Appbar {
        this.searchTitle = title
        this.searchDrawableRight = drawableRight
        this.searchHandler = handler
        this.isSearchCentered = isCentered ?: false
        this.searchDrawableRightHandler = drawableRightHandler
        this.searchBottomPadding = bottomPadding ?: 16
        return this
    }

    @Composable
    fun build() {

        appbar(
            backgroundColor = backgroundColor ?: colorResource(id = R.color.neutral_50),
            cardModifier = cardModifier ?: Modifier,
            rowModifier = rowModifier ?: Modifier,
            isCentered = isCentered,
            useBackButton = useBackButton!!,
            title = title,
            titleTextSize = titleTextSize ?: 20,
            titleFontWeight = titleFontWeight!!,
            titleFontColor = titleFontColor!!,
            subtitleTextSize = subtitleTextSize ?: 16,
            subtitle = subtitle,
            subtitleHandler = subtitleHandler,
            rightButtonTitle = rightButtonTitle,
            rightButtonHandler = rightButtonHandler,
            rightButtonTint = rightButtonTint!!,
            rightButtonImage = rightButtonImage,
            searchTitle = searchTitle,
            searchDrawableRight = searchDrawableRight,
            searchDrawableRightHandler = searchDrawableRightHandler,
            isSearchCentered = isSearchCentered,
            searchBottomPadding = searchBottomPadding,
            searchHandler = searchHandler,
            elevation = elevation ?: 4
        )
    }
}

@Composable
fun appbar(
    backgroundColor: Color = colorResource(id = R.color.neutral_50),
    cardModifier: Modifier,
    rowModifier: Modifier,
    isCentered: Boolean,
    useBackButton: Boolean,
    title: String,
    titleTextSize: Int,
    titleFontWeight: FontWeight,
    titleFontColor: Int,
    subtitleTextSize: Int,
    subtitle: String? = null,
    subtitleHandler: (() -> Unit)? = null,
    rightButtonTitle: List<String> = listOf(),
    rightButtonImage: List<Any> = listOf(),
    rightButtonHandler: List<(View?) -> Unit> = listOf(),
    rightButtonTint: Int,
    searchTitle: String? = null,
    searchDrawableRight: Int? = null,
    searchDrawableRightHandler: () -> Unit? = {},
    searchHandler: () -> Unit? = {},
    isSearchCentered:Boolean? =false,
    searchBottomPadding:Int = 16,
    elevation: Int,
) {
    val activity = LocalContext.current.getActivity()
    Card(
        modifier = cardModifier
            .fillMaxWidth(),
        elevation = elevation.dp,
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(0.dp)
    ) {
        Column {
            Row(
                rowModifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically
                ) {
                    if (useBackButton) {
                        Image(
                            modifier = Modifier
                                .height(28.dp)
                                .width(28.dp)
                                .clickable {
                                    activity?.onBackPressed()
                                },
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(
                                colorResource(id = R.color.colorPrimary)
                            )
                        )
                        if (!isCentered) {
                            horizontalSpacer(width = 32.dp)
                        }
                    }
                    Column {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = title,
                            fontFamily = lato,
                            fontWeight = titleFontWeight,
                            fontSize = titleTextSize.sp,
                            color = colorResource(
                                id = titleFontColor
                            ),
                            textAlign = if (isCentered) TextAlign.Center else TextAlign.Left,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                        subtitle?.let {
                            if (subtitle.isEmpty()) return@let
                            Row(Modifier.clickable {
                                subtitleHandler?.invoke()
                            }, verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = subtitle,
                                    fontFamily = lato,
                                    fontSize = subtitleTextSize.sp,
                                    color = colorResource(
                                        id = R.color.textTertiary
                                    ),
                                    maxLines = 1,
                                    textAlign = if (isCentered) TextAlign.Center else TextAlign.Left
                                )
                                subtitleHandler?.let {
                                    Image(modifier = Modifier
                                        .height(18.dp)
                                        .width(18.dp),
                                        painter = painterResource(id = R.drawable.ic_arrow_drop_down),
                                        contentDescription = null,
                                        colorFilter = ColorFilter.tint(
                                            colorResource(id = R.color.neutral_400)))
                                }
                            }
                        }
                    }
                }
                LazyRow {
                    rightButtonTitle.forEachIndexed { index, title ->
                        item {
                            Text(
                                modifier = Modifier.clickable {
                                    rightButtonHandler[index](null)
                                },
                                text = title.uppercase(),
                                fontFamily = lato,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = colorResource(id = R.color.colorPrimary)
                            )
                        }
                    }
                    rightButtonImage.forEachIndexed { index, image ->
                        item {
                            if (image is Int){
                                Image(
                                    modifier = Modifier
                                        .height(28.dp)
                                        .width(28.dp)
                                        .clip(RoundedCornerShape(28))
                                        .clickable {
                                            rightButtonHandler[index](null)
                                        },
                                    painter = painterResource(id = image),
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(colorResource(id = rightButtonTint))
                                )
//                                AndroidView(factory = {
//                                    ComposeView(it).apply {
//                                        setContent {
//
//                                        }
//                                    }
//                                })
                            } else{
                                Image(
                                    modifier = Modifier
                                        .height(28.dp)
                                        .width(28.dp)
                                        .clip(CircleShape)
                                        .clickable {
                                            rightButtonHandler[index](null)
                                        },
                                    painter = rememberAsyncImagePainter(model = image as String),
                                    contentDescription = null,
                                )
//                                AndroidView(factory = {
//                                    ComposeView(it).apply {
//                                        setContent {
//
//                                        }
//                                    }
//                                })
                            }
                            if (index != rightButtonImage.lastIndex) {
                                horizontalSpacer(width = 8.dp)
                            }
                        }
                    }
                }
            }
            searchTitle?.let {
                search(
                    title = searchTitle,
                    drawableRight = searchDrawableRight,
                    drawableRightHandler = searchDrawableRightHandler,
                    isCentered = isSearchCentered
                ) {
                    searchHandler()
                }
                verticalSpacer(height = searchBottomPadding)
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun search(
    title: String,
    drawableRight: Int?,
    drawableRightHandler: () -> Unit?,
    isCentered:Boolean?=false,
    handler: () -> Unit,
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
        backgroundColor = colorResource(id = R.color.card),
        border = BorderStroke(1.dp, colorResource(id = R.color.neutral_300)),
        onClick = {
            handler()
        },
        shape = CircleShape,
        elevation = 0.dp) {
        Row(
            Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .padding(12.dp)
                    .weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier
                            .height(24.dp)
                            .width(24.dp),
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(colorResource(id = R.color.textTertiary))
                    )
                    horizontalSpacer(width = 8.dp)
                    val textModifier = Modifier
                    if (isCentered == true){
                        textModifier.weight(1f)
                    }
                    Text(
                        modifier = textModifier,
                        text = title,
                        fontFamily = lato,
                        textAlign = if (isCentered == true) TextAlign.Center else TextAlign.Start,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.textTertiary)
                    )
                }
            }
            drawableRight?.let {
                Image(modifier = Modifier
                    .width(24.dp)
                    .height(24.dp)
                    .clickable {
                        drawableRightHandler()
                    },
                    painter = painterResource(id = drawableRight),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        colorResource(id = R.color.textTertiary)))
                horizontalSpacer(width = 12)
            }
        }
    }
}

