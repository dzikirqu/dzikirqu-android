package com.wagyufari.dzikirqu.ui.main.note.personal

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseFragment
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.databinding.FragmentNotePersonalBinding
import com.wagyufari.dzikirqu.ui.adapters.NoteAdapter
import com.wagyufari.dzikirqu.ui.main.note.NoteActivity
import com.wagyufari.dzikirqu.ui.main.note.NoteActivity.Companion.EXTRA_FOLDER_NAME
import com.wagyufari.dzikirqu.ui.main.note.appbar
import com.wagyufari.dzikirqu.ui.main.note.drawer
import com.wagyufari.dzikirqu.ui.note.composer.NoteComposerActivity
import com.wagyufari.dzikirqu.util.ViewUtils.height
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@OptIn(ExperimentalMaterialApi::class)
@AndroidEntryPoint
class NotePersonalFragment : BaseFragment<FragmentNotePersonalBinding, NotePersonalViewModel>(),
    NotePersonalNavigator {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_note_personal
    override val viewModel: NotePersonalViewModel by viewModels()

    lateinit var requestGoogleLogin: ActivityResultLauncher<IntentSenderRequest>

    @Inject
    lateinit var noteAdapter: NoteAdapter

    companion object {
        fun NotePersonalFragment.getFolderName(): String? {
            return requireActivity().intent.getStringExtra(EXTRA_FOLDER_NAME)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = 500
            scrimColor = Color.TRANSPARENT
        }
        requestGoogleLogin = getGoogleIntentSenderRequest()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.navigator = this
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

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

        viewDataBinding?.drawerCompose?.setContent {
            drawer()
        }
        viewDataBinding?.appbar?.setContent {
            appbar()
        }

        viewModel.currentUser.value = Firebase.auth.currentUser
        viewDataBinding?.fab?.isVisible = Firebase.auth.currentUser != null
        viewModel.profilePicture.value = Firebase.auth.currentUser?.photoUrl?.toString()

        configureNotesRecycler()
    }

    fun configureNotesRecycler() {

        viewDataBinding?.recycler?.apply {
            adapter = noteAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }

        noteAdapter.setListener(object:NoteAdapter.Callback{
            override fun onSelectFolder(folder: String, extras: FragmentNavigator.Extras) {
                viewDataBinding?.root?.findNavController()?.navigate(R.id.openFolderFromPersonal,
                    bundleOf("folderName" to folder),
                    null,
                    null)
            }
        })

        viewModel.notes.observe(viewLifecycleOwner) {
            val sortedNote = viewModel.sort(it)
            val data = mutableListOf<Any>()

            sortedNote.forEach {
                if (it.folder != null && it.folder?.isNotBlank() == true){
                    if (!data.contains(it.folder ?: "")){
                        data.add(it.folder ?: "")
                    }
                } else{
                    data.add(it)
                }
            }
            noteAdapter.submitList(if(getFolderName() == null && viewModel.selectedFolder.value != deletedFolderId) data else sortedNote)
        }
    }

}