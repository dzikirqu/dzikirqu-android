package com.wagyufari.dzikirqu.ui.search.prayer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wagyufari.dzikirqu.BR
import com.wagyufari.dzikirqu.R
import com.wagyufari.dzikirqu.base.BaseFragment
import com.wagyufari.dzikirqu.databinding.FragmentSearchPrayerBinding
import com.wagyufari.dzikirqu.model.Prayer
import com.wagyufari.dzikirqu.model.events.NoteInsertEvent
import com.wagyufari.dzikirqu.ui.adapters.PrayerAdapter
import com.wagyufari.dzikirqu.ui.read.ReadActivity
import com.wagyufari.dzikirqu.util.RxBus
import com.wagyufari.dzikirqu.util.StringExt.getText
import com.wagyufari.dzikirqu.util.ViewUtils.isHidden
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