package com.benrostudios.enchante

import android.app.Application
import com.benrostudios.enchante.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class EnchanteApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@EnchanteApplication)
            modules(appComponent)
        }
    }
}