package com.harissabil.fisch.feature.logbook.catches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harissabil.fisch.core.common.util.Resource
import com.harissabil.fisch.core.firebase.firestore.data.mapper.toLogbook
import com.harissabil.fisch.core.firebase.firestore.domain.model.Logbook
import com.harissabil.fisch.core.firebase.firestore.domain.usecase.DeleteLogbook
import com.harissabil.fisch.core.firebase.firestore.domain.usecase.GetLogbooks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CatchesViewModel @Inject constructor(
    private val getLogbooks: GetLogbooks,
    private val deleteLogbook: DeleteLogbook,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortOption = MutableStateFlow(SortBy.LATEST)
    val sortOption: StateFlow<SortBy> = _sortOption.asStateFlow()

    private val _logbooks = MutableStateFlow<List<Logbook>?>(null)

    val filteredLogbooks = combine(
        _searchQuery,
        _logbooks,
        _sortOption
    ) { query, logbooks, sortOption ->
        // Filtering based on search query
        val filteredList = if (query.isEmpty()) {
            logbooks
        } else {
            logbooks?.filter { logbook ->
                query in (logbook.jenisIkan?.lowercase().orEmpty()) ||
                        query in (logbook.tempatPenangkapan?.lowercase().orEmpty())
            }
        }

        // Sorting based on the selected option
        val sortedList = when (sortOption) {
            SortBy.LATEST -> filteredList?.sortedByDescending { it.waktuPenangkapan }
            SortBy.OLDEST -> filteredList?.sortedBy { it.waktuPenangkapan }
            SortBy.FISH_NAME -> filteredList?.sortedBy { it.jenisIkan }
            SortBy.LOCATION -> filteredList?.sortedBy { it.tempatPenangkapan }
        }

        sortedList
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = null
    )

    private val _logbookOnMore = MutableStateFlow<Logbook?>(null)
    val logbookOnMore: StateFlow<Logbook?> = _logbookOnMore.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow: SharedFlow<UIEvent> = _eventFlow.asSharedFlow()

    init {
        getLogbooks()
    }

    fun onEvent(event: CatchesEvent) {
        when (event) {
            is CatchesEvent.UpdateSearchQuery -> updateSearchQuery(event.searchQuery)
            is CatchesEvent.SortIconClick -> Unit
            is CatchesEvent.SortCatches -> sortCatches(event.sortBy)
            is CatchesEvent.GetLogbooks -> getLogbooks()
            is CatchesEvent.MoreOption -> moreOption(event.logbook)
            is CatchesEvent.EditLogbook -> Unit
            is CatchesEvent.DeleteLogbook -> deleteLogbook()
        }
    }

    private fun updateSearchQuery(newSearchQuery: String) {
        _searchQuery.update { newSearchQuery }
    }

    private fun sortCatches(sortBy: SortBy) {
        _sortOption.update { sortBy }
    }

    private fun getLogbooks() = viewModelScope.launch {
        _isLoading.update { true }
//        delay(5000)
//        _isLoading.update { false }
//        _logbooks.value = provideDummyLogbooks()
        getLogbooks.invoke().collect { response ->
            when (response) {
                is Resource.Error -> {
                    Timber.e("Error: ${response.message}")
                    _eventFlow.emit(
                        UIEvent.ShowSnackbar(
                            response.message ?: "Something went wrong!"
                        )
                    )
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    Timber.d("Logbooks: ${response.data}")
                    _logbooks.update { response.data?.map { it.toLogbook() } }
                    _isLoading.update { false }
                }
            }
        }
    }

    private fun moreOption(logbook: Logbook) {
        _logbookOnMore.update { logbook }
    }

    private fun deleteLogbook() = viewModelScope.launch {
        _logbookOnMore.value?.let {
            Timber.d("Delete logbook: $it; ImageUrl: ${it.fotoIkan}")
            val response = deleteLogbook.invoke(it.id ?: "", it.fotoIkan)
            when (response) {
                is Resource.Error -> {
                    _eventFlow.emit(
                        UIEvent.ShowSnackbar(
                            response.message ?: "Something went wrong"
                        )
                    )
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    _eventFlow.emit(UIEvent.ShowSnackbar("Catches deleted successfully"))
                }
            }
            _logbookOnMore.value = null
        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}

fun provideDummyLogbooks(): List<Logbook> = listOf(
    Logbook(
        id = "1",
        email = "tes@gmail.com",
        jenisIkan = "Ikan Hiu",
        jumlahIkan = 10,
        tempatPenangkapan = "Florida",
        waktuPenangkapan = null,
        fotoIkan = "https://www.fisheries.noaa.gov/s3//2023-06/750x500-Great-White-iStock.jpg",
        catatan = null
    ),
    Logbook(
        id = "2",
        email = "tes@gmail.com",
        jenisIkan = "Gurita",
        jumlahIkan = 10,
        tempatPenangkapan = "Laut Pasifik",
        waktuPenangkapan = null,
        fotoIkan = "https://www.aquariumofpacific.org/images/made_new/email_images-godzilla_in_new_exhibit_600_q85.jpg",
        catatan = null
    ),
    Logbook(
        id = "3",
        email = "tes@gmail.com",
        jenisIkan = "Piranha",
        jumlahIkan = 10,
        tempatPenangkapan = "Amazon",
        waktuPenangkapan = null,
        fotoIkan = "https://www.balisafarimarinepark.com/wp-content/uploads/2022/02/foto-ikan-piranha-600x401.jpg?p=27780",
        catatan = null
    ),
    Logbook(
        id = "4",
        email = "tes@gmail.com",
        jenisIkan = "Ikan Mas",
        jumlahIkan = 10,
        tempatPenangkapan = "Sungai Deli",
        waktuPenangkapan = null,
        fotoIkan = "https://awsimages.detik.net.id/community/media/visual/2021/07/15/ikan-mas-raksasa.jpeg?w=1200",
        catatan = null
    ),
    Logbook(
        id = "5",
        email = "tes@gmail.com",
        jenisIkan = "Ikan Mas",
        jumlahIkan = 10,
        tempatPenangkapan = null,
        waktuPenangkapan = null,
        fotoIkan = "https://awsimages.detik.net.id/community/media/visual/2021/07/15/ikan-mas-raksasa.jpeg?w=1200",
        catatan = null
    ),
    Logbook(
        id = "6",
        email = "tes@gmail.com",
        jenisIkan = "Ikan Hiu",
        jumlahIkan = 10,
        tempatPenangkapan = "Florida",
        waktuPenangkapan = null,
        fotoIkan = "https://www.fisheries.noaa.gov/s3//2023-06/750x500-Great-White-iStock.jpg",
        catatan = null
    ),
    Logbook(
        id = "7",
        email = "tes@gmail.com",
        jenisIkan = "Gurita",
        jumlahIkan = 10,
        tempatPenangkapan = "Laut Pasifik",
        waktuPenangkapan = null,
        fotoIkan = "https://www.aquariumofpacific.org/images/made_new/email_images-godzilla_in_new_exhibit_600_q85.jpg",
        catatan = null
    ),
    Logbook(
        id = "8",
        email = "tes@gmail.com",
        jenisIkan = "Piranha",
        jumlahIkan = 10,
        tempatPenangkapan = "Amazon",
        waktuPenangkapan = null,
        fotoIkan = "https://www.balisafarimarinepark.com/wp-content/uploads/2022/02/foto-ikan-piranha-600x401.jpg?p=27780",
        catatan = null
    ),
    Logbook(
        id = "9",
        email = "tes@gmail.com",
        jenisIkan = "Ikan Mas",
        jumlahIkan = 10,
        tempatPenangkapan = "Sungai Deli",
        waktuPenangkapan = null,
        fotoIkan = "https://awsimages.detik.net.id/community/media/visual/2021/07/15/ikan-mas-raksasa.jpeg?w=1200",
        catatan = null
    ),
    Logbook(
        id = "10",
        email = "tes@gmail.com",
        jenisIkan = "Ikan Mas",
        jumlahIkan = 10,
        tempatPenangkapan = null,
        waktuPenangkapan = null,
        fotoIkan = "https://awsimages.detik.net.id/community/media/visual/2021/07/15/ikan-mas-raksasa.jpeg?w=1200",
        catatan = null
    )
)