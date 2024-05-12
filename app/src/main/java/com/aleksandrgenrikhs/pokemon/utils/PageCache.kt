package com.aleksandrgenrikhs.pokemon.utils

import com.aleksandrgenrikhs.pokemon.domain.Page
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object PageCache {

    private val cache = HashMap<Int, Page>()

    private val mutex = Mutex()

    suspend fun getPage(key: Int): Page? = mutex.withLock {cache[key]}

    suspend fun putPage(key: Int, value: Page) {
       mutex.withLock {cache[key] = value}
    }
}