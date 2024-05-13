package com.harissabil.fisch.feature.logbook.add_catch.domain.usecase

import com.harissabil.fisch.feature.logbook.add_catch.domain.IntroManager
import javax.inject.Inject

class SaveIntroShown @Inject constructor(
    private val introManager: IntroManager,
) {
    suspend operator fun invoke() = introManager.saveIntroShown()
}