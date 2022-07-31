package com.wagyufari.dzikirqu.ui.hadithchapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseActivity
import com.wagyufari.dzikirqu.databinding.ActivityHadithChapterBinding
import com.wagyufari.dzikirqu.model.Hadith
import com.wagyufari.dzikirqu.model.HadithChapter
import com.wagyufari.dzikirqu.model.HadithSubChapter
import com.wagyufari.dzikirqu.ui.adapters.HadithChapterAdapter
import com.wagyufari.dzikirqu.ui.read.ReadActivity
import com.wagyufari.dzikirqu.util.FileUtils
import com.wagyufari.dzikirqu.util.StringExt.getText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HadithChapterActivity : BaseActivity<ActivityHadithChapterBinding, HadithChapterActivityViewModel>(){

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_hadith_chapter
    override val viewModel: HadithChapterActivityViewModel by viewModels()

    @Inject
    lateinit var hadithChapterAdapter: HadithChapterAdapter

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
        viewDataBinding.lifecycleOwner = this
        viewModel.navigator = this
        setUpViews()
    }

    fun setUpViews() {
        viewDataBinding.title.text = getHadith()?.name
        viewDataBinding.description.text = getHadith()?.description?.getText()
        viewDataBinding.recycler.apply {
            adapter = hadithChapterAdapter.apply {
                setListener(object : HadithChapterAdapter.Callback {
                    override fun onSelectChapter(chapter: HadithChapter) {
                        ReadActivity.startHadith(this@HadithChapterActivity, getHadith(), chapter, isNoteDeeplink())
                    }
                    override fun onSelectSubChapter(subchapter: HadithSubChapter) {
                        ReadActivity.startHadith(this@HadithChapterActivity, getHadith(), subchapter, isNoteDeeplink())
                    }
                })
                submitList(getChapters())
            }
            layoutManager = linearLayoutManager
        }
    }

    companion object {

        const val EXTRA_HADITH = "extra_hadith"
        const val EXTRA_IS_NOTE_DEEPLINK = "extra_note_deeplink"

        @JvmStatic
        fun start(context: Context, hadith: Hadith, isNoteDeeplink:Boolean?=false) {
            context.startActivity(Intent(context, HadithChapterActivity::class.java).apply {
                putExtra(EXTRA_HADITH, hadith)
                putExtra(EXTRA_IS_NOTE_DEEPLINK, isNoteDeeplink)
            })
        }

        fun HadithChapterActivity.getHadith(): Hadith? {
            return intent?.getParcelableExtra(EXTRA_HADITH)
        }

        fun HadithChapterActivity.isNoteDeeplink():Boolean{
            return intent?.getBooleanExtra(EXTRA_IS_NOTE_DEEPLINK, false) ?: false
        }

        fun HadithChapterActivity.getChapters(): List<HadithChapter> {
            val hadith = getHadith()
            val json = FileUtils.getJsonStringFromAssets(
                this,
                "json/hadith/${hadith?.metadata}.json"
            )
            return Gson().fromJson(json, object : TypeToken<List<HadithChapter>>() {}.type)
        }
    }

}