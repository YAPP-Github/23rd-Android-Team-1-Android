package com.susu.feature.received.ledgerdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.susu.core.designsystem.component.appbar.SusuDefaultAppBar
import com.susu.core.designsystem.component.appbar.icon.BackIcon
import com.susu.core.designsystem.component.appbar.icon.DeleteText
import com.susu.core.designsystem.component.appbar.icon.EditText
import com.susu.core.designsystem.component.button.GhostButtonColor
import com.susu.core.designsystem.component.button.SmallButtonStyle
import com.susu.core.designsystem.component.button.SusuFloatingButton
import com.susu.core.designsystem.component.button.SusuGhostButton
import com.susu.core.designsystem.theme.Gray25
import com.susu.core.designsystem.theme.Gray50
import com.susu.core.designsystem.theme.SusuTheme
import com.susu.core.model.Ledger
import com.susu.core.ui.R
import com.susu.core.ui.alignList
import com.susu.core.ui.extension.collectWithLifecycle
import com.susu.core.ui.extension.encodeToUri
import com.susu.feature.received.ledgerdetail.component.LedgerDetailEnvelopeContainer
import com.susu.feature.received.ledgerdetail.component.LedgerDetailOverviewColumn
import kotlinx.serialization.json.Json

@Composable
fun LedgerDetailRoute(
    viewModel: LedgerDetailViewModel = hiltViewModel(),
    ledger: String?,
    navigateLedgerEdit: (Ledger) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    viewModel.sideEffect.collectWithLifecycle { sideEffect ->
        when (sideEffect) {
            is LedgerDetailSideEffect.NavigateLedgerEdit -> navigateLedgerEdit(sideEffect.ledger)
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.initData(ledger)
    }

    LedgerDetailScreen(
        uiState = uiState,
        onClickEdit = viewModel::navigateLedgerEdit,
    )
}

@Composable
fun LedgerDetailScreen(
    uiState: LedgerDetailState = LedgerDetailState(),
    onClickBack: () -> Unit = {},
    onClickEdit: () -> Unit = {},
    onClickDelete: () -> Unit = {},
    onClickFilterButton: () -> Unit = {},
    onClickAlignButton: () -> Unit = {},
    onClickEnvelopeAddButton: () -> Unit = {},
    onClickFloatingButton: () -> Unit = {},
    onClickSeeMoreIcon: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .background(SusuTheme.colorScheme.background15)
            .fillMaxSize(),
    ) {
        Column {
            SusuDefaultAppBar(
                leftIcon = {
                    BackIcon(onClick = onClickBack)
                },
                actions = {
                    Row(
                        modifier = Modifier.padding(horizontal = SusuTheme.spacing.spacing_m),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(SusuTheme.spacing.spacing_m),
                    ) {
                        EditText(onClickEdit)
                        DeleteText(onClickDelete)
                    }
                },
            )

            LazyColumn(
                contentPadding = PaddingValues(
                    vertical = SusuTheme.spacing.spacing_xl,
                ),
            ) {
                item {
                    with(uiState) {
                        LedgerDetailOverviewColumn(
                            money = money,
                            count = count,
                            eventName = name,
                            eventCategory = category,
                            eventRange = "$startDate - $endDate",
                        )
                    }
                }

                item {
                    Spacer(
                        modifier = Modifier
                            .padding(vertical = SusuTheme.spacing.spacing_m)
                            .fillMaxWidth()
                            .height(8.dp)
                            .background(Gray25),
                    )
                }

                item {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = SusuTheme.spacing.spacing_m,
                        ),
                        horizontalArrangement = Arrangement.spacedBy(SusuTheme.spacing.spacing_xxs),
                    ) {
                        SusuGhostButton(
                            color = GhostButtonColor.Black,
                            style = SmallButtonStyle.height32,
                            text = stringResource(R.string.word_filter),
                            leftIcon = {
                                Icon(
                                    modifier = Modifier.size(16.dp),
                                    painter = painterResource(id = R.drawable.ic_filter),
                                    contentDescription = null,
                                )
                            },
                            onClick = onClickFilterButton,
                        )

                        SusuGhostButton(
                            color = GhostButtonColor.Black,
                            style = SmallButtonStyle.height32,
                            text = alignList[0], // TODO State 변환
                            leftIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_align),
                                    contentDescription = null,
                                )
                            },
                            onClick = onClickAlignButton,
                        )
                    }
                }

                var showEmptyScreen = false // TODO Refactor
                if (showEmptyScreen) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 104.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(SusuTheme.spacing.spacing_m),
                        ) {
                            Text(
                                text = stringResource(com.susu.feature.received.R.string.ledger_detail_screen_empty_envelope),
                                style = SusuTheme.typography.text_s,
                                color = Gray50,
                            )
                            SusuGhostButton(
                                color = GhostButtonColor.Black,
                                style = SmallButtonStyle.height40,
                                text = stringResource(com.susu.feature.received.R.string.ledger_detail_screen_add_envelope),
                                onClick = onClickEnvelopeAddButton,
                            )
                        }
                    }
                } else {
                    items(count = 100) {
                        LedgerDetailEnvelopeContainer(
                            onClickSeeMoreIcon = onClickSeeMoreIcon,
                        )
                    }
                }
            }
        }

        SusuFloatingButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(SusuTheme.spacing.spacing_l),
            onClick = onClickFloatingButton,
        )
    }
}

@Preview
@Composable
fun LedgerDetailScreenPreview() {
    SusuTheme {
        LedgerDetailScreen()
    }
}
