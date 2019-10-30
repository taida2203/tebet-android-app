package com.tebet.mojual.view.base

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tebet.mojual.common.util.rx.SchedulerProvider
import com.tebet.mojual.data.DataManager
import com.tebet.mojual.data.models.NetworkError
import com.tebet.mojual.view.profile.ProfileViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

import java.lang.ref.WeakReference

abstract class BaseViewModel<N>(
    val dataManager: DataManager,
    val schedulerProvider: SchedulerProvider
) : ViewModel() {

    val isLoading = ObservableBoolean()

    val isEmpty = ObservableBoolean()

    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private var mNavigator: WeakReference<N>? = null

    var baseErrorHandlerData: MutableLiveData<NetworkError> = MutableLiveData()
        private set

    var navigator: N
        get() = mNavigator?.get()!!
        set(navigator) {
            this.mNavigator = WeakReference(navigator)
        }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    fun setIsLoading(isLoading: Boolean) {
        this.isLoading.set(isLoading)
    }

    fun setIsEmpty(isEmpty: Boolean) {
        this.isEmpty.set(isEmpty)
    }

    protected fun handleError(error: NetworkError, ignoreForceLogout : Boolean = false) {
        if (ignoreForceLogout) {
            error.errorCode = 403
        }
        baseErrorHandlerData.postValue(error)
    }

    open fun loadData(isForceLoad: Boolean? = false) {
    }
}
