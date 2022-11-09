package com.dzikirqu.android.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class TripleTrigger<A, B, C>(a: LiveData<A>, b: LiveData<B>, c: LiveData<C>) : MediatorLiveData<Triple<A?, B?, C?>>() {
    init {
        addSource(a) { value = Triple(first = it, second = b.value, third = c.value) }
        addSource(b) { value = Triple(first = a.value, second = it, third = c.value) }
        addSource(c) { value = Triple(first = a.value, second = b.value, third = it) }
    }
}