package com.wagyufari.flyercomposer.gallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class PickImage: ActivityResultContract<Intent, MediaStoreImage?>() {
    companion object{
        const val EXTRA_PICK_IMAGE = "extra_pick_image"
    }

    override fun parseResult(resultCode: Int, intent: Intent?): MediaStoreImage? {
        if (resultCode != Activity.RESULT_OK){
            return null
        }
        return intent?.getParcelableExtra(EXTRA_PICK_IMAGE)
    }

    override fun createIntent(context: Context, input: Intent): Intent {
        return input
    }
}