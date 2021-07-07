package com.benrostudios.enchante.data.repositories.wallet

import androidx.lifecycle.LiveData

interface WalletRepo {
    suspend fun getWalletBalance()
    suspend fun makeWalletTransaction()
    suspend fun addFundsToWallet()
    suspend fun makePurchase()
}