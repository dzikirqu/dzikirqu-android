package com.wagyufari.dzikirqu.ui.jump

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseActivity
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.databinding.ActivityJumpQuranBinding
import com.wagyufari.dzikirqu.model.Surah
import com.wagyufari.dzikirqu.model.events.NoteInsertEvent
import com.wagyufari.dzikirqu.model.jump.JumpQuranModel
import com.wagyufari.dzikirqu.ui.adapters.JumpQuranAdapter
import com.wagyufari.dzikirqu.ui.read.ReadActivity
import com.wagyufari.dzikirqu.util.RxBus
import com.wagyufari.dzikirqu.util.ViewUtils.isHidden
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class JumpQuranActivity : BaseActivity<ActivityJumpQuranBinding, JumpQuranViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_jump_quran
    override val viewModel: JumpQuranViewModel by viewModels()

    companion object {

        const val EXTRA_IS_NOTE_DEEPLINK = "extra_is_note_deeplink"

        fun start(context: Context, isNoteDeeplink: Boolean? = false) {
            context.startActivity(Intent(context, JumpQuranActivity::class.java).apply {
                putExtra(EXTRA_IS_NOTE_DEEPLINK, isNoteDeeplink)
            })
        }

        fun newIntent(context: Context, isNoteDeeplink: Boolean? = false): Intent {
            return Intent(context, JumpQuranActivity::class.java).apply {
                putExtra(EXTRA_IS_NOTE_DEEPLINK, isNoteDeeplink)
            }
        }

        fun JumpQuranActivity.isNoteDeeplink(): Boolean {
            return intent.getBooleanExtra(EXTRA_IS_NOTE_DEEPLINK, false)
        }
    }


    @Inject
    lateinit var jumpQuranAdapter: JumpQuranAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Prefs.colorTheme)
        configureTransition()
        super.onCreate(savedInstanceState)

        viewModel.surah.observe(this) { surah ->
            if (viewModel.isHasSecondValue() && viewModel.isSecondValueNumber()) {
                val result = surah.filter { viewModel.getSecondNumber() <= it.verses }
                    .map { JumpQuranModel(it, viewModel.getSecondNumber()) }
                jumpQuranAdapter.submitList(result)
            } else {
                val result = surah.map { JumpQuranModel(it, 1) }
                jumpQuranAdapter.submitList(result)
            }
        }

        configureViews()
    }

    fun configureTransition() {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        findViewById<View>(android.R.id.content).transitionName = "shared_element_container"
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 500L
        }
    }

    fun configureViews() {
        viewDataBinding.search.addTextChangedListener {
            viewModel.query.value = it.toString()
            viewDataBinding.recycler.isHidden(it.toString().isBlank())
            viewDataBinding.clear.isHidden(it.toString().isBlank())
        }

        viewDataBinding.clear.setOnClickListener {
            viewDataBinding.search.setText("")
        }

        viewDataBinding.back.setOnClickListener {
            super.onBackPressed()
        }
        viewDataBinding.recycler.apply {
            adapter = jumpQuranAdapter.apply {
                setListener(object : JumpQuranAdapter.Callback {
                    override fun onSelectedItem(surah: Surah, ayah: Int) {
                        if (isNoteDeeplink()) {
                            RxBus.getDefault()
                                .send(NoteInsertEvent("[${surah.name} ${ayah}](https://dzikirqu.com/quran/${surah.id}/${ayah})"))
                        } else {
                            ReadActivity.startSurah(this@JumpQuranActivity, surah.id, ayah)
                        }
                        finish()
                    }
                })
            }
            layoutManager = LinearLayoutManager(this@JumpQuranActivity)
        }
        viewDataBinding.search.setOnEditorActionListener { v, actionId, _ ->
            if (viewDataBinding.search.text.toString().isEmpty().not()) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (jumpQuranAdapter.currentList.isNotEmpty()) {
                        val surah = jumpQuranAdapter.currentList[0].surah
                        val ayah = jumpQuranAdapter.currentList[0].verse
                        if (isNoteDeeplink()) {
                            RxBus.getDefault()
                                .send(NoteInsertEvent("[${surah.name} ${ayah}](https://dzikirqu.com/quran/${surah.id}/${ayah})"))
                        } else {
                            ReadActivity.startSurah(this@JumpQuranActivity, surah.id, ayah)
                        }
                        finish()
                    }
                }
            }
            false
        }

        viewDataBinding.search.setOnFocusChangeListener { view, b ->
            if (b) {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            }
        }
        viewDataBinding.search.requestFocus()
    }

    override fun onBackPressed() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        super.onBackPressed()
    }
}