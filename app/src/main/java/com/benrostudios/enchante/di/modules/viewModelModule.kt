package com.benrostudios.enchante.di.modules

import com.benrostudios.enchante.ui.viewmodels.ApiViewModel
import com.benrostudios.enchante.ui.viewmodels.AuthViewModel
import com.benrostudios.enchante.ui.viewmodels.NearbyViewModel
import com.benrostudios.enchante.ui.viewmodels.WalletViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { ApiViewModel(get()) }
    viewModel { WalletViewModel(get()) }
    viewModel { NearbyViewModel() }
}