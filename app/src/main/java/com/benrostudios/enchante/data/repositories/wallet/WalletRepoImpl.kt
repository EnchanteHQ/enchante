package com.benrostudios.enchante.data.repositories.wallet

import android.content.Context
import android.util.Log.d
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.benrostudios.enchante.R
import com.rapydsdk.callbacks.*
import com.rapydsdk.entities.*
import com.rapydsdk.ppackages.*
import java.util.*


class WalletRepoImpl(context: Context) : WalletRepo {
    private var rpdInstance: RPDUserManager? = null
    private var globalUser: RPDUser? = null
    private var globalRPDAccountsManager: RPDAccountsManager? = null
    private val _walletBalanceResponse = MutableLiveData<RPDAccount>()

    init {
        RPDSdk.setDebugMode(true)
        RPDSdk.setup(
            context, context.getString(R.string.RAPYD_ACCESS_KEY),
            context.getString(R.string.RAPYD_SECRET_KEY)
        )
        val rpdUser = RPDUser()
        rpdUser.phoneNumber = "+611868065687"
        rpdInstance = RPDUserManager()
        rpdInstance?.attachUser(rpdUser, object : UserCallback {
            override fun onResponse(p0: RPDUser?) {
                if (p0 != null) {
                    globalUser = p0
                    globalRPDAccountsManager = RPDAccountsManager()
                }
            }

            override fun onRPDError(p0: RPDError?) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun getWalletBalance() {
        val accountManager = RPDAccountsManager()
        accountManager.getUserAccounts(object : AccountsCallback {
            override fun onResponse(p0: ArrayList<RPDAccount>?) {
                p0?.forEach {
                    if (it.currency == "USD") {
                        d("WalletRepo", "${it.balance}")
                        _walletBalanceResponse.postValue(it)
                    }
                }
            }

            override fun onRPDError(p0: RPDError?) {
                TODO("Not yet implemented")
            }

        })
    }

    override suspend fun makeWalletTransaction(destination: String, amount: Double) {
        val transferRequest = RPDTransferRequest()
        transferRequest.currency = RPDCurrency("USD")
        transferRequest.amount = "$amount"
        transferRequest.destination = destination
        globalRPDAccountsManager?.transferToUser(transferRequest, object : TransferFundsToUserCallback {
            override fun onResponse(p0: RPDUserTransferDetails?) {
                val transferReply = RPDTransferReply()
                transferReply.transferId = p0?.id
                transferReply.transferStatus = RPDTransferReply.STATUS_ACCEPT
                RPDAccountsManager().transferReply(
                    transferReply,
                    object : TransferFundsToUserCallback {
                        override fun onResponse(p0: RPDUserTransferDetails?) {
                            // Return Positive Response
                            d("WalletRepo", "${p0?.amount}")
                            getWalletBalance()
                        }

                        override fun onRPDError(p0: RPDError?) {
                            TODO("Not yet implemented")
                        }
                    })
            }

            override fun onRPDError(p0: RPDError?) {
                TODO("Not yet implemented")
            }
        })

    }

    override suspend fun addFundsToWallet(amount: Double) {
        globalRPDAccountsManager?.addFunds(
            RPDCurrency("USD"),
            "$amount",
            object : FundsDetailsCallback {
                override fun onResponse(fundsDetails: RPDFundsDetails) {
                    getWalletBalance()
                }

                override fun onRPDError(error: RPDError) {
                    // Enter your code here.
                }
            })
    }

    override suspend fun makePurchase() {
        //TODO:
    }

    override val walletBalance: LiveData<RPDAccount>
        get() = _walletBalanceResponse
}