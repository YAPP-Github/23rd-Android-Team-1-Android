package com.susu.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EnvelopesListResponse(
    @SerialName("data")
    val envelopesList: List<EnvelopesResponse>,
    val page: Int,
    val size: Int,
    val sort: Sort,
    val totalCount: Int,
    val totalPage: Int,
)

@Serializable
data class Sort(
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean,
)

internal fun EnvelopesListResponse.toModel() = this.envelopesList.map { envelopes ->
    envelopes.toModel()
}
