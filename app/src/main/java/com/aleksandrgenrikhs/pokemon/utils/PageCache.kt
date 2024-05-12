package com.aleksandrgenrikhs.pokemon.utils

import com.aleksandrgenrikhs.pokemon.domain.Page

object PageCache {

    private val cache = HashMap<Int, Page>()

    fun getPage(key: Int): Page? {
        return cache[key]
    }

    fun putPage(key: Int, value: Page) {
        cache[key] = value
    }
}