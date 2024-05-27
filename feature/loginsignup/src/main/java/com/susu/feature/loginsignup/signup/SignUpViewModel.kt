package com.susu.feature.loginsignup.signup

import androidx.lifecycle.viewModelScope
import com.susu.core.model.SignUpUser
import com.susu.core.model.exception.UnknownException
import com.susu.core.ui.Gender
import com.susu.core.ui.SnsProviders
import com.susu.core.ui.USER_BIRTH_RANGE
import com.susu.core.ui.USER_INPUT_REGEX
import com.susu.core.ui.USER_NAME_MAX_LENGTH
import com.susu.core.ui.base.BaseViewModel
import com.susu.domain.usecase.loginsignup.SignUpUseCase
import com.susu.feature.loginsignup.social.KakaoLoginHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
) : BaseViewModel<SignUpState, SignUpEffect>(SignUpState()) {

    fun showDatePicker() = intent { copy(showDatePicker = true) }
    fun hideDatePicker() = intent { copy(showDatePicker = false) }

    fun updateName(name: String) {
        val trimmedName = name.trim()
        if (trimmedName.length > USER_NAME_MAX_LENGTH) return

        intent { copy(name = trimmedName, isNameValid = USER_INPUT_REGEX.matches(trimmedName)) }
    }

    fun updateGender(gender: Gender) {
        if (currentState.gender == gender) {
            intent { copy(gender = Gender.NONE) }
        } else {
            intent { copy(gender = gender) }
        }
    }

    fun updateBirth(birth: Int) {
        intent { copy(birth = birth) }
    }

    fun agreeTerm(termId: Int) {
        intent { copy(agreedTerms = agreedTerms + termId) }
    }

    fun disagreeTerm(termId: Int) {
        intent { copy(agreedTerms = agreedTerms - termId) }
    }

    fun agreeAllTerms(entireTermIds: List<Int>) {
        intent { copy(agreedTerms = entireTermIds) }
    }

    fun disagreeAllTerms() {
        intent { copy(agreedTerms = emptyList()) }
    }

    fun goNextStep() {
        when (uiState.value.currentStep) {
            SignUpStep.TERMS -> intent { copy(currentStep = SignUpStep.NAME) }
            SignUpStep.TERM_DETAIL -> intent { copy(currentStep = SignUpStep.TERMS) }
            SignUpStep.NAME -> intent { copy(currentStep = SignUpStep.ADDITIONAL) }
            SignUpStep.ADDITIONAL -> signUp()
        }
    }

    fun goPreviousStep() {
        when (uiState.value.currentStep) {
            SignUpStep.TERMS -> postSideEffect(SignUpEffect.NavigateToLogin)
            SignUpStep.TERM_DETAIL -> intent { copy(currentStep = SignUpStep.TERMS) }
            SignUpStep.NAME -> intent { copy(currentStep = SignUpStep.TERMS) }
            SignUpStep.ADDITIONAL -> intent { copy(currentStep = SignUpStep.NAME) }
        }
    }

    fun goTermDetail() {
        intent { copy(currentStep = SignUpStep.TERM_DETAIL) }
    }

    private fun signUp() {
        KakaoLoginHelper.getAccessToken { oauthAccessToken ->
            viewModelScope.launch {
                intent { copy(isLoading = true) }
                if (oauthAccessToken != null) {
                    signUpUseCase(
                        provider = SnsProviders.Kakao.path,
                        oauthAccessToken = oauthAccessToken,
                        signUpUser = SignUpUser(
                            name = uiState.value.name,
                            gender = uiState.value.gender.content,
                            birth = if (uiState.value.birth in USER_BIRTH_RANGE) {
                                uiState.value.birth
                            } else {
                                null
                            },
                            termAgreement = uiState.value.agreedTerms.filter { it > 0 },
                        ),
                    ).onSuccess {
                        postSideEffect(SignUpEffect.NavigateToReceived)
                    }.onFailure {
                        postSideEffect(SignUpEffect.ShowSnackbar(it.message ?: UnknownException().message))
                    }
                } else {
                    postSideEffect(SignUpEffect.ShowKakaoErrorSnackbar)
                }
                intent { copy(isLoading = false) }
            }
        }
    }
}
