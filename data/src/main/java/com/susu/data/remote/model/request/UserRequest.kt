package com.susu.data.remote.model.request

import com.susu.core.model.SignUpUser
import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val name: String,
    val gender: String?,
    val termAgreement: List<Int>,
    val birth: Int?,
)

fun SignUpUser.toData() = UserRequest(
    name = name,
    gender = gender.ifEmpty { null },
    birth = if (birth < 0) null else birth,
    termAgreement = termAgreement,
)
