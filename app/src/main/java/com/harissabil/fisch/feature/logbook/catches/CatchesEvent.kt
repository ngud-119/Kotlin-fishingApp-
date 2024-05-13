package com.harissabil.fisch.feature.logbook.catches

import com.harissabil.fisch.core.firebase.firestore.domain.model.Logbook

sealed class CatchesEvent {

    data class UpdateSearchQuery(val searchQuery: String) : CatchesEvent()

    data object SortIconClick : CatchesEvent()

    data class SortCatches(val sortBy: SortBy) : CatchesEvent()

    data object GetLogbooks : CatchesEvent()

    data class MoreOption(val logbook: Logbook) : CatchesEvent()

    data object EditLogbook : CatchesEvent()

    data object DeleteLogbook : CatchesEvent()
}
