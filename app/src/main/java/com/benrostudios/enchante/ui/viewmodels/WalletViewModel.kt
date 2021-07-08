package com.benrostudios.enchante.ui.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benrostudios.enchante.data.repositories.wallet.WalletRepo
import com.rapydsdk.entities.RPDAccount
import kotlinx.coroutines.launch

class WalletViewModel(private val walletRepo: WalletRepo) : ViewModel() {

    private val _response = MutableLiveData<RPDAccount>()
    val response: LiveData<RPDAccount> = _response

    init {
        walletRepo.walletBalance.observeForever {
            _response.postValue(it)
        }
    }

    fun getWalletAccount() {
        viewModelScope.launch {
            walletRepo.getWalletBalance()
        }
    }

    fun makeTransaction(destination: String, amount: Double) {
        viewModelScope.launch {
            walletRepo.makeWalletTransaction(destination, amount)
        }
    }

    fun addFunds(amount: Double) {
        viewModelScope.launch {
            walletRepo.addFundsToWallet(amount)
        }
    }

    fun makePurchase() {
        viewModelScope.launch {
            walletRepo.makePurchase()
        }
    }


}