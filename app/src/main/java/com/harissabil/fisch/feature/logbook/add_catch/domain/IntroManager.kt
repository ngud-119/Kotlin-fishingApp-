package com.harissabil.fisch.feature.logbook.add_catch.domain

import kotlinx.coroutines.flow.Flow

interface IntroManager {

    suspend fun saveIntroShown()

    fun readIntroShown(): Flow<Boolean>
}