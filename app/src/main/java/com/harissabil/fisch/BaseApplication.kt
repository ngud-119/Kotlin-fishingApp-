package com.harissabil.fisch

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.harissabil.fisch.core.firebase.auth.domain.AuthRepository
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application(), ImageLoaderFactory {

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun newImageLoader(): ImageLoader = imageLoader

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}