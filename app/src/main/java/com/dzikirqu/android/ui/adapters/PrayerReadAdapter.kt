package com.dzikirqu.android.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dzikirqu.android.base.BaseEventViewHolder
import com.dzikirqu.android.data.Prefs
import com.dzikirqu.android.data.room.dao.getPrayerDao
import com.dzikirqu.android.databinding.PageReadPrayerBinding
import com.dzikirqu.android.model.PrayerData
import com.dzikirqu.android.model.events.SettingsEvent
import com.dzikirqu.android.ui.share.ShareImageActivity
import com.dzikirqu.android.util.RxBus
import com.dzikirqu.android.util.StringExt.getArabic
import com.dzikirqu.android.util.StringExt.getText
import com.dzikirqu.android.util.setOnLongClickCopy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class PrayerReadAdapter : RecyclerView.Adapter<BaseEventViewHolder>() {

    private val data: MutableList<PrayerData>

    init {
        this.data = ArrayList()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        compositeDisposable.dispose()
    }

    override fun onBindViewHolder(holder: BaseEventViewHolder, position: Int) {
        holder.onBind(position)
        compositeDisposable.add(
            RxBus.getDefault().toObservables()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ obj ->
                    holder.onEvent(obj)
                }, { it.printStackTrace() })
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseEventViewHolder {
        return PrayerViewHolder(
            PageReadPrayerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun addItems(data: ArrayList<PrayerData>?) {
        this.data.addAll(data ?: ArrayList())
        notifyDataSetChanged()
    }

    fun clearItems() {
        data.clear()
        notifyDataSetChanged()
    }


    inner class PrayerViewHolder(private val mBinding: PageReadPrayerBinding) :
        BaseEventViewHolder(mBinding.root) {

        override fun onEvent(obj: Any) {
            when (obj) {
                is SettingsEvent -> {
                    onSettingsEvent()
                }
            }
        }

        fun onSettingsEvent() {
            val prefs = Prefs
            mBinding.arabic.textSize = prefs.arabicTextSize
            mBinding.arabic.visibility = if (prefs.isUseArabic) View.VISIBLE else View.GONE
            mBinding.translation.textSize = prefs.translationTextSize
            mBinding.translation.visibility =
                if (prefs.isUseTranslation) View.VISIBLE else View.GONE
            mBinding.primaryDivider.visibility =
                if (prefs.isUseTranslation) View.VISIBLE else View.GONE
        }

        override fun onBind(position: Int) {
            if (data.isNotEmpty()) {
                val viewModel = data[position]
                viewModel.link?.let {
                    mBinding.arabic.text = viewModel.link?.title?.getText()
                    mBinding.translation.text = viewModel.link?.subtitle?.getText()
                } ?: kotlin.run {
                    mBinding.arabic.text = viewModel.text?.getArabic()
                    mBinding.translation.text = viewModel.text?.getText()
                }
                mBinding.source.text = viewModel.source?.getText()
                onSettingsEvent()

                mBinding.arabic.setOnLongClickCopy()
                mBinding.translation.setOnLongClickCopy()
                mBinding.source.setOnLongClickCopy()

                mBinding.shareButton.setOnClickListener {
                    MainScope().launch {
                        val title = (mBinding.root.context.getPrayerDao()
                            .getPrayerByIdSuspend(viewModel.prayer_id.toString())
                            .firstOrNull()?.title?.getText()) + (if (data.size > 1) " (${position + 1})" else "")
                        ShareImageActivity.start(
                            mBinding.root.context,
                            viewModel.text?.getArabic().toString(),
                            viewModel.text?.getText().toString(),
                            viewModel.source?.getText().toString(),
                            title
                        )
                    }
                }
            }
        }
    }
}