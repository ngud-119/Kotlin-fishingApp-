package com.harissabil.fisch.feature.map.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harissabil.fisch.core.common.util.Resource
import com.harissabil.fisch.core.firebase.firestore.data.mapper.toMap
import com.harissabil.fisch.core.firebase.firestore.domain.usecase.GetMaps
import com.harissabil.fisch.feature.map.data.toMapItem
import com.harissabil.fisch.feature.map.domain.MapItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getMaps: GetMaps,
) : ViewModel() {

    private val _maps = MutableStateFlow<List<MapItem>?>(emptyList())
    val maps: StateFlow<List<MapItem>?> = _maps.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow: SharedFlow<UIEvent> = _eventFlow.asSharedFlow()

    init {
        getMaps()
    }

    private fun getMaps() = viewModelScope.launch {
        getMaps.invoke().collect { response ->
            when (response) {
                is Resource.Error -> {
                    _eventFlow.emit(
                        UIEvent.ShowSnackbar(
                            response.message ?: "Something went wrong!"
                        )
                    )
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    Timber.d("Maps: ${response.data}")
                    _maps.update { response.data?.map { it.toMap().toMapItem() } }
                }
            }
        }
//        _maps.update {
//
//            fun getRandomPosition(): LatLng {
//                return LatLng(
//                    1.35 + Random.nextFloat(),
//                    103.87 + Random.nextFloat()
//                );
//            }
//
//            val placelist = mutableListOf<MapItem>()
//            for (i in 0..69) {
//                placelist.add(
//                    MapItem(
//                        id = i.toString(),
//                        logbookRef = null,
//                        placeName = "Place $i",
//                        latLong = getRandomPosition()
//                    )
//                )
//            }
//
//            placelist
//        }
    }

    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}