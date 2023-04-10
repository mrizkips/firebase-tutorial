package com.mrizkips.firebasetutorial.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User (
    val name: String? = null,
    val photoUrl: String? = null,
    val email: String? = null
)