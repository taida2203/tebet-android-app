package com.tebet.mojual.view.splash.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tebet.mojual.view.splash.viewmodel.SplashViewModel
import javax.inject.Inject

/**
 * Created by Ege Kuzubasioglu on 10.06.2018 at 00:47.
 * Copyright (c) 2018. All rights reserved.
 */

class SplashViewModelFactory @Inject constructor(
    private val splashViewModel: SplashViewModel
) : ViewModelProvider.Factory {

  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
      return splashViewModel as T
    }
    throw IllegalArgumentException("Unknown class name")
  }
}