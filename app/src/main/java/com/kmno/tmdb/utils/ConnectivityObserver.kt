package com.kmno.tmdb.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Created by Kamran Nourinezhad on 18 June-6 2025.
 * Copyright (c)  2025 MCI.
 */

class ConnectivityObserver(context: Context) {

    sealed class Status {
        object Available : Status();
        object Unavailable : Status()
    }

    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @SuppressLint("MissingPermission")
    fun observe(): Flow<Status> = callbackFlow {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(n: Network, caps: NetworkCapabilities) {
                this@callbackFlow.trySend(if (caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) Status.Available else Status.Unavailable).isSuccess
            }

            override fun onLost(n: Network) {
                this@callbackFlow.trySend(Status.Unavailable).isSuccess
            }
        }

        cm.registerNetworkCallback(request, callback)
        awaitClose { cm.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged()
}
