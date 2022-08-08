package com.wagyufari.dzikirqu.ui.read.prayer

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseFragment
import com.wagyufari.dzikirqu.data.room.dao.BookmarkDao
import com.wagyufari.dzikirqu.data.room.dao.PrayerDao
import com.wagyufari.dzikirqu.databinding.FragmentReadPrayerBinding
import com.wagyufari.dzikirqu.model.Bookmark
import com.wagyufari.dzikirqu.model.BookmarkHighlightUpdate
import com.wagyufari.dzikirqu.model.BookmarkType
import com.wagyufari.dzikirqu.model.Prayer
import com.wagyufari.dzikirqu.ui.adapters.PrayerReadAdapter
import com.wagyufari.dzikirqu.ui.bsd.TextBSD
import com.wagyufari.dzikirqu.ui.bsd.settings.SettingsBSD
import com.wagyufari.dzikirqu.ui.bsd.settings.SettingsConstants
import com.wagyufari.dzikirqu.ui.v2.theme.lato
import com.wagyufari.dzikirqu.util.Appbar
import com.wagyufari.dzikirqu.util.StringExt.getText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ReadPrayerFragment :
    BaseFragment<FragmentReadPrayerBinding, ReadPrayerViewModel>(),
    ReadPrayerNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_read_prayer
    override val viewModel: ReadPrayerViewModel by viewModels()

    @Inject
    lateinit var prayerReadAdapter: PrayerReadAdapter

    @Inject
    lateinit var mBookmarkDao: BookmarkDao

    @Inject
    lateinit var mPrayerDao: PrayerDao

    companion object {
        const val ARG_PRAYER_ID = "arg_prayer_id"
        fun newInstance(prayerId: String): Fragment {
            return ReadPrayerFragment().apply {
                arguments = bundleOf(ARG_PRAYER_ID to prayerId)
            }
        }

        fun ReadPrayerFragment.getPrayerId(): String? {
            return arguments?.getString(ARG_PRAYER_ID)
        }
    }

    var pageChangeCallback: ViewPager2.OnPageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.position.value = position
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner
        viewModel.navigator = this
        viewModel.prayerId.value = getPrayerId()
        observePrayerData()
        setUpPager()

        lifecycleScope.launch {
            mBookmarkDao.getBookmarkById(getPrayer().id)
                .observe(viewLifecycleOwner) {
                    configureBookmark(it)
                }
        }

        viewDataBinding?.toolbar?.setContent {
            Appbar()
                .withBackButton()
                .setTitle(viewModel.bookName.value, 16)
                .setSubtitle(viewModel.prayerName.value, 14)
                .setPadding(8)
                .isCentered()
                .setRightButton(rightButtonTitle = listOf(),
                    rightButtonImage = listOf(R.drawable.ic_settings),
                    rightButtonHandler = listOf {
                        onClickSettings()
                    }).build()
        }

        setUpBottomContainer()
    }

    fun configureBookmark(bookmark: Bookmark?) {
//        val isBookmarked = bookmark != null
//        viewDataBinding?.pin?.isVisible = isBookmarked
//        viewDataBinding?.pin?.backgroundTintList =
//            ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.white))
//        viewDataBinding?.pin?.setTintColor(if (bookmark?.highlighted == true) R.color.colorPrimary else R.color.neutral_400)
//        viewDataBinding?.bookmark?.backgroundTintList =
//            ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.white))
//        viewDataBinding?.bookmark?.setTintColor(if (isBookmarked) R.color.colorPrimary else R.color.neutral_400)
//
//        viewDataBinding?.pin?.setOnClickListener {
//            io {
//                bookmark?.let {
//                    mBookmarkDao.updateHighlight(BookmarkHighlightUpdate(bookmark.id ?: 0,
//                        !it.highlighted))
//                }
//            }
//        }
//
//        io {
//            val prayer = getPrayer()
//            main {
//                viewDataBinding?.bookmark?.setOnClickListener {
//                    io {
//                        if (!isBookmarked) {
//                            mBookmarkDao.putBookmark(
//                                Bookmark(
//                                    idString = prayer.id,
//                                    type = BookmarkType.PRAYER
//                                )
//                            )
//                        } else {
//                            mBookmarkDao.deleteBookmark(prayer.id, BookmarkType.PRAYER)
//                        }
//                    }
//                }
//            }
//        }
    }

    suspend fun getPrayer(): Prayer {
        return mPrayerDao.getPrayerByIdSuspend(getPrayerId() ?: "").firstOrNull() ?: Prayer(
            id = "",
            book_id = null,
            order = null,
            title = null
        )
    }

    fun setUpBottomContainer() {
        viewDataBinding?.bottomContainer?.setContent {
            val position = viewModel.position.observeAsState().value ?: 0
            val prayers = (viewModel.prayerData.observeAsState().value ?: listOf())
            val bookmark = mBookmarkDao.getBookmarkById(getPrayerId().toString()).observeAsState().value
            val isBookmarked = bookmark != null
            val isHighlighted = bookmark?.highlighted == true

            if (prayers.isNotEmpty()){
                val prayer = prayers[position]
                val counterCurrentValue = viewModel.counter[prayer.id] ?: 0

                Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically){

                    if(prayer.notes?.isNotEmpty() == true){
                        Card(modifier = Modifier.weight(1f), shape = CircleShape, backgroundColor = colorResource(id = R.color.colorPrimary)){
                            Box(Modifier.clickable{
                                TextBSD.newInstance(prayer.notes?.getText()).show(childFragmentManager, "")
                            }){
                                Text(modifier = Modifier
                                    .padding(18.dp)
                                    .align(Alignment.Center),
                                    text = prayer.notes?.getText().toString(), textAlign = TextAlign.Center, fontFamily = lato, fontSize = 20.sp, color = colorResource(
                                        id = R.color.white), maxLines = 2, overflow = TextOverflow.Ellipsis)
                            }
                        }
                    } else{
                        Box(Modifier.weight(1f))
                    }

                    if(isBookmarked){
                        Card(shape = CircleShape, backgroundColor = colorResource(id = R.color.neutral_100), elevation = 4.dp){
                            Box(Modifier.clickable {
                                lifecycleScope.launch {
                                    mBookmarkDao.updateHighlight(BookmarkHighlightUpdate(bookmark?.id ?: 0,
                                        !isHighlighted))
                                }
                            }){
                                Image(modifier = Modifier
                                    .size(54.dp)
                                    .padding(16.dp), painter = painterResource(id = R.drawable.ic_star), contentDescription = null, colorFilter = ColorFilter.tint(
                                    colorResource(id = if(isHighlighted) R.color.colorPrimary else R.color.neutral_400)))
                            }
                        }
                    }

                    Card(shape = CircleShape, backgroundColor = colorResource(id = R.color.neutral_100), elevation = 4.dp){
                        Box(Modifier.clickable {
                            lifecycleScope.launch {
                                if(isBookmarked){
                                    mBookmarkDao.deleteBookmark(getPrayerId(), BookmarkType.PRAYER)
                                } else{
                                    mBookmarkDao.putBookmark(
                                        Bookmark(
                                            idString = getPrayerId(),
                                            type = BookmarkType.PRAYER
                                        )
                                    )
                                }
                            }
                        }){
                            Image(modifier = Modifier
                                .size(54.dp)
                                .padding(16.dp), painter = painterResource(id = R.drawable.ic_bookmarks), contentDescription = null, colorFilter = ColorFilter.tint(
                                colorResource(id = if(isBookmarked) R.color.colorPrimary else R.color.neutral_400)))
                        }
                    }

                    if(prayer.notes?.isNotEmpty() == true) {
                        val counter = prayer.notes?.firstOrNull()?.counter ?: 0
                        Card(shape = CircleShape, backgroundColor = colorResource(id = R.color.neutral_100), elevation = 4.dp){
                            Box(Modifier
                                .size(54.dp)
                                .clickable {
                                    if (counterCurrentValue == counter - 1) {
                                        viewModel.counter[prayer.id] = counterCurrentValue + 1
                                        viewDataBinding?.pager?.setCurrentItem(viewModel.position.value?.plus(
                                            1) ?: 1, true)
                                    } else if (counterCurrentValue != counter) {
                                        viewModel.counter[prayer.id] = counterCurrentValue + 1
                                    } else {
                                        viewDataBinding?.pager?.setCurrentItem(viewModel.position.value?.plus(
                                            1) ?: 1, true)
                                    }
                                }){
                                if(counterCurrentValue in 1 until counter){
                                    Text(modifier = Modifier
                                        .align(Alignment.Center), text = counterCurrentValue.toString(), fontFamily = lato, fontSize = 18.sp, color = colorResource(
                                        id = R.color.colorPrimary), fontWeight = FontWeight.Bold)
                                    CircularProgressIndicator(modifier = Modifier
                                        .size(54.dp)
                                        .align(Alignment.Center), progress = counterCurrentValue.toFloat() / counter.toFloat(), color = colorResource(
                                        id = R.color.colorPrimary))
                                } else{
                                    Image(modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(12.dp), painter = painterResource(id = R.drawable.ic_check), contentDescription = null, colorFilter = ColorFilter.tint(
                                        colorResource(id = if(counterCurrentValue == counter) R.color.colorPrimary else R.color.neutral_400)))
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    fun setUpPager() {
        viewDataBinding?.pager?.adapter = prayerReadAdapter
        TabLayoutMediator(viewDataBinding?.tab!!, viewDataBinding?.pager!!) { tab, position ->
            tab.text = (position + 1).toString()
        }.attach()
        viewDataBinding?.pager?.registerOnPageChangeCallback(pageChangeCallback)
        viewDataBinding?.tab?.tabMode = TabLayout.MODE_AUTO
    }

    fun observePrayerData() {
        viewModel.prayerData.observe(viewLifecycleOwner) {
            viewModel.onSuccessPrayerData(it)
            onPositionChanged(0)
        }
    }

    fun onPositionChanged(position: Int) {
//        val data = viewModel.prayerData
//        if (data.value?.isEmpty() == true) return
//        if (data.value?.get(position)?.notes?.getText()?.isNotBlank() == true) {
//            viewDataBinding?.bottomContainer?.setBackgroundColor(ContextCompat.getColor(
//                requireActivity(),
//                R.color.neutral_200))
//            viewDataBinding?.recite?.isVisible = true
//            viewDataBinding?.recite?.text = data.value?.get(position)?.notes?.getText()
//            viewDataBinding?.recite?.setOnClickListener {
//                data.value?.get(position)?.link?.let {
//                    it.start(requireActivity())
//                } ?: kotlin.run {
//                    TextBSD.newInstance(data.value?.get(position)?.notes?.getText())
//                        .show(childFragmentManager, "")
//                }
//            }
//        } else {
//            viewDataBinding?.bottomContainer?.setBackgroundColor(0)
//            viewDataBinding?.recite?.visibility = View.INVISIBLE
//        }
    }

    override fun onClickSettings() {
        SettingsBSD(arrayListOf(SettingsConstants.FONTS)).show(childFragmentManager, "")
    }
}


