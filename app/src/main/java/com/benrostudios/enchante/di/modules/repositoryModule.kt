package com.benrostudios.enchante.di.modules


import com.benrostudios.enchante.data.repositories.api.ApiRepo
import com.benrostudios.enchante.data.repositories.api.ApiRepoImpl
import com.benrostudios.enchante.data.repositories.auth.AuthRepo
import com.benrostudios.enchante.data.repositories.auth.AuthRepoImpl
import com.benrostudios.enchante.utils.SharedPrefManager
import org.koin.dsl.module

val repositoryModule = module {
    single { SharedPrefManager(get()) }
    single<AuthRepo> { AuthRepoImpl(get()) }
    single<ApiRepo> { ApiRepoImpl(get()) }
}