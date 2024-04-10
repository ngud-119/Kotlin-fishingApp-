package com.harissabil.fisch.feature.add_catch.presentation

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.type.content
import com.harissabil.fisch.core.gemini.GeminiClient
import com.harissabil.fisch.core.common.util.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCatchViewModel @Inject constructor(
    private val geminiClient: GeminiClient,
) : ViewModel() {

    private val _state = mutableStateOf(AddCatchState())
    val state: State<AddCatchState> = _state

    fun setFishName(fishName: String) {
        _state.value = _state.value.copy(fishName = fishName)
    }

    fun identifyFish(images: SnapshotStateList<Bitmap>) {

        _state.value = _state.value.copy(isGenerating = true)

        val generativeModel =
            if (images.isNotEmpty()) geminiClient.geneminiProVisionModel else geminiClient.geneminiProModel

        val inputContent = content {
            images.forEach { imageBitmap ->
                image(imageBitmap)
            }
            text(Constant.FISH_IDENTIFIER_PROMPT)
        }
        viewModelScope.launch {
            generativeModel.generateContentStream(inputContent)
                .collect { chunk ->
                    _state.value = _state.value.copy(fishName = chunk.text.toString())
                }
            _state.value = _state.value.copy(isGenerating = false)
        }
    }
}