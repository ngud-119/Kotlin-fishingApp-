package com.harissabil.fisch.feature.logbook.add_catch.domain.usecase

import com.harissabil.fisch.feature.logbook.add_catch.domain.IntroManager
import javax.inject.Inject

class ReadIntroShown @Inject constructor(
    private val introManager: IntroManager
) {
    operator fun invoke() = introManager.readIntroShown()
}