package com.wagyufari.dzikirqu.ui.main.note.folder

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseFragment
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.databinding.FragmentNoteFolderBinding
import com.wagyufari.dzikirqu.ui.adapters.NoteAdapter
import com.wagyufari.dzikirqu.ui.main.note.NoteActivity
import com.wagyufari.dzikirqu.ui.main.note.NoteActivity.Companion.EXTRA_FOLDER_NAME
import com.wagyufari.dzikirqu.ui.main.note.drawer
import com.wagyufari.dzikirqu.ui.main.note.personal.NotePersonalFragment.Companion.getFolderName
import com.wagyufari.dzikirqu.ui.main.note.personal.deletedFolderId
import com.wagyufari.dzikirqu.ui.note.composer.NoteComposerActivity
import com.wagyufari.dzikirqu.util.Appbar
import com.wagyufari.dzikirqu.util.ViewUtils.height
import com.wagyufari.dzikirqu.util.appbar
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