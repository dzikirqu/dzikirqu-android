package com.dzikirqu.android.ui.search.note

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzikirqu.android.BR
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseFragment
import com.dzikirqu.android.databinding.FragmentSearchNoteBinding
import com.dzikirqu.android.databinding.ItemNoteBinding
import com.dzikirqu.android.model.Note
import com.dzikirqu.android.ui.adapters.GenericAdapter
import com.dzikirqu.android.ui.adapters.NoteAdapter
import com.dzikirqu.android.ui.note.composer.NoteComposerActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SearchNoteFragment : BaseFragment<FragmentSearchNoteBinding, SearchNoteViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_search_note
    override val viewModel: SearchNoteViewModel by viewModels()

    @Inject
    lateinit var noteAdapter:NoteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        configureNotesRecycler()
    }

    lateinit var genericAdapter: GenericAdapter<Note, ItemNoteBinding>
    fun configureNotesRecycler(){
        genericAdapter = GenericAdapter(ItemNoteBinding::inflate, mListener1 = { note, mBinding->
            mBinding.title.text = note.title
            mBinding.title.isVisible = note.title?.isNotBlank() == true
            mBinding.subtitle.isVisible = note.subtitle?.isNotBlank() == true
            mBinding.text.isVisible = note.content?.isNotBlank() == true
            mBinding.subtitle.text = note.subtitle
            mBinding.clickable.setOnClickListener {
                startActivity(NoteComposerActivity.newIntent(requireActivity(), note.id, false))
            }
            mBinding.text.text = note.content?.replace("\n", " ")
        })

        viewDataBinding?.recycler?.apply {
            adapter = genericAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }

        viewModel.result.observe(viewLifecycleOwner){
            genericAdapter.submitList(it)
        }
    }

}