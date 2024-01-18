package com.susu.feature.mypage.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.susu.core.designsystem.component.appbar.SusuDefaultAppBar
import com.susu.core.designsystem.component.appbar.icon.LogoIcon
import com.susu.core.designsystem.component.appbar.icon.NotificationIcon
import com.susu.core.designsystem.component.button.GhostButtonColor
import com.susu.core.designsystem.component.button.SmallButtonStyle
import com.susu.core.designsystem.component.button.SusuGhostButton
import com.susu.core.designsystem.theme.Gray100
import com.susu.core.designsystem.theme.Gray20
import com.susu.core.designsystem.theme.Gray50
import com.susu.core.designsystem.theme.Gray60
import com.susu.core.designsystem.theme.SusuTheme
import com.susu.core.ui.extension.collectWithLifecycle
import com.susu.core.ui.R
import com.susu.core.ui.extension.susuClickable
import com.susu.feature.mypage.main.component.MyPageMenuItem

@Composable
fun MyPageDefaultRoute(
    padding: PaddingValues,
    viewModel: MyPageViewModel = hiltViewModel(),
    navigateToLogin: () -> Unit,
    navigateToInfo: () -> Unit,
    navigateToSocial: () -> Unit,
) {
    viewModel.sideEffect.collectWithLifecycle { sideEffect ->
        when (sideEffect) {
            MyPageEffect.NavigateToLogin -> navigateToLogin()
            MyPageEffect.NavigateToInfo -> navigateToInfo()
            MyPageEffect.NavigateToSocial -> navigateToSocial()
            is MyPageEffect.ShowToast -> {}
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MyPageDefaultScreen(
        padding = padding,
        uiState = uiState,
        onLogout = viewModel::logout,
        onWithdraw = viewModel::withdraw,
        navigateToInfo = navigateToInfo,
        navigateToSocial = navigateToSocial,
    )
}

@Composable
fun MyPageDefaultScreen(
    padding: PaddingValues,
    uiState: MyPageState = MyPageState(),
    onLogout: () -> Unit = {},
    onWithdraw: () -> Unit = {},
    navigateToInfo: () -> Unit = {},
    navigateToSocial: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(padding),
    ) {
        SusuDefaultAppBar(
            modifier = Modifier.padding(SusuTheme.spacing.spacing_xs),
            leftIcon = { LogoIcon() },
            actions = { NotificationIcon() },
        )

        MyPageMenuItem(
            titleText = uiState.userName,
            titleTextColor = Gray100,
            titleTextStyle = SusuTheme.typography.title_m,
            action = {
                Row(
                    modifier = Modifier.susuClickable(onClick = navigateToInfo),
                ) {
                    Text(text = "내 정보", style = SusuTheme.typography.title_xxs, color = Gray60)
                    Spacer(modifier = Modifier.width(SusuTheme.spacing.spacing_xxs))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = "내 정보 보기",
                        tint = Gray60,
                    )
                }
            },
            padding = PaddingValues(SusuTheme.spacing.spacing_m),
            actionItemPadding = SusuTheme.spacing.spacing_xxl,
        )

        MyPageDivider()

        MyPageMenuItem(
            titleText = "연동된 소셜 계정",
            onMenuClick = navigateToSocial,
        )
        MyPageMenuItem(
            titleText = "엑셀 파일 내보내기",
        )
        MyPageMenuItem(
            titleText = "개인정보 처리 방침",
        )

        MyPageDivider()

        MyPageMenuItem(
            titleText = "앱 버전",
            action = {
                Text(text = "업데이트 하기", style = SusuTheme.typography.title_xxs, color = Gray60)
            },
        )

        MyPageDivider()

        MyPageMenuItem(
            titleText = "로그아웃",
            onMenuClick = onLogout,
        )
        MyPageMenuItem(
            titleText = "탈퇴하기",
            onMenuClick = onWithdraw,
        )
        Box(
            modifier = Modifier.fillMaxWidth()
                .weight(1f)
                .background(color = Gray20)
                .padding(SusuTheme.spacing.spacing_m),
        ) {
            Text(
                modifier = Modifier.align(Alignment.TopStart),
                text = "앱 버전 ${uiState.appVersion}",
                style = SusuTheme.typography.title_xxxs,
                color = Gray50,
            )
            SusuGhostButton(
                modifier = Modifier.align(Alignment.Center),
                color = GhostButtonColor.Orange,
                style = SmallButtonStyle.height40,
                text = "수수에게 피드백 남기기",
            )
        }
    }
}

@Composable
fun MyPageDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 8.dp,
    color: Color = Gray20,
) {
    HorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color,
    )
}

@Preview(showBackground = true)
@Composable
fun MyPageDefaultScreenPreview() {
    SusuTheme {
        MyPageDefaultScreen(padding = PaddingValues(0.dp))
    }
}