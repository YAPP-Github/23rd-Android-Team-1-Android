package com.susu.feature.envelopefilter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.analytics.FirebaseAnalytics
import com.susu.core.designsystem.component.appbar.SusuDefaultAppBar
import com.susu.core.designsystem.component.appbar.icon.BackIcon
import com.susu.core.designsystem.component.button.FilledButtonColor
import com.susu.core.designsystem.component.button.LinedButtonColor
import com.susu.core.designsystem.component.button.RefreshButton
import com.susu.core.designsystem.component.button.SelectedFilterButton
import com.susu.core.designsystem.component.button.SmallButtonStyle
import com.susu.core.designsystem.component.button.SusuFilledButton
import com.susu.core.designsystem.component.button.SusuLinedButton
import com.susu.core.designsystem.component.button.XSmallButtonStyle
import com.susu.core.designsystem.theme.SusuTheme
import com.susu.core.model.Friend
import com.susu.core.ui.extension.collectWithLifecycle
import com.susu.core.ui.extension.toMoneyFormat
import com.susu.feature.envelopefilter.component.MoneySlider
import com.susu.feature.envelopefilter.component.SearchBar
import com.susu.feature.sent.R
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
@Composable
fun EnvelopeFilterRoute(
    viewModel: EnvelopeFilterViewModel = hiltViewModel(),
    popBackStack: () -> Unit,
    popBackStackWithFilter: (String) -> Unit,
    handleException: (Throwable, () -> Unit) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    viewModel.sideEffect.collectWithLifecycle { sideEffect ->
        when (sideEffect) {
            is EnvelopeFilterSideEffect.HandleException -> handleException(sideEffect.throwable, sideEffect.retry)
            EnvelopeFilterSideEffect.PopBackStack -> popBackStack()
            is EnvelopeFilterSideEffect.PopBackStackWithFilter -> popBackStackWithFilter(sideEffect.filter)
            EnvelopeFilterSideEffect.LogClickApplyButtonEvent -> scope.launch {
                FirebaseAnalytics.getInstance(context).logEvent(
                    FirebaseAnalytics.Event.SELECT_CONTENT,
                    bundleOf(
                        FirebaseAnalytics.Param.CONTENT_TYPE to "sent_filter_apply_button",
                    ),
                )
            }

            EnvelopeFilterSideEffect.LogClickFriendButtonEvent -> scope.launch {
                FirebaseAnalytics.getInstance(context).logEvent(
                    FirebaseAnalytics.Event.SELECT_CONTENT,
                    bundleOf(
                        FirebaseAnalytics.Param.CONTENT_TYPE to "sent_filter_friend_button",
                    ),
                )
            }

            EnvelopeFilterSideEffect.LogClickSliderEvent -> scope.launch {
                FirebaseAnalytics.getInstance(context).logEvent(
                    FirebaseAnalytics.Event.SELECT_CONTENT,
                    bundleOf(
                        FirebaseAnalytics.Param.CONTENT_TYPE to "sent_filter_slider",
                    ),
                )
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.initData()
    }

    LaunchedEffect(key1 = uiState.searchKeyword) {
        snapshotFlow { uiState.searchKeyword }
            .debounce(100L)
            .collect(viewModel::getFriendList)
    }

    LaunchedEffect(key1 = uiState.fromAmount, key2 = uiState.toAmount) {
        snapshotFlow { uiState.fromAmount }
            .debounce(1000L)
            .collect {
                viewModel.logSliderClickEvent()
            }

        snapshotFlow { uiState.toAmount }
            .debounce(1000L)
            .collect {
                viewModel.logSliderClickEvent()
            }
    }

    EnvelopeFilterScreen(
        uiState = uiState,
        onClickBackIcon = viewModel::popBackStack,
        onClickApplyFilterButton = {
            viewModel.popBackStackWithFilter()
            viewModel.logApplyClickEvent()
        },
        onClickRefreshButton = viewModel::clearFilter,
        onTextChangeSearch = viewModel::updateName,
        onClickFriendChip = { friend ->
            viewModel.toggleFriend(friend)
            viewModel.logFriendClickEvent()
        },
        onCloseFriendChip = viewModel::unselectFriend,
        onMoneyValueChange = viewModel::updateMoneyRange,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EnvelopeFilterScreen(
    uiState: EnvelopeFilterState = EnvelopeFilterState(),
    onClickBackIcon: () -> Unit = {},
    onClickApplyFilterButton: () -> Unit = {},
    onClickRefreshButton: () -> Unit = {},
    onTextChangeSearch: (String) -> Unit = {},
    onClickFriendChip: (Friend) -> Unit = {},
    onCloseFriendChip: (Friend) -> Unit = {},
    onMoneyValueChange: (Float?, Float?) -> Unit = { _, _ -> },
) {
    Column(
        modifier = Modifier
            .background(SusuTheme.colorScheme.background10)
            .fillMaxSize(),
    ) {
        SusuDefaultAppBar(
            leftIcon = {
                BackIcon(onClickBackIcon)
            },
            title = stringResource(id = com.susu.core.ui.R.string.word_filter),
        )

        Column(
            modifier = Modifier
                .padding(
                    top = SusuTheme.spacing.spacing_xl,
                    start = SusuTheme.spacing.spacing_m,
                    end = SusuTheme.spacing.spacing_m,
                )
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(text = stringResource(R.string.envelope_filter_screen_friend), style = SusuTheme.typography.title_xs)
            Spacer(modifier = Modifier.size(SusuTheme.spacing.spacing_m))
            SearchBar(
                value = uiState.searchKeyword,
                placeholder = stringResource(R.string.envelope_filter_search_placeholder),
                onValueChange = onTextChangeSearch,
            )
            Spacer(modifier = Modifier.size(SusuTheme.spacing.spacing_m))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(SusuTheme.spacing.spacing_xxs),
                verticalArrangement = Arrangement.spacedBy(SusuTheme.spacing.spacing_xxs),
            ) {
                uiState.friendList.forEach { friend ->
                    SusuLinedButton(
                        color = LinedButtonColor.Black,
                        style = XSmallButtonStyle.height28,
                        isActive = friend in uiState.selectedFriendList,
                        text = friend.name,
                        onClick = { onClickFriendChip(friend) },
                    )
                }
            }

            Spacer(modifier = Modifier.size(SusuTheme.spacing.spacing_xxxxxxl))

            if (uiState.maxFromAmount != uiState.maxToAmount) {
                Text(
                    text = stringResource(
                        if (uiState.isSent) {
                            R.string.envelope_filter_total_money
                        } else {
                            R.string.envelope_filter_screen_money
                        },
                    ),
                    style = SusuTheme.typography.title_xs,
                )
                Spacer(modifier = Modifier.size(SusuTheme.spacing.spacing_m))

                Text(
                    text = stringResource(
                        R.string.envelope_filter_range_text,
                        uiState.sliderValue.start.toMoneyFormat(),
                        uiState.sliderValue.endInclusive.toMoneyFormat(),
                    ),
                    style = SusuTheme.typography.title_m,
                )

                Spacer(modifier = Modifier.size(SusuTheme.spacing.spacing_xxs))

                MoneySlider(
                    value = uiState.sliderValue,
                    onValueChange = { onMoneyValueChange(it.start, it.endInclusive) },
                    valueRange = uiState.sliderValueRange,
                )
            }

            Spacer(modifier = Modifier.size(SusuTheme.spacing.spacing_s))
        }

        Column(
            modifier = Modifier
                .padding(
                    start = SusuTheme.spacing.spacing_m,
                    end = SusuTheme.spacing.spacing_m,
                    bottom = SusuTheme.spacing.spacing_xxs,
                )
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(SusuTheme.spacing.spacing_m),
        ) {
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(SusuTheme.spacing.spacing_xxs),
                horizontalArrangement = Arrangement.spacedBy(SusuTheme.spacing.spacing_xxs),
            ) {
                uiState.selectedFriendList.forEach { friend ->
                    SelectedFilterButton(
                        name = friend.name,
                        onClickCloseIcon = { onCloseFriendChip(friend) },
                    )
                }

                if (uiState.fromAmount != null) {
                    SelectedFilterButton(
                        name = "${uiState.sliderValue.start.toMoneyFormat()}~${uiState.sliderValue.endInclusive.toMoneyFormat()}",
                        onClickCloseIcon = { onMoneyValueChange(null, null) },
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(SusuTheme.spacing.spacing_m),
            ) {
                RefreshButton(onClick = onClickRefreshButton)

                SusuFilledButton(
                    modifier = Modifier.fillMaxWidth(),
                    color = FilledButtonColor.Black,
                    style = SmallButtonStyle.height48,
                    isActive = true,
                    text = stringResource(com.susu.core.ui.R.string.word_apply_filter),
                    onClick = onClickApplyFilterButton,
                )
            }
        }
    }
}

@Preview
@Composable
fun EnvelopeFilterScreenPreview() {
    SusuTheme {
        EnvelopeFilterScreen()
    }
}
