package com.susu.feature.mypage.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.susu.feature.mypage.info.MyPageInfoRoute
import com.susu.feature.mypage.main.MyPageDefaultRoute
import com.susu.feature.mypage.social.MyPageSocialRoute

fun NavController.navigateMyPage(navOptions: NavOptions) {
    navigate(MyPageRoute.default, navOptions)
}

fun NavController.navigateMyPageInfo() {
    navigate(MyPageRoute.info)
}

fun NavController.navigateMyPageSocial() {
    navigate(MyPageRoute.social)
}

fun NavGraphBuilder.myPageNavGraph(
    padding: PaddingValues,
    navigateToLogin: () -> Unit,
    navigateToInfo: () -> Unit,
    navigateToSocial: () -> Unit,
    popBackStack: () -> Unit,
) {
    composable(route = MyPageRoute.default) {
        MyPageDefaultRoute(
            padding = padding,
            navigateToLogin = navigateToLogin,
            navigateToInfo = navigateToInfo,
            navigateToSocial = navigateToSocial,
        )
    }
    composable(route = MyPageRoute.info) {
        MyPageInfoRoute(padding = padding, popBackStack = popBackStack)
    }
    composable(route = MyPageRoute.social) {
        MyPageSocialRoute(padding = padding, popBackStack = popBackStack)
    }
}

object MyPageRoute {
    const val default = "my-page"
    const val info = "info"
    const val social = "social"
}
