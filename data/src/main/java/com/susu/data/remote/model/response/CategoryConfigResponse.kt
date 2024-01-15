package com.susu.data.remote.model.response

import com.susu.core.model.Category
import kotlinx.serialization.Serializable

@Serializable
data class CategoryConfigResponse(
    val id: Int,
    val seq: Int,
    val name: String,
)

internal fun CategoryConfigResponse.toModel() = Category(
    id = id,
    seq = seq,
    category = name,
)
