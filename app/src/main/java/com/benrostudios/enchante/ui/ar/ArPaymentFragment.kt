package com.benrostudios.enchante.ui.ar

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.benrostudios.enchante.R
import com.google.ar.sceneform.ux.ArFragment


class ArPaymentFragment : ArFragment() {
    override fun getAdditionalPermissions(): Array<String> =
        listOf(Manifest.permission.ACCESS_FINE_LOCATION).toTypedArray()
}