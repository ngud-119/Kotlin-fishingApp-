package com.harissabil.fisch.feature.profile.data

import com.harissabil.fisch.core.firebase.auth.data.dto.SignedInResponse
import com.harissabil.fisch.feature.profile.domain.model.UserData

internal fun SignedInResponse.toUserData() = UserData(
    userId = userId,
    userName = userName,
    email = email,
    profilePictureUrl = profilePictureUrl
)
