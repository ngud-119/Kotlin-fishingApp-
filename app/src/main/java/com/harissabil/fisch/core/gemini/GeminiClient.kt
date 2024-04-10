package com.harissabil.fisch.core.gemini

import com.google.ai.client.generativeai.GenerativeModel
import com.harissabil.fisch.BuildConfig
import com.harissabil.fisch.core.common.util.Constant.GEMINI_PRO
import com.harissabil.fisch.core.common.util.Constant.GEMINI_PRO_VISION

class GeminiClient {

    val geneminiProModel by lazy {
        GenerativeModel(
            modelName = GEMINI_PRO,
            apiKey = BuildConfig.GEMINI_API_KEY,
        ).apply {
            startChat()
        }
    }

    val geneminiProVisionModel by lazy {
        GenerativeModel(
            modelName = GEMINI_PRO_VISION,
            apiKey = BuildConfig.GEMINI_API_KEY,
        ).apply {
            startChat()
        }
    }
}