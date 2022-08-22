package com.wagyufari.dzikirqu.util

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateUtils
import android.text.style.ReplacementSpan
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wagyufari.dzikirqu.constants.LocaleConstants
import com.wagyufari.dzikirqu.constants.LocaleConstants.locale
import com.wagyufari.dzikirqu.data.ApiService
import com.wagyufari.dzikirqu.data.Prefs
import com.wagyufari.dzikirqu.data.repository.SurahRepository
import com.wagyufari.dzikirqu.data.room.dao.getNoteDao
import com.wagyufari.dzikirqu.data.room.dao.getNotePropertyDao
import com.wagyufari.dzikirqu.model.*
import com.wagyufari.dzikirqu.model.events.MainTabType
import com.wagyufari.dzikirqu.model.legacy.NoteLegacy
import com.wagyufari.dzikirqu.model.response.toNote
import com.wagyufari.dzikirqu.util.markwon.*
import io.noties.markwon.Markwon
import io.noties.markwon.SoftBreakAddsNewLinePlugin
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.handler.EmphasisEditHandler
import io.noties.markwon.editor.handler.StrongEmphasisEditHandler
import io.noties.markwon.html.HtmlPlugin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.File
import java.lang.reflect.Field
import java.net.URI
import java.text.ParseException
import java.util.*

fun Date.toAgo(): String {
    try {
        val time: Long = this.time
        val now = System.currentTimeMillis()
        return DateUtils.getRelativeTimeSpanString(time, now, DateUtils.SECOND_IN_MILLIS).toString()
            .toLocale()
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return ""
}

internal fun String.toLocale(): String {
    if (Prefs.language == "english") return this
    return this.replace("minutes ago", "menit yang lalu").replace("minute ago", "menit yang lalu").replace("seconds ago", "detik yang lalu")
        .replace("hours ago", "jam yang lalu").replace("hour ago", "jam yang lalu")
}

fun Context.getActivity(): AppCompatActivity? = when (this) {
    is AppCompatActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

fun Fragment.startActivityWithTransition(intent: Intent, view: View?) {
    view?.let { view ->
        val options = ActivityOptions.makeSceneTransitionAnimation(
            requireActivity(),
            view,
            "shared_element_container"
        )
        requireActivity().startActivity(intent, options.toBundle())
    }
}

fun <T, K, R> LiveData<T>.combineWith(
    liveData: LiveData<K>,
    block: (T?, K?) -> R,
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        result.value = block(this.value, liveData.value)
    }
    result.addSource(liveData) {
        result.value = block(this.value, liveData.value)
    }
    return result
}


fun Activity.startActivityWithTransition(intent: Intent, view: View?) {
    view?.let { view ->
        val options = ActivityOptions.makeSceneTransitionAnimation(
            this,
            view,
            "shared_element_container"
        )
        startActivity(intent, options.toBundle())
    }
}

fun Uri.circleGlideInto(imageView: ImageView?) {
    if (imageView != null) {
        Glide.with(imageView.context).load(this).circleCrop()
            .into(
                imageView
            )
    }
}

fun String.circleGlideInto(imageView: ImageView?) {
    if (imageView != null) {
        Glide.with(imageView.context).load(this).circleCrop()
            .into(
                imageView
            )
    }
}

@Composable
fun verticalSpacer(height: Int) {
    Spacer(Modifier.height(height.dp))
}

@Composable
fun verticalSpacer(height: Dp) {
    Spacer(Modifier.height(height))
}

@Composable
fun horizontalSpacer(width: Int) {
    Spacer(Modifier.width(width.dp))
}

@Composable
fun horizontalSpacer(width: Dp) {
    Spacer(Modifier.width(width))
}

fun AppCompatActivity.showQuranModeDialog(surah: Int, ayah: Int? = null) {
//    window.setLayout(
//        ViewGroup.LayoutParams.MATCH_PARENT,
//        ViewGroup.LayoutParams.MATCH_PARENT
//    );
//    val dialog = Dialog(this)
//    dialog.setContentView(
//        DialogQuranReadModeBinding.inflate(
//            LayoutInflater.from(
//                this
//            )
//        )
//            .apply {
//                vertical.setOnClickListener {
//                    Prefs.defaultQuranReadMode =
//                        ReadModeConstants.VERTICAL
//                    ReadActivity.startSurah(
//                        this@showQuranModeDialog,
//                        surah,
//                        ayah
//                    )
//                    dialog.dismiss()
//                }
//                paged.setOnClickListener {
//                    lifecycleScope.launch {
//                    }
//                    Prefs.defaultQuranReadMode = ReadModeConstants.PAGED
//                    dialog.dismiss()
//                }
//            }.root
//    )
//    dialog.show()
}

fun getLocale(): Locale {
    return if (Prefs.language == "english") {
        Locale("en", "EN")
    } else {
        Locale("id", "ID")
    }
}

fun delay(duration: Long, handler: () -> Unit) {
    Handler().postDelayed({
        handler()
    }, duration)
}

fun View.showKeyboard() {
    requestFocus()
    val imm =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, 0)
}

@SuppressLint("ClickableViewAccessibility")
fun TextView.onClickDrawableListener(onClick: () -> Unit, onDrawableRightClick: () -> Unit) {
    this.setOnTouchListener { view, motionEvent ->
        if (motionEvent.action == MotionEvent.ACTION_UP) {
            if (motionEvent.rawX >= (this.right - this.compoundDrawables[2].bounds.width() - 32.dips())) {
                onDrawableRightClick()
            } else {
                onClick()
            }
        }
        false
    }
}

fun percentageOf(value: Int, percentage: Int): Int {
    return (value * percentage / 100)
}

fun animateValue(startValue: Int, endValue: Int, duration: Long? = 500, onAnimate: (Int) -> Unit) {
    ValueAnimator.ofInt(startValue, endValue).apply {
        this.duration = duration ?: 500
        addUpdateListener {
            onAnimate(it.animatedValue as Int)
        }
    }.start()
}


fun Int.dips(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}

fun Activity.configureTransition() {
    window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
    findViewById<View>(android.R.id.content).transitionName = "shared_element_container"
    setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
    window.sharedElementEnterTransition = MaterialContainerTransform().apply {
        addTarget(android.R.id.content)
        duration = 500L
    }
    window.sharedElementReturnTransition = MaterialContainerTransform().apply {
        addTarget(android.R.id.content)
        duration = 500L
    }
}

fun Context.getNoteFromReference(shareId: String, onSuccess: (Note) -> Unit, onFailed: () -> Unit) {
    if (!isNetworkAvailable()) {
        Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show()
        onFailed()
    } else if (Firebase.auth.currentUser == null) {
        Toast.makeText(this, "Please login by Google to access this note", Toast.LENGTH_SHORT)
            .show()
        onFailed()
    } else {
        io {
            networkCall({ ApiService.create().getNoteByShareId(shareId) }, {
                val note = it.data?.toNote()
                main {
                    if (note != null) {
                        onSuccess(note)
                    }
                }
            }, {
                main {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                onFailed()
            })
        }
//        val notesRef =
//            FirebaseStorage.getInstance().reference.child("notes/shared/${reference}.json")
//        val TWENTY_MEGABYTES = 1024 * 1024 * 20
//        notesRef.downloadUrl.addOnSuccessListener {
//            notesRef.getBytes(TWENTY_MEGABYTES.toLong()).addOnSuccessListener {
//                onSuccess(Gson().fromJson(String(it), object:TypeToken<Note>(){}.type))
//            }.addOnFailureListener {
//                it.printStackTrace()
//                onFailed()
//            }
//        }.addOnFailureListener {
//            Toast.makeText(this, "Note not found", Toast.LENGTH_SHORT).show()
//            onFailed()
//        }
    }
}

suspend fun Context.getUserNotes(onSuccess: () -> Unit, onFailed: () -> Unit) {
    networkCall({ ApiService.create().getNotes().data?.map { it.toNote() } ?: listOf() },
        { notes ->
            getNoteDao().deleteAll()
            getNotePropertyDao().deleteAll()
            getNoteDao().putNotes(notes)
            notes.map { it.folder }.filter { it?.isNotBlank() == true }.toHashSet().forEach {
                getNotePropertyDao().putNoteProperty(NoteProperty.newFolder().apply {
                    content = it.toString()
                })
            }
            notes.map { it.speaker }.map { it?.name }.filter { it?.isNotBlank() == true }
                .toHashSet().forEach {
                    getNotePropertyDao().putNoteProperty(NoteProperty.newPresenter().apply {
                        content = it.toString()
                    })
                }
            notes.map { it.location }.map { it?.name }.filter { it?.isNotBlank() == true }
                .toHashSet().forEach {
                    getNotePropertyDao().putNoteProperty(NoteProperty.newLocation().apply {
                        content = it.toString()
                    })
                }
            arrayListOf<String>().apply {
                notes.forEach { it.tags?.let { it1 -> addAll(it1) } }
            }.toHashSet().forEach {
                getNotePropertyDao().putNoteProperty(NoteProperty.newTag().apply {
                    content = it
                })
            }
            onSuccess()
        }, {
            if (it is HttpException && it.code() == 404) {
                onSuccess()
            } else {
                onFailed()
            }
        })
}

fun String.getNoteList(): List<Note> {
    return try {
        Gson().fromJson(this, object : TypeToken<List<Note>>() {}.type)
    } catch (e: Exception) {
        Gson().fromJson<List<NoteLegacy>>(this, object : TypeToken<List<NoteLegacy>>() {}.type)
            .map {
                Note(
                    id = it.id,
                    title = it.title,
                    subtitle = it.subtitle,
                    speaker = Speaker(name = it.speaker),
                    location = Location(name = it.location),
                    date = it.date,
                    tags = it.tags,
                    content = it.content,
                    updatedDate = it.updatedDate,
                    createdDate = it.createdDate,
                    folder = it.folder,
                    isDeleted = it.isDeleted,
                    shareId = it.sharedReference,
                )
            }
    }
}

fun getNotesJsonCount(): Int {
    val files = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
        "/DzikirQu Notes/"
    ).listFiles() ?: emptyArray()
    return files.size
}

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T,
) =
    lazy(LazyThreadSafetyMode.NONE) {
        bindingInflater.invoke(layoutInflater)
    }

fun getFirstDayOfHijriMonth(): Date {
    var currentDay = UmmalquraCalendar().get(Calendar.DAY_OF_MONTH)
    val calendar = Calendar.getInstance()
    while (currentDay != 1) {
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        currentDay = UmmalquraCalendar().apply {
            time = calendar.time
        }.get(Calendar.DAY_OF_MONTH)
    }
    return calendar.time
}

fun Date.getHijriMonthName(): String {
    return UmmalquraCalendar().also {
        it.time = this
    }.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)
}

fun getLastDayOfHijriMonth(): Date {
    val currentMonth = getHijriMonth()
    var elapsedMonth = getHijriMonth()
    val calendar = Calendar.getInstance()
    while (currentMonth == elapsedMonth) {
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        elapsedMonth = UmmalquraCalendar().apply {
            time = calendar.time
        }.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)
    }
    return calendar.apply {
        add(Calendar.DAY_OF_MONTH, -1)
    }.time
}

fun getHijriMonth(): String? {
    return UmmalquraCalendar().getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)
}

fun Activity.makeStatusBarTransparent() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            statusBarColor = Color.TRANSPARENT
        }
    }
}

fun getHijriDate(): String {
    val cal = UmmalquraCalendar()
    cal.time = Calendar.getInstance().time
    return "${cal.get(Calendar.DAY_OF_MONTH)} ${
        cal.getDisplayName(
            Calendar.MONTH,
            Calendar.LONG,
            Locale.ENGLISH
        )
    }," +
            " ${cal.get(Calendar.YEAR)}"
}

suspend fun Context.putDefaultBookmarks() {
//    getPrayerDao().getPrayerById("605def78a7f8a17311814780").firstOrNull()?.let { prayer->
//        getBookmarkDao().putBookmark(Bookmark(id = PrayerBookmarkHelper.getId(prayer), ids = arrayListOf(prayer.book_id.toString(), prayer.id), type = BookmarkType.PRAYER, highlighted = true))
//    }
//    getPrayerDao().getPrayerById("605def78a7f8a1731181477f").firstOrNull()?.let { prayer->
//        getBookmarkDao().putBookmark(Bookmark(id = PrayerBookmarkHelper.getId(prayer), ids = arrayListOf(prayer.book_id.toString(), prayer.id), type = BookmarkType.PRAYER, highlighted = true))
//    }
//    getPrayerDao().getPrayerById("605def78a7f8a17311814782").firstOrNull()?.let { prayer->
//        getBookmarkDao().putBookmark(Bookmark(id = PrayerBookmarkHelper.getId(prayer), ids = arrayListOf(prayer.book_id.toString(), prayer.id), type = BookmarkType.PRAYER, highlighted = true))
//    }
//    getPrayerDao().getPrayerById("605def78a7f8a17311814781").firstOrNull()?.let { prayer->
//        getBookmarkDao().putBookmark(Bookmark(id = PrayerBookmarkHelper.getId(prayer), ids = arrayListOf(prayer.book_id.toString(), prayer.id), type = BookmarkType.PRAYER, highlighted = true))
//    }
}

fun Activity.io(runnable: suspend () -> Unit) {
    CoroutineScope(IO).launch {
        runnable.invoke()
    }
}

fun Context.io(runnable: suspend () -> Unit) {
    CoroutineScope(IO).launch {
        runnable.invoke()
    }
}

fun Context.main(runnable: suspend () -> Unit) {
    CoroutineScope(Main).launch {
        runnable.invoke()
    }
}

fun Fragment.io(runnable: suspend () -> Unit) {
    CoroutineScope(IO).launch {
        runnable.invoke()
    }
}

fun Activity.main(runnable: suspend () -> Unit) {
    CoroutineScope(Main).launch {
        runnable.invoke()
    }
}

fun Fragment.main(runnable: suspend () -> Unit) {
    CoroutineScope(Main).launch {
        runnable.invoke()
    }
}

fun Any.send() {
    RxBus.getDefault().send(this)
}

fun EditText.hideKeyboard() {
    val imm: InputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun String.getPath2FromUrl(): Int {
    val paths = URI(this).path.split("/").filter { it.toIntOrNull() != null }
    return if (paths.isNotEmpty()) paths[1].toInt() else 1
}

fun String.getPath3StringFromUrl(): String {
    val paths = URI(this).path.split("/").filter { it != "" }
    return paths[2]
}

fun String.getPath1FromUrl(): Int {
    val paths = URI(this).path.split("/").filter { it.toIntOrNull() != null }
    return if (paths.isNotEmpty()) paths[0].toInt() else 1
}

fun String.getPath2FromUrlNullable(): Int? {
    val paths = URI(this).path.split("/").filter { it.toIntOrNull() != null }
    return if (paths.count() > 1) paths[1].toInt() else null
}

fun String.getPath1FromUrlNullable(): Int? {
    val paths = URI(this).path.split("/").filter { it.toIntOrNull() != null }
    return if (paths.isNotEmpty()) paths[0].toInt() else null
}

fun getQuranUrl(surah: Int, ayah: Int): String {
    val builder = Uri.Builder()
    builder.scheme("https")
        .authority("dzikirqu.com")
        .appendPath("quran")
        .appendPath("$surah")
        .appendPath("$ayah")
    return builder.build().toString()
}


fun getQuranUrl(ayah: Ayah): String {
    val builder = Uri.Builder()
    builder.scheme("https")
        .authority("dzikirqu.com")
        .appendPath("quran")
        .appendPath("${ayah.chapterId}")
        .appendPath("${ayah.verse_number}")
    return builder.build().toString()
}

fun Context.isInternetAvailable(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}

fun BottomNavigationView.setCurrentItem(id: Int) {
    if (selectedItemId != id) {
        selectedItemId = id
    }
}

fun BottomNavigationView.setCurrentItem(type: MainTabType) {
    if (selectedItemId != type.id) {
        selectedItemId = type.id
    }
}

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    val activeNetworkInfo = connectivityManager?.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

fun Context.isDeviceHasCompass(): Boolean {
    val packageManager = packageManager
    return packageManager.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS)
}

fun Context.copy(text: String) {
    val clipboard: ClipboardManager? = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
    val clip: ClipData = ClipData.newPlainText("DzikirQu", text)
    clipboard?.setPrimaryClip(clip)
}

fun TextView.setOnLongClickCopy() {
    setOnLongClickListener {
        it.context.apply {
            Toast.makeText(this, LocaleConstants.TEXT_COPIED.locale(), Toast.LENGTH_SHORT).show()
            this.copy(this@setOnLongClickCopy.text.toString())
        }
        true
    }
}

fun View.setOnLongClickCopy(text: String) {
    setOnLongClickListener {
        it.context.apply {
            Toast.makeText(this, LocaleConstants.TEXT_COPIED.locale(), Toast.LENGTH_SHORT).show()
            this.copy(text)
        }
        true
    }
}

fun wysiwygEditor(context: Context): MarkwonEditor {
    val markwon = Markwon.builder(context)
        .usePlugin(SoftBreakAddsNewLinePlugin.create())
        .usePlugin(HtmlPlugin.create {
            it.addHandler(AlignTagHandler())
        })
        .build()

    val editor = MarkwonEditor.builder(markwon)
        .useEditHandler(HeadingEditHandler())
        .useEditHandler(EmphasisEditHandler())
        .useEditHandler(StrongEmphasisEditHandler())
        .useEditHandler(StrikethroughEditHandler())
        .useEditHandler(CodeEditHandler())
        .useEditHandler(BlockQuoteEditHandler())
        .useEditHandler(LinkEditHandler { widget, link ->
        })
        .build()
    return editor
}

private class HidePunctuationSpan : ReplacementSpan() {
    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?,
    ): Int {
        // last space (which is swallowed until next non-space character appears)
        // block quote
        // code tick

//      Debug.i("text: '%s', %d-%d (%d)", text.subSequence(start, end), start, end, text.length());
        if (end == text.length) {
            // TODO: find first non-space character (not just first one because commonmark allows
            //  arbitrary (0-3) white spaces before content starts

            //  TODO: if all white space - render?
            val c = text[start]
            if ('#' == c || '>' == c || '-' == c // TODO: not thematic break
                || '+' == c // `*` is fine but only for a list
                || isBulletList(text, c, start, end)
                || Character.isDigit(c) // assuming ordered list (replacement should only happen for ordered lists)
                || Character.isWhitespace(c)
            ) {
                return (paint.measureText(text, start, end) + 0.5f).toInt()
            }
        }
        return 0
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint,
    ) {
        // will be called only when getSize is not 0 (and if it was once reported as 0...)
        if (end == text.length) {

            // if first non-space is `*` then check for is bullet
            //  else `**` would be still rendered at the end of the emphasis
            if (text[start] == '*'
                && !isBulletList(text, '*', start, end)
            ) {
                return
            }

            // TODO: inline code last tick received here, handle it (do not highlight)
            //  why can't we have reported width in this method for supplied text?

            // let's use color to make it distinct from the rest of the text for demonstration purposes
            paint.color = -0x10000
            canvas.drawText(text, start, end, x, y.toFloat(), paint)
        }
    }

    companion object {
        private fun isBulletList(
            text: CharSequence,
            firstChar: Char,
            start: Int,
            end: Int,
        ): Boolean {
            return ('*' == firstChar
                    && (end - start == 1 || Character.isWhitespace(text[start + 1])))
        }
    }
}

fun EditText.onTextChanged(changed: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            changed(p0.toString())
        }

        override fun afterTextChanged(p0: Editable?) {
        }

    })
}

fun getResId(resName: String?, c: Class<*>): Int {
    return try {
        val idField: Field = c.getDeclaredField(resName)
        idField.getInt(idField)
    } catch (e: Exception) {
        e.printStackTrace()
        -1
    }
}

fun Context.startAutostartPermissionActivity() {
    val intent = Intent()
    intent.component = ComponentName(
        "com.miui.securitycenter",
        "com.miui.permcenter.autostart.AutoStartManagementActivity"
    )
    startActivity(intent)
}

suspend fun QuranLastRead.getLastRead(mContext: Context): QuranLastReadString {
    val surah = SurahRepository.getInstance(mContext).getSurahById(surah)
    return QuranLastReadString(
        surah?.name ?: "",
        "${LocaleConstants.AYAH.locale()} $ayah",
        ""
    )
}
