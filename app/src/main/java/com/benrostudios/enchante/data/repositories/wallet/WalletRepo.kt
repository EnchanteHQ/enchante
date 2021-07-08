package com.benrostudios.enchante.data.repositories.wallet

import androidx.lifecycle.LiveData
import com.rapydsdk.entities.RPDAccount

interface WalletRepo {
    fun getWalletBalance()
    suspend fun makeWalletTransaction(destination: String, amount: Double)
    suspend fun addFundsToWallet(amount: Double)
    suspend fun makePurchase()
    val walletBalance: LiveData<RPDAccount>
}