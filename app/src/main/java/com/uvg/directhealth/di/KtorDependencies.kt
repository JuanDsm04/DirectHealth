package com.uvg.directhealth.di

import com.uvg.directhealth.data.network.HttpClientFactory
import io.ktor.client.HttpClient

object KtorDependencies {
    private var httpClient: HttpClient? = null
    private fun buildHttpClient(): HttpClient = HttpClientFactory.create()

    fun provideHttpClient(): HttpClient {
        return httpClient ?: synchronized(this) {
            httpClient ?: buildHttpClient().also { httpClient = it }
        }
    }
}