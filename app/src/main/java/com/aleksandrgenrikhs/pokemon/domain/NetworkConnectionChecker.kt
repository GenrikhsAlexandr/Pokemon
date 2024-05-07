package com.aleksandrgenrikhs.pokemon.domain

import android.content.Context

interface NetworkConnectionChecker {
    fun isNetworkConnected(context: Context): Boolean
}