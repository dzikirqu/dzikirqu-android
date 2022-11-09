package com.dzikirqu.android.ui.main.note.personal


import android.content.IntentSender
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseViewModel
import com.dzikirqu.android.data.ApiService
import com.dzikirqu.android.data.AppDataManager
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.data.room.dao.getNoteDao
import com.dzikirqu.android.data.room.dao.getNotePropertyDao
import com.dzikirqu.android.model.Note
import com.dzikirqu.android.model.NoteProperty
import com.dzikirqu.android.model.NoteSortBy
import com.dzikirqu.android.model.events.ApplyWindowEvent
import com.dzikirqu.android.model.events.SettingsEvent
import com.dzikirqu.android.model.request.toRequestModel
import com.dzikirqu.android.model.response.toNote
import com.dzikirqu.android.util.*
import com.dzikirqu.android.util.ViewUtils.getProgressDialog
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotePersonalViewModel @Inject constructor(
    dataManager: AppDataManager,
) :
    BaseViewModel<NotePersonalNavigator>(dataManager) {
    override fun onEvent(obj: Any) {
        when (obj) {
            is SettingsEvent -> {
                sortBy.value = Prefs.noteSortBy
                isSortAsc.value = Prefs.isNoteSortAsc
            }
            is ApplyWindowEvent -> {
                navigator?.onApplyWindowEvent(obj.insets)
            }
        }
    }


    val statusBarHeight = mutableStateOf(0)
    var profilePicture = MutableLiveData("")

    val sortBy = MutableLiveData(Prefs.noteSortBy)
    val isSortAsc = MutableLiveData(Prefs.isNoteSortAsc)
    var selectedFolder: MutableLiveData<String?> = MutableLiveData()
    val currentUser:MutableLiveData<FirebaseUser?> = MutableLiveData(Firebase.auth.currentUser)

    val deletedNotes = dataManager.mNoteDatabase.noteDao().getDeletedNote()

    val notes = Transformations.switchMap(TripleTrigger(sortBy, isSortAsc, selectedFolder)) {
        if (it.third == deletedFolderId) {
            dataManager.mNoteDatabase.noteDao().getDeletedNote()
        } else {
            it.third?.let{
                dataManager.mNoteDatabase.noteDao().getNoteByFolder(it)
            }?: kotlin.run {
                dataManager.mNoteDatabase.noteDao().getNotes()
            }
        }
    }

    fun sort(notes:List<Note>):List<Note>{
        return when(sortBy.value){
            NoteSortBy.UPDATED_DATE -> {
                if (isSortAsc.value == true) notes.sortedBy { it.getUpdatedDateObject()?.time } else notes.sortedByDescending { it.getUpdatedDateObject()?.time }
            }
            NoteSortBy.CREATED_DATE -> {
                if (isSortAsc.value == true) notes.sortedBy { it.getDateObject()?.time } else notes.sortedByDescending { it.getDateObject()?.time }
            }
            else -> {
                if (isSortAsc.value == true) notes.sortedByDescending { it.title } else notes.sortedBy { it.title }
            }
        }
    }

    suspend fun getUserNotes(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        networkCall({ ApiService.create().getNotes().data?.map { it.toNote() } ?: listOf() },
            { notes ->
                val noteDao = dataManager.mNoteDatabase.noteDao()
                val notePropertyDao = dataManager.mNoteDatabase.notePropertyDao()
                noteDao.deleteAll()
                notePropertyDao.deleteAll()
                noteDao.putNotes(notes.map {
                    it.apply {
                        it.tags = it.tags?.toHashSet()?.toCollection(arrayListOf())
                    }
                })
                notes.map { it.folder }.filter { it?.isNotBlank() == true }.toHashSet().forEach {
                    notePropertyDao.putNoteProperty(NoteProperty.newFolder().apply {
                        content = it.toString()
                    })
                }
                notes.map { it.speaker }.map { it?.name }.filter { it?.isNotBlank() == true }
                    .toHashSet().forEach {
                        notePropertyDao.putNoteProperty(NoteProperty.newPresenter().apply {
                            content = it.toString()
                        })
                    }
                notes.map { it.location }.map { it?.name }.filter { it?.isNotBlank() == true }
                    .toHashSet().forEach {
                        notePropertyDao.putNoteProperty(NoteProperty.newLocation().apply {
                            content = it.toString()
                        })
                    }
                arrayListOf<String>().apply {
                    notes.forEach { it.tags?.let { it1 -> addAll(it1) } }
                }.toHashSet().filter { it.isNotBlank() }.forEach {
                    notePropertyDao.putNoteProperty(NoteProperty.newTag().apply {
                        content = it
                    })
                }
                onSuccess()
            }, {
                onFailure(it)
            })
    }
}

val deletedFolderId = "196229059680-s3285vd19tkv153i8j51qj167pnpjgh4.apps.googleusercontent.com"

fun NotePersonalFragment.signIn() {
    oneTapClient.beginSignIn(signInRequest)
        .addOnSuccessListener(requireActivity()) { result ->
            try {
                requestGoogleLogin.launch(IntentSenderRequest.Builder(result.pendingIntent.intentSender).build())
            } catch (e: IntentSender.SendIntentException) {
                toast(e.localizedMessage)
            }
        }
        .addOnFailureListener(requireActivity()) { e ->
            Toast.makeText(requireActivity(), e.localizedMessage, Toast.LENGTH_SHORT).show()
        }

}

fun NotePersonalFragment.signOut() {
    if (!requireActivity().isNetworkAvailable()) toast("Please check your internet connection!")
    val progress = requireActivity().getProgressDialog("Signing out...")
    main {
        progress.show()
        val notes = requireActivity().getNoteDao().getNotesSuspended()
        if (notes.isEmpty()) {
            doSignOut()
            progress.dismiss()
            return@main
        }
        io {
            networkCall(
                { ApiService.create().syncNotes(notes.map { it.toRequestModel() }) },
                onSuccess = {
                    main {
                        doSignOut()
                        progress.dismiss()
                    }
                },
                onFailure = {
                    main {
                        toast("Sign out failed ${it.message}")
                        progress.dismiss()
                    }
                })
        }
    }
}

fun NotePersonalFragment.doSignOut() {
    io {
        requireActivity().getNoteDao().deleteAll()
        requireActivity().getNotePropertyDao().deleteAll()
        Firebase.auth.signOut()
        main {
            viewModel.currentUser.value = Firebase.auth.currentUser
            if (Firebase.auth.currentUser == null) {
                viewModel.selectedFolder.value = null
            }
        }
        toast("Sign out")
    }
}

fun NotePersonalFragment.getGoogleIntentSenderRequest(): ActivityResultLauncher<IntentSenderRequest> {
    return registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
        val progressDialog = requireActivity().getProgressDialog("Loading...")
        val backupProgressDialog = requireActivity().getProgressDialog("Loading notes...")
        progressDialog.show()
        try {
            val credential = Identity.getSignInClient(requireActivity()).getSignInCredentialFromIntent(it?.data)
            val firebaseCredential = GoogleAuthProvider.getCredential(credential.googleIdToken, null)
            Firebase.auth.signInWithCredential(firebaseCredential)
                .addOnFailureListener {
                    println("Process has failed ${it.message}")
                }
                .addOnCompleteListener(requireActivity()) { task ->
                    println("Completed")
                    if (task.isSuccessful) {
                        Firebase.auth.currentUser?.getIdToken(true)
                            ?.addOnCompleteListener {
                                if (task.isSuccessful) {
                                    Prefs.accessToken = it.result.token
                                    progressDialog.dismiss()
                                    if (Firebase.auth.currentUser != null) {
                                        backupProgressDialog.show()
                                        io {
                                            viewModel.getUserNotes({
                                                toast("Login successful")
                                                backupProgressDialog.dismiss()
                                            }, {
                                                Firebase.auth.signOut()
                                                toast("Operation failed, please try again!")
                                                backupProgressDialog.dismiss()
                                            })
                                        }
                                        viewModel.currentUser.value = Firebase.auth.currentUser
                                    }
                                }
                            }
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "Login Failed, please try again ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        progressDialog.dismiss()
                    }
                }

        } catch (e: Exception) {
            progressDialog.dismiss()
            e.printStackTrace()
        }
    }
}
