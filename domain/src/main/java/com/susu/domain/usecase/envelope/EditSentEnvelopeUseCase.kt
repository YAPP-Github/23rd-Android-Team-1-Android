package com.susu.domain.usecase.envelope

import com.susu.core.common.runCatchingIgnoreCancelled
import com.susu.core.model.Category
import com.susu.domain.repository.EnvelopesRepository
import com.susu.domain.repository.FriendRepository
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

class EditSentEnvelopeUseCase @Inject constructor(
    private val friendRepository: FriendRepository,
    private val envelopesRepository: EnvelopesRepository,
) {
    suspend operator fun invoke(param: Param) = runCatchingIgnoreCancelled {
        with(param) {
            friendRepository.editFriend(
                id = friendId,
                name = friendName,
                phoneNumber = phoneNumber,
                relationshipId = relationshipId,
                customRelation = customRelation,
            )

            envelopesRepository.editEnvelope(
                id = envelopeId,
                type = "SENT",
                friendId = friendId,
                amount = amount,
                gift = gift,
                memo = memo,
                categoryId = category.id.toLong(),
                customCategory = category.customCategory,
                hasVisited = hasVisited,
                handedOverAt = handedOverAt,
            )
        }
    }

    data class Param(
        val envelopeId: Long,
        val friendId: Long,
        val friendName: String,
        val phoneNumber: String? = null,
        val relationshipId: Long,
        val customRelation: String? = null,
        val category: Category,
        val amount: Long,
        val gift: String? = null,
        val memo: String? = null,
        val handedOverAt: LocalDateTime,
        val hasVisited: Boolean? = null,
    )
}
