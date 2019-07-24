package com.tebet.mojual.view.splash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.sdk.auth.core.models.AuthJson
import com.tebet.mojual.data.models.UserProfile
import com.tebet.mojual.data.repository.ProfileRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

/**
 * Created by Ege Kuzubasioglu on 10.06.2018 at 00:46.
 * Copyright (c) 2018. All rights reserved.
 */


class SplashViewModel @Inject constructor(
    private val profileRepository: ProfileRepository) : ViewModel() {

  var profileResult: MutableLiveData<AuthJson<UserProfile>> = MutableLiveData()
  var profileError: MutableLiveData<String> = MutableLiveData()
  var profileLoader: MutableLiveData<Boolean> = MutableLiveData()
  private lateinit var disposableObserver: DisposableObserver<AuthJson<UserProfile>>

  fun profileResult(): LiveData<AuthJson<UserProfile>> {
    return profileResult
  }

  fun profileError(): LiveData<String> {
    return profileError
  }

  fun profileLoader(): LiveData<Boolean> {
    return profileLoader
  }

  fun loadProfile() {

    disposableObserver = object : DisposableObserver<AuthJson<UserProfile>>() {
      override fun onComplete() {

      }

      override fun onNext(profileResponse: AuthJson<UserProfile>) {
        profileResult.postValue(profileResponse)
        profileLoader.postValue(false)
      }

      override fun onError(e: Throwable) {
        profileError.postValue(e.message)
        profileLoader.postValue(false)
      }
    }

    profileRepository.getProfile()
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .debounce(400, MILLISECONDS)
        .subscribe(disposableObserver)
  }

  fun disposeElements() {
    if (!disposableObserver.isDisposed) disposableObserver.dispose()
  }

}