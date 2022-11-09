package com.wagyufari.flyercomposer

import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.dzikirqu.android.util.ViewUtils.getProgressDialog
import com.dzikirqu.android.util.io
import com.dzikirqu.android.util.main
import com.wagyufari.flyercomposer.databinding.ActivityFlyerComposerBinding
import com.wagyufari.flyercomposer.gallery.GalleryPickerActivity
import com.wagyufari.flyercomposer.gallery.MediaStoreImage
import com.wagyufari.flyercomposer.gallery.PickImage
import java.io.ByteArrayOutputStream


class FlyerComposerActivity : AppCompatActivity() {


    lateinit var binding: ActivityFlyerComposerBinding

    val requestImagePicker = registerForActivityResult(PickImage()) {
        media = it
        binding.image.setImageURI(Uri.parse(it?.contentUri))
    }

    var media: MediaStoreImage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlyerComposerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.image.setOnClickListener {
            requestImagePicker.launch(Intent(this, GalleryPickerActivity::class.java))
        }

        binding.paste.setOnClickListener {
            val mClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipboardText = mClipboardManager.primaryClip!!.getItemAt(0)
                .coerceToText(applicationContext).toString()
            val title = clipboardText.split("\n")[0]
            val caption = clipboardText.substring(title.count()+1, clipboardText.count())
            binding.title.text = title
            if (clipboardText.split("\n").count() > 1) {
                binding.caption.text = caption
            }
        }

        binding.upload.setOnClickListener {

            if (binding.caption.text.isEmpty() || media == null)
                return@setOnClickListener

            binding.image.isDrawingCacheEnabled = true
            binding.image.buildDrawingCache()

            val bitmap = (binding.image.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val dialog = getProgressDialog("Loading")
            dialog.show()
            media?.let { media ->
                val imageRef =
                    FirebaseStorage.getInstance().reference.child("flyer/${media.displayName}.png")
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                }.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        throw Throwable(task.exception);
                    }
                    imageRef.downloadUrl;
                }.addOnCompleteListener {
                    if (it.isSuccessful) {
                        val downloadUri: Uri? = it.result
                        downloadUri.let {
                            io {
//                                ApiService.create().putFlyers(
//                                    FlyerRequestModel(
//                                        title = binding.title.text.toString(),
//                                        caption = binding.caption.text.toString(),
//                                        image = it?.toString(),
//                                        language = "bahasa"
//                                    )
//                                )
                                main {
                                    this.media = null
                                    binding.title.text = ""
                                    binding.caption.text = ""
                                    dialog.dismiss()
                                }
                            }
                        }
                    } else {
                        dialog.dismiss()
                    }

                }
            }
        }
    }
}

data class Flyer(
    var title: String? = null,
    var caption: String? = null,
    var image: String? = "",
    var language: String? = "bahasa"
)