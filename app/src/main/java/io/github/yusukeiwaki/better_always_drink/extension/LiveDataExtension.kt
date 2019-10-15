package io.github.yusukeiwaki.better_always_drink.extension

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

@MainThread
inline fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, crossinline onChanged: (T) -> Unit): Observer<T> {
    val observer = object : Observer<T> {
        override fun onChanged(t: T) {
            onChanged.invoke(t)
            removeObserver(this)
        }
    }
    observe(owner, observer)
    return observer
}
