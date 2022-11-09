package com.dzikirqu.android.ui.search.prayer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzikirqu.android.BR
import com.dzikirqu.android.R
import com.dzikirqu.android.base.BaseFragment
import com.dzikirqu.android.databinding.FragmentSearchPrayerBinding
import com.dzikirqu.android.model.Prayer
import com.dzikirqu.android.model.events.NoteInsertEvent
import com.dzikirqu.android.ui.adapters.PrayerAdapter
import com.dzikirqu.android.ui.read.ReadActivity
import com.dzikirqu.android.util.RxBus
import com.dzikirqu.android.util.StringExt.getText
import com.dzikirqu.android.util.ViewUtils.isHidden
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SearchPrayerFragment : BaseFragment<FragmentSearchPrayerBinding, SearchPrayerViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_search_prayer
    override val viewModel: SearchPrayerViewModel by viewModels()

    @Inject
    lateinit var prayerAdapter: PrayerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.navigator = this
        viewDataBinding?.lifecycleOwner = viewLifecycleOwner

        viewModel.result.observe(viewLifecycleOwner){
            viewDataBinding?.recycler?.isHidden(it.isEmpty())
            prayerAdapter.submitList(it)
        }
        viewDataBinding?.recycler?.apply {
            adapter = prayerAdapter.apply{
                isSearch()
                setListener(object:PrayerAdapter.Callback{
                    override fun onSelectedItem(prayer: Prayer) {
                        if (viewModel.isNoteDeeplink){
                            RxBus.getDefault().send(NoteInsertEvent("[${prayer.title?.getText()}](https://dzikirqu.com/prayer/${prayer.id}/)"))
                        } else{
                            ReadActivity.startPrayer(requireActivity(), prayer)
                        }
                        requireActivity().finish()
                    }
                })
            }
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }
}