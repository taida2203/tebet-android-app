package com.tebet.mojual.common.util

import io.reactivex.Observable
import java.util.concurrent.Callable

object ObservableCreator {
    fun <T> observable(callable: Callable<T>): Observable<T> {
        return Observable.create { e ->
            try {
                e.onNext(callable.call())
            } catch (ex: Exception) {
                if (e.isDisposed.not()) e.onError(ex)
            }
            e.onComplete()
        }
    }
}