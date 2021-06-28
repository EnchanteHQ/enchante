package com.benrostudios.enchante.di.modules


import com.benrostudios.enchante.data.repositories.auth.AuthRepo
import com.benrostudios.enchante.data.repositories.auth.AuthRepoImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepo> { AuthRepoImpl(get()) }
}