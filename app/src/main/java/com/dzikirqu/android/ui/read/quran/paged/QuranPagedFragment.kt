package com.dzikirqu.android.ui.read.quran.paged

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.text.Layout
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.view.setPadding
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.viewModels
import com.google.android.play.core.splitcompat.SplitCompat
import com.dzikirqu.android.*
import com.dzikirqu.android.base.BaseFragment
import com.dzikirqu.android.databinding.FragmentQuranPagedBinding
import com.dzikirqu.android.databinding.LineSurahHeaderBinding
import com.dzikirqu.android.model.AyahLineWord
import com.dzikirqu.android.model.QuranLastRead
import com.dzikirqu.android.ui.bsd.quranmenu.QuranMenuBSD
import com.dzikirqu.android.util.RxBus
import com.dzikirqu.android.util.ViewUtils.dpToPx
import com.dzikirqu.android.util.io
import com.dzikirqu.android.util.main
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class QuranPagedFragment : BaseFragment<FragmentQuranPagedBinding, QuranPagedViewModel>(),
    QuranPagedNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_quran_paged
    override val viewModel: QuranPagedViewModel by viewModels()

    var page: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            page = it.getInt(ARG_PAGE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner
        viewDataBinding?.page?.text = page.toString()
        setUpWords()
    }

    fun setUpWords() {
        val typeface = Typeface.createFromAsset(
            requireActivity().createPackageContext(
                BuildConfig.APPLICATION_ID,
                0
            ).also {
                SplitCompat.install(it)
            }.assets, "fonts/quran/p$page.ttf"
        )
        viewDataBinding?.container?.removeAllViews()
        io {
            val words = viewModel.getWords(page)
            main {
                viewModel.getSurahName(
                    words.firstOrNull()?.verse_key?.substringBefore(":")?.toInt() ?: 1
                )
                viewModel.getJuz(page)
                for (i in 1..15) {
                    val verse = viewModel.getVerse(i, words)
                    if (verse.isBlank()) {
                        viewModel.getSeparator(page, i)?.let {
                            if (it.bismillah == true) {
                                viewDataBinding?.container?.addView(getBismillah())
                            } else if (it.surah != null) {
                                viewDataBinding?.container?.addView(getSurah(it.unicode.toString()))
                            }
                            true
                        } ?: kotlin.run {
                            viewDataBinding?.container?.addView(getBlank())
                        }
                    } else {
                        viewDataBinding?.container?.addView(getLine(verse, typeface, words))
                    }
                }
            }
        }
    }

    fun getBismillah(): View {
        return TextView(requireActivity()).apply {
            typeface = Typeface.createFromAsset(requireActivity().assets, "fonts/Bismillah.ttf")
            text = "﷽"
            setTextColor(resources.getColor(R.color.textPrimary))
            TextViewCompat.setAutoSizeTextTypeWithDefaults(
                this,
                TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
            )
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0
            ).apply {
                weight = 0.8f
                setPadding(dpToPx(2))
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun getSurah(surah: String): View {
        return LineSurahHeaderBinding.inflate(LayoutInflater.from(requireContext())).apply {
            text.text = surah + "\uE903"
            text.typeface = Typeface.createFromAsset(requireActivity().assets, "fonts/Surah.ttf")
        }.root
    }

    fun getBlank(): View {
        return View(requireActivity()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0
            ).apply {
                weight = 1f
            }
        }
    }

    override fun onResume() {
        super.onResume()
        RxBus.getDefault().send(QuranLastRead(
            surah = viewModel.surahId.value ?: 0,
            ayah = 0,
            page = page,
            timestamp = Calendar.getInstance().timeInMillis,
            isSavedFromPage = true
        ))
    }

    @SuppressLint("ClickableViewAccessibility")
    fun getLine(verse: String, face: Typeface, words: List<AyahLineWord>): View {
        return TextView(requireActivity()).apply {
            typeface = face
            setTextIsSelectable(true)
            text = HtmlCompat.fromHtml(verse, HtmlCompat.FROM_HTML_MODE_COMPACT)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0
            ).apply {
                weight = 1f
                setOnTouchListener { v, event ->
                    when (event.action) {
                        MotionEvent.ACTION_UP -> {
                            val layout: Layout? = (v as TextView).layout
                            val x = event!!.x
                            val y = event.y.toInt()
                            if (layout != null) {
                                val line: Int = layout.getLineForVertical(y)
                                var offset: Int = layout.getOffsetForHorizontal(line, x) - 1
                                if (offset == -1) {
                                    offset = 0
                                }
                                val htmlEscapedVerse = verse.replace("<font color=\"#048383\">", "")
                                    .replace("</font>", "")
                                words.filter { it.code_v1?.contains(htmlEscapedVerse[offset]) == true }
                                    .firstOrNull()
                                    ?.let { word ->
                                        word.verse_key?.split(":")?.get(0)?.toInt().let { surah ->
                                            word.verse_key?.split(":")?.get(1)?.toInt()
                                                .let { ayah ->
                                                    QuranMenuBSD.newInstance(surah ?: 1, ayah ?: 1, page).show(
                                                        childFragmentManager,
                                                        ""
                                                    )
                                                }
                                        }
                                    }
                            }
                        }
                    }
                    true
                }

            }
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            setTextColor(requireContext().resources.getColor(R.color.textPrimary))
            TextViewCompat.setAutoSizeTextTypeWithDefaults(
                this,
                TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
            )
        }
    }

    override fun onMenuEvent() {
        setUpWords()
    }

    companion object {

        const val ARG_PAGE = "arg_page"

        @JvmStatic
        fun newInstance(page: Int): QuranPagedFragment {
            return QuranPagedFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PAGE, page)
                }
            }
        }
    }


}