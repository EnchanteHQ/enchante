package com.benrostudios.enchante.di

import com.benrostudios.enchante.di.modules.networkModule
import com.benrostudios.enchante.di.modules.repositoryModule
import com.benrostudios.enchante.di.modules.viewModelModule

val appComponent = listOf(
    networkModule,
    repositoryModule,
    viewModelModule
)