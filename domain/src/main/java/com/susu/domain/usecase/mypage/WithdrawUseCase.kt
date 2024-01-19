package com.susu.domain.usecase.mypage

import com.susu.core.common.runCatchingIgnoreCancelled
import com.susu.domain.repository.TokenRepository
import com.susu.domain.repository.UserRepository
import javax.inject.Inject

class WithdrawUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
) {
    suspend operator fun invoke() = runCatchingIgnoreCancelled {
        println("탈퇴해요~~~")
        userRepository.withdraw()
        tokenRepository.deleteTokens()
    }
}
