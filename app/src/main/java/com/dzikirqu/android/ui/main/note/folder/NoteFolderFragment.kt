package com.dzikirqu.android.ui.main.note.folder

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.dzikirqu.android.BR
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseFragment
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.databinding.FragmentNoteFolderBinding
import com.dzikirqu.android.ui.adapters.NoteAdapter
import com.dzikirqu.android.ui.note.composer.NoteComposerActivity
import com.dzikirqu.android.util.Appbar
import com.dzikirqu.android.util.ViewUtils.height
import com.dzikirqu.android.util.appbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@OptIn(ExperimentalMaterialApi::class)
@AndroidEntryPoint
class NoteFolderFragment : BaseFragment<FragmentNoteFolderBinding, NoteFolderViewModel>(),
    NoteFolderNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_note_folder
    override val viewModel: NoteFolderViewModel by viewModels()


    @Inject
    lateinit var noteAdapter: NoteAdapter

    companion object {
        fun NoteFolderFragment.getFolderName(): String? {
            return arguments?.getString("folderName")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = 500
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigator = this
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        ViewCompat.setTransitionName(viewDataBinding?.drawerLayout!!, getFolderName())

        viewDataBinding?.statusBarHeight?.height(Prefs.statusBarHeight, duration = 0)
        viewDataBinding?.appbar?.updatePadding(0, Prefs.statusBarHeight, 0, 0)
        viewModel.statusBarHeight.value = Prefs.statusBarHeight

        viewModel.selectedFolder.value = getFolderName()

        configureViews()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun configureViews() {
        viewDataBinding?.fab?.setOnClickListener {
            startActivity(NoteComposerActivity.newIntent(requireActivity(), folderName = getFolderName()))
        }

        viewDataBinding?.appbar?.setContent {
            Appbar(Modifier,
                Modifier.padding(top = 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp),
                backgroundColor = colorResource(id = android.R.color.transparent)).setTitle(
                getFolderName().toString(),
            ).withBackButton().setElevation(0).build()
        }

        viewDataBinding?.fab?.isVisible = Firebase.auth.currentUser != null
        viewModel.profilePicture.value = Firebase.auth.currentUser?.photoUrl?.toString()

        configureNotesRecycler()
    }

    fun configureNotesRecycler() {

        viewDataBinding?.recycler?.apply {
            adapter = noteAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }

        viewModel.notes.observe(viewLifecycleOwner) {
            val sortedNote = viewModel.sort(it)
            noteAdapter.submitList(sortedNote)
        }
    }

}