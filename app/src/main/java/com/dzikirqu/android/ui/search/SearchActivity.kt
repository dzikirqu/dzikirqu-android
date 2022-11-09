package com.dzikirqu.android.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.dzikirqu.android.constants.LocaleConstants
import com.dzikirqu.android.constants.LocaleConstants.locale
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.databinding.ActivitySearchBinding
import com.dzikirqu.android.model.events.SearchEvent
import com.dzikirqu.android.util.configureTransition
import com.dzikirqu.android.util.send
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    companion object {

        const val EXTRA_SEARCH_TYPE = "extra_search_type"
        const val EXTRA_IS_NOTE_DEEPLINK = "extra_is_note_deeplink"

        fun startPrayer(mContext: Context, isNoteDeeplink: Boolean? = false) {
            mContext.startActivity(Intent(mContext, SearchActivity::class.java).apply {
                putExtra(EXTRA_SEARCH_TYPE, SearchTypeConstant.PRAYER)
                putExtra(EXTRA_IS_NOTE_DEEPLINK, isNoteDeeplink)
            })
        }

        fun startQuran(mContext: Context) {
            mContext.startActivity(Intent(mContext, SearchActivity::class.java).apply {
                putExtra(EXTRA_SEARCH_TYPE, SearchTypeConstant.QURAN)
            })
        }

        fun newNoteIntent(context: Context): Intent {
            return Intent(context, SearchActivity::class.java).apply {
                putExtra(EXTRA_SEARCH_TYPE, SearchTypeConstant.NOTE)
            }
        }
        fun newQuranIntent(context: Context): Intent {
            return Intent(context, SearchActivity::class.java).apply {
                putExtra(EXTRA_SEARCH_TYPE, SearchTypeConstant.QURAN)
            }
        }
        fun newPrayerIntent(context: Context): Intent {
            return Intent(context, SearchActivity::class.java).apply {
                putExtra(EXTRA_SEARCH_TYPE, SearchTypeConstant.PRAYER)
            }
        }

        fun SearchActivity.getType(): String {
            return intent.getStringExtra(EXTRA_SEARCH_TYPE) ?: SearchTypeConstant.QURAN
        }

        fun SearchActivity.isNoteDeeplink():Boolean{
            return intent.getBooleanExtra(EXTRA_IS_NOTE_DEEPLINK, false)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Prefs.colorTheme)
        configureTransition()
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureViews()
    }

    fun configureViews() {
        binding.quran.isVisible = getType() == SearchTypeConstant.QURAN
        binding.prayer.isVisible = getType() == SearchTypeConstant.PRAYER
        binding.notes.isVisible = getType() == SearchTypeConstant.NOTE

        when (getType()) {
            SearchTypeConstant.QURAN -> {
                binding.search.hint = LocaleConstants.SEARCH_QURAN.locale()
            }
            SearchTypeConstant.PRAYER -> {
                binding.search.hint = LocaleConstants.SEARCH_PRAYER.locale()
            }
            SearchTypeConstant.NOTE -> {
                binding.search.hint = LocaleConstants.SEARCH_NOTES.locale()
            }
        }

        binding.search.addTextChangedListener {
            binding.clear.isVisible = binding.search.text.toString().isNotEmpty()
            SearchEvent(binding.search.text.toString(), isNoteDeeplink()).send()
        }

        binding.clear.setOnClickListener {
            binding.search.setText("")
        }

        binding.back.setOnClickListener {
            super.onBackPressed()
        }


        binding.search.setOnFocusChangeListener { view, b ->
            if (b) {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            }
        }
        binding.search.requestFocus()
    }

    override fun onBackPressed() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        super.onBackPressed()
    }
}

object SearchTypeConstant {
    const val QURAN = "quran"
    const val PRAYER = "prayer"
    const val NOTE = "note"
}