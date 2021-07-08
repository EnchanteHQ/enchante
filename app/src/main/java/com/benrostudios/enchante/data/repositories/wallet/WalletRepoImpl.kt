package com.benrostudios.enchante.data.repositories.wallet

import android.content.Context
import android.util.Log.d
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.benrostudios.enchante.R
import com.benrostudios.enchante.utils.SharedPrefManager
import com.rapydsdk.callbacks.*
import com.rapydsdk.entities.*
import com.rapydsdk.ppackages.*
import java.util.*


class WalletRepoImpl(context: Context, sharedPrefManager: SharedPrefManager) : WalletRepo {
    private var rpdInstance: RPDUserManager? = null
    private var globalUser: RPDUser? = null
    private var globalRPDAccountsManager: RPDAccountsManager? = null
    private val _walletBalanceResponse = MutableLiveData<RPDAccount>()

    init {
        d("Wallet Repo", "Init")
        RPDSdk.setDebugMode(true)
        RPDSdk.setup(
            context, context.getString(R.string.RAPYD_ACCESS_KEY),
            context.getString(R.string.RAPYD_SECRET_KEY)
        )
        val rpdUser = RPDUser()
        rpdUser.phoneNumber = sharedPrefManager.userPhoneNumber
        rpdInstance = RPDUserManager()
        rpdInstance?.attachUser(rpdUser, object : UserCallback {
            override fun onResponse(p0: RPDUser?) {
                if (p0 != null) {
                    globalUser = p0
                    globalRPDAccountsManager = RPDAccountsManager()
                }
            }

            override fun onRPDError(p0: RPDError?) {
                d("WalletRepo", "Wallet Instance ${p0?.reason} ${p0?.description}")
            }

        })
    }


    override fun getWalletBalance() {
        d("Wallet Repo", "Wallet Balance Called")
        val accountManager = RPDAccountsManager()
        accountManager.getUserAccounts(object : AccountsCallback {
            override fun onResponse(p0: ArrayList<RPDAccount>?) {
                if (p0.isNullOrEmpty()) {
                    val dummyRpd = RPDAccount()
                    dummyRpd.balance = 0.0
                    _walletBalanceResponse.postValue(dummyRpd)
                }
                p0?.forEach {
                    if (it.currency == "USD") {
                        d("WalletRepo", "${it.balance}")
                        _walletBalanceResponse.postValue(it)
                    }
                }
            }

            override fun onRPDError(p0: RPDError?) {
                d("WalletRepo", "Wallet Balance ${p0?.reason} ${p0?.description}")
            }

        })
    }

    override suspend fun makeWalletTransaction(destination: String, amount: Double) {
        val transferRequest = RPDTransferRequest()
        transferRequest.currency = RPDCurrency("USD")
        transferRequest.amount = "$amount"
        transferRequest.destination = destination
        globalRPDAccountsManager?.transferToUser(
            transferRequest,
            object : TransferFundsToUserCallback {
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
                                d("WalletRepo", "Wallet Balance ${p0?.reason} ${p0?.description}")
                            }
                        })
                }

                override fun onRPDError(p0: RPDError?) {
                    d("WalletRepo", "Wallet Balance ${p0?.reason} ${p0?.description}")
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
                    d("WalletRepo", "Wallet Balance ${error.reason} ${error.description}")
                }
            })
    }

    override suspend fun makePurchase() {
        //TODO:
    }

    override val walletBalance: LiveData<RPDAccount>
        get() = _walletBalanceResponse
}