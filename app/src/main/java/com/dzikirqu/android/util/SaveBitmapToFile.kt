package com.dzikirqu.android.util

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Environment
import com.dzikirqu.android.R
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by RizaFu on 27/12/17.
 */
class SaveBitmapToFile(private val context: Context, private val mBitmap: Bitmap, private val onFinish: (File?) ->  Unit?) : AsyncTask<Any, Void, File>() {

    private val tag = "SaveBitmapToFileTask"

    companion object {
        fun getInstance(context:Context,bitmap: Bitmap, onFinish: (File?) -> Unit?): SaveBitmapToFile{
            return SaveBitmapToFile(context,bitmap, onFinish)
        }
    }

    override fun onPostExecute(result: File?) {
        super.onPostExecute(result)
        onFinish.invoke(result)
    }

    override fun doInBackground(vararg params: Any): File? {
        var out: OutputStream? = null
        try {
            var scaledBitmap: Bitmap? = PhotoUtils.scaleImage(mBitmap)

            if (scaledBitmap == null) {
                scaledBitmap = mBitmap
            }

            val pictureDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), context.resources.getString(
                R.string.app_name))
            pictureDir.mkdirs()

            val fileName = "IMG_" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date()) + ".jpg"
            val outFile = File(pictureDir, fileName)
            out = BufferedOutputStream(FileOutputStream(outFile))
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, out)
            return outFile
        } catch (e: FileNotFoundException) {
            return null
        } finally {
            if (out != null) {
                try {
                    out.close()
                }
                catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}