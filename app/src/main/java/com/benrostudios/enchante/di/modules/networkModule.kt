package com.benrostudios.enchante.di.modules

import com.benrostudios.enchante.data.network.ApiService
import com.benrostudios.enchante.data.network.AuthService
import org.koin.dsl.module


val networkModule = module {
    single { AuthService(get()) }
    single { ApiService(get(), get()) }
}