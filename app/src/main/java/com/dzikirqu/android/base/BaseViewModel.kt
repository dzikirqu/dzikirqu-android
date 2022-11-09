package com.dzikirqu.android.base

import android.graphics.Color
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.dzikirqu.android.data.AppDataManager
import com.dzikirqu.android.model.events.SettingsEvent
import com.dzikirqu.android.util.RxBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference


abstract class BaseViewModel<N: BaseNavigator>(
    val dataManager: AppDataManager
) : ViewModel() {

    private val TAG = "BaseViewModel"

    private lateinit var mNavigator: WeakReference<N?>
    var navigator: N?
        get() = mNavigator.get()
        set(navigator) {
            this.mNavigator = WeakReference(navigator)
        }

    var lifecycleOwner: LifecycleOwner? = null
    var colorSchemeResource = intArrayOf(Color.parseColor("#00A85F"))

    //it's must be inject from dagger
    val compositeDisposable = CompositeDisposable()

    init {
        compositeDisposable.add(
            RxBus.getDefault().toObservables()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ obj ->
                    if (obj is SettingsEvent){
                        navigator?.onSettingsEvent()
                    }
                    onEvent(obj)
                }, { it.printStackTrace() })
        )
    }
    abstract fun onEvent(obj: Any)

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}
