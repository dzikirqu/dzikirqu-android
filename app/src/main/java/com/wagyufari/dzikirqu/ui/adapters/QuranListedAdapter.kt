package com.wagyufari.dzikirqu.ui.adapters

import android.graphics.Typeface
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseEventViewHolder
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.data.room.BookmarkDatabase
import com.wagyufari.dzikirqu.databinding.*
import com.wagyufari.dzikirqu.model.Ayah
import com.wagyufari.dzikirqu.model.Ayah.Companion.bookmark
import com.wagyufari.dzikirqu.model.events.MenuEvent
import com.wagyufari.dzikirqu.model.events.MenuEventType
import com.wagyufari.dzikirqu.model.events.SettingsEvent
import com.wagyufari.dzikirqu.model.identifiers.BismillahObject
import com.wagyufari.dzikirqu.model.identifiers.JuzObject
import com.wagyufari.dzikirqu.model.identifiers.SurahObject
import com.wagyufari.dzikirqu.util.RxBus
import com.wagyufari.dzikirqu.util.StringExt.getArabic
import com.wagyufari.dzikirqu.util.StringExt.getText
import com.wagyufari.dzikirqu.util.ViewUtils.isHidden
import com.wagyufari.dzikirqu.util.binding.ImageViewBinding.setTintColor
import com.wagyufari.dzikirqu.util.io
import com.wagyufari.dzikirqu.util.main
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class QuranListedAdapter : RecyclerView.Adapter<BaseEventViewHolder>() {

    private var mListener: Callback? = null
    private val data: MutableList<Any>

    init {
        this.data = ArrayList()
    }

    companion object {
        const val VIEW_TYPE_NORMAL = 0
        const val VIEW_TYPE_BISMILLAH = 1
        const val VIEW_TYPE_SURAH_DETAIL = 3
        const val VIEW_TYPE_JUZ = 4
    }

    override fun getItemCount(): Int {
        return if (data.isNotEmpty()) {
            data.size
        } else {
            0
        }
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        compositeDisposable.dispose()
    }

    override fun onBindViewHolder(holder: BaseEventViewHolder, position: Int) {
        holder.onBind(position)
        compositeDisposable.add(
            RxBus.getDefault().toObservables()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ obj ->
                    holder.onEvent(obj)
                }, { it.printStackTrace() })
        )
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            data[position] is BismillahObject -> {
                VIEW_TYPE_BISMILLAH
            }
            data[position] is SurahObject -> {
                VIEW_TYPE_SURAH_DETAIL
            }
            data[position] is JuzObject -> {
                VIEW_TYPE_JUZ
            }
            else -> {
                VIEW_TYPE_NORMAL
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseEventViewHolder {
        return when (viewType) {
            VIEW_TYPE_NORMAL -> {
                val viewBinding = ItemQuranAyahBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                SurahViewHolder(viewBinding)
            }
            VIEW_TYPE_BISMILLAH -> {
                val viewBinding = ItemQuranBismillahBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                SurahBismillahViewHolder(viewBinding)
            }
            VIEW_TYPE_SURAH_DETAIL -> {
                val viewBinding = ItemQuranSurahNameBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                SurahDetailViewHolder(viewBinding)
            }
            VIEW_TYPE_JUZ -> {
                val viewBinding = ItemQuranJuzBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                JuzViewHolder(viewBinding)
            }
            else -> {
                BaseEventViewHolder.EmptyViewHolder(
                    ItemEmptyBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }
        }
    }


    var isLoaded = false

    fun addItems(data: ArrayList<Any>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun clearItems() {
        data.clear()
        notifyDataSetChanged()
    }

    fun positionOfAyah(ayah: Int): Int {
        if (ayah == 1) {
            return 0
        }
        data.forEachIndexed { index, any ->
            if (any is Ayah && any.verse_number == ayah) {
                return index
            }
        }
        return 0
    }

    fun setListener(listener: Callback) {
        this.mListener = listener
    }

    interface Callback {
        fun onSelectedItem(ayah: Ayah)
    }

    class SurahBismillahViewHolder(private val mBinding: ItemQuranBismillahBinding) :
        BaseEventViewHolder(mBinding.root) {
        override fun onEvent(obj: Any) {

        }

        override fun onBind(position: Int) {
            mBinding.text.typeface =
                Typeface.createFromAsset(mBinding.root.context.assets, "fonts/Bismillah.ttf")
            mBinding.text.text = "﷽"
            mBinding.root.animation =
                AnimationUtils.loadAnimation(mBinding.root.context, R.anim.fade_in)
        }
    }

    inner class SurahDetailViewHolder(private val mBinding: ItemQuranSurahNameBinding) :
        BaseEventViewHolder(mBinding.root) {
        override fun onEvent(obj: Any) {

        }

        override fun onBind(position: Int) {
            val viewModel = data[position] as SurahObject
            mBinding.viewModel = viewModel
            mBinding.root.animation =
                AnimationUtils.loadAnimation(mBinding.root.context, R.anim.fade_in)
        }
    }

    inner class JuzViewHolder(private val mBinding: ItemQuranJuzBinding) :
        BaseEventViewHolder(mBinding.root) {
        override fun onEvent(obj: Any) {

        }

        override fun onBind(position: Int) {
            val viewModel = data[position] as JuzObject
            mBinding.root.animation =
                AnimationUtils.loadAnimation(mBinding.root.context, R.anim.fade_in)
            mBinding.viewModel = viewModel
        }
    }

    private var isSearchMode = false
    private var query = ""
    fun isSearchMode(query: String) {
        isSearchMode = true
        this.query = query
    }

    inner class SurahViewHolder(private val mBinding: ItemQuranAyahBinding) :
        BaseEventViewHolder(mBinding.root) {

        var ayah: Ayah? = null

        override fun onEvent(obj: Any) {
            when (obj) {
                is SettingsEvent -> {
                    onSettingsEvent()
                }
                is MenuEvent -> {
                    if (obj.type == MenuEventType.AutoLastRead) return
                    configureLastRead()
                }
            }
        }

        fun configureLastRead() {
            val lastRead = Prefs.quranLastRead
            val chapterId = lastRead.surah
            val verseNumber = lastRead.ayah
            mBinding.lastRead.isHidden(!(ayah?.chapterId == chapterId && ayah?.verse_number == verseNumber))
        }

        fun onSettingsEvent() {
            val prefs = Prefs
            if (!isSearchMode) {
                mBinding.arabic.typeface =
                    Typeface.createFromAsset(mBinding.root.context.assets, Prefs.arabicFont)
                mBinding.arabic.textSize = prefs.arabicTextSize
                mBinding.translation.textSize = prefs.translationTextSize
            }
            mBinding.translation.visibility =
                if (prefs.isUseTranslation) View.VISIBLE else View.GONE
            mBinding.translation.isVisible = prefs.isUseTranslation
            mBinding.arabic.isVisible = !isSearchMode && prefs.isUseArabic
            mBinding.recyclerWbw.isVisible = Prefs.quranWbwEnabled
        }

        override fun onBind(position: Int) {
            ayah = data[position] as Ayah

            mBinding.root.animation =
                AnimationUtils.loadAnimation(mBinding.root.context, R.anim.fade_in)

            if (position % 2 == 0 && !isSearchMode) {
                mBinding.root.setBackgroundColor(mBinding.root.context.getColor(R.color.neutral_50))
            } else {
                mBinding.root.setBackgroundColor(mBinding.root.context.getColor(R.color.neutral_0))
            }
            ayah?.let { ayah ->
                mBinding.root.setOnClickListener { mListener?.onSelectedItem(ayah) }

                if (isSearchMode) {
                    mBinding.index.text = "${ayah.chapterId}:${ayah.verse_number}"
                    mBinding.translation.text = Html.fromHtml(
                        ayah.text.getText().lowercase().replace(
                            query.lowercase(),
                            "<font color=\"#2d4c4c\"><b>$query</b></font>"
                        )
                    )
                } else {
                    mBinding.index.text = "${ayah.chapterId}:${ayah.verse_number}"
                    CoroutineScope(Dispatchers.Main).launch {
                        mBinding.translation.text = ayah.text.getText()
                        mBinding.arabic.text = ayah.text.getArabic().replace(
                            if (ayah.verse_number == 1 && ayah.chapterId != 1) "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ" else "",
                            ""
                        )
                    }
                }
                mBinding.arabic.isVisible = !isSearchMode
                mBinding.root.setOnClickListener {
                    mListener?.onSelectedItem(ayah)
                }

                mBinding.recyclerWbw.apply {
                    layoutManager = FlexboxLayoutManager(mBinding.root.context).apply {
                        flexDirection = FlexDirection.ROW_REVERSE
                    }
                    adapter = QuranWbwAdapter().apply {
                        submitList(ayah.wbw)
                    }
                }

                mBinding.bookmark.setOnClickListener {
                    ayah.bookmark(mBinding.root.context)
                }

                onSettingsEvent()
                configureBookmarkIcon(ayah)
                configureLastRead()
            }
        }

        fun configureBookmarkIcon(ayah: Ayah) {
            mBinding.root.context.apply {
                io {
                    val bookmark = BookmarkDatabase.getDatabase(this).bookmarkDao()
                        .getBookmarkByIdSuspend(ayah.id.toString())
                    main {
                        mBinding.bookmark.setTintColor(if (bookmark == null) R.color.neutral_300 else R.color.colorPrimary)
                    }
                }
            }
        }

    }
}