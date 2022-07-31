package com.wagyufari.dzikirqu.ui.main.bookmarks

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseActivity
import com.wagyufari.dzikirqu.databinding.ActivityBookmarkBinding
import com.wagyufari.dzikirqu.model.BookmarkType
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BookmarkActivity : BaseActivity<ActivityBookmarkBinding, BookmarkViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_bookmark
    override val viewModel: BookmarkViewModel by viewModels()

    companion object{

        const val EXTRA_TYPE = "extra_type"

        fun startPrayer(context:Context){
            context.startActivity(Intent(context, BookmarkActivity::class.java).apply {
                putExtra(EXTRA_TYPE, BookmarkType.PRAYER)
            })
        }
        fun startQuran(context:Context){
            context.startActivity(Intent(context, BookmarkActivity::class.java).apply {
                putExtra(EXTRA_TYPE, BookmarkType.QURAN)
            })
        }

        fun BookmarkActivity.getType():String{
            return intent.getStringExtra(EXTRA_TYPE) ?: BookmarkType.QURAN
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
        viewDataBinding.lifecycleOwner = this
    }
}