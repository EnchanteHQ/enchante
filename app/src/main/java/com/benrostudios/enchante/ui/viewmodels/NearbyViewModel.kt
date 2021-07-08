package com.benrostudios.enchante.ui.viewmodels

import androidx.lifecycle.ViewModel

class NearbyViewModel : ViewModel() {
    private var amount = 0.0


    fun setAmount(amount: Double) {
        this.amount = amount
    }

    fun getAmount(): Double {
        return amount
    }
}