package com.susu.feature.sent.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.susu.feature.envelope.SentEnvelopeRoute
import com.susu.feature.envelopeadd.SentEnvelopeAddRoute
import com.susu.feature.envelopedetail.SentEnvelopeDetailRoute
import com.susu.feature.envelopeedit.SentEnvelopeEditRoute
import com.susu.feature.envelopefilter.EnvelopeFilterRoute
import com.susu.feature.sent.SentRoute

fun NavController.navigateSent(navOptions: NavOptions) {
    navigate(SentRoute.route, navOptions)
}

fun NavController.navigateSentEnvelope() {
    navigate(SentRoute.sentEnvelopeRoute)
}

fun NavController.navigateSentEnvelopeDetail() {
    navigate(SentRoute.sentEnvelopeDetailRoute)
}

fun NavController.navigateSentEnvelopeEdit() {
    navigate(SentRoute.sentEnvelopeEditRoute)
}

fun NavController.navigateSentEnvelopeAdd() {
    navigate(SentRoute.sentEnvelopeAddRoute)
}

fun NavController.navigateEnvelopeFilter(filter: String) {
    navigate(SentRoute.envelopeFilterRoute(filter))
}

fun NavGraphBuilder.sentNavGraph(
    padding: PaddingValues,
    popBackStack: () -> Unit,
    navigateSentEnvelope: () -> Unit,
    navigateSentEnvelopeDetail: () -> Unit,
    navigateSentEnvelopeEdit: () -> Unit,
    navigateSentEnvelopeAdd: () -> Unit,
    popBackStackWithFilter: (String) -> Unit,
    handleException: (Throwable, () -> Unit) -> Unit,
) {
    composable(route = SentRoute.route) {
        SentRoute(
            padding = padding,
            navigateSentEnvelope = navigateSentEnvelope,
            navigateSentEnvelopeAdd = navigateSentEnvelopeAdd,
        )
    }

    composable(route = SentRoute.sentEnvelopeRoute) {
        SentEnvelopeRoute(
            popBackStack = popBackStack,
            navigateSentEnvelopeDetail = navigateSentEnvelopeDetail,
        )
    }

    composable(route = SentRoute.sentEnvelopeDetailRoute) {
        SentEnvelopeDetailRoute(
            popBackStack = popBackStack,
            navigateSentEnvelopeEdit = navigateSentEnvelopeEdit,
        )
    }

    composable(route = SentRoute.sentEnvelopeEditRoute) {
        SentEnvelopeEditRoute(
            popBackStack = popBackStack,
            navigateSentEnvelopeDetail = navigateSentEnvelopeDetail,
        )
    }

    composable(route = SentRoute.sentEnvelopeAddRoute) {
        SentEnvelopeAddRoute(
            popBackStack = popBackStack,
            handleException = handleException,
        )
    }

    composable(
        route = SentRoute.envelopeFilterRoute("{${SentRoute.FILTER_ENVELOPE_ARGUMENT}}"),
    ) {
        EnvelopeFilterRoute(
            popBackStack = popBackStack,
            popBackStackWithFilter = popBackStackWithFilter,
            handleException = handleException,
        )
    }
}

object SentRoute {
    const val route = "sent"
    const val sentEnvelopeRoute = "sent-envelope"
    const val sentEnvelopeDetailRoute = "sent-envelope-detail"
    const val sentEnvelopeEditRoute = "sent-envelope-edit"
    const val sentEnvelopeAddRoute = "sent-envelope-add"
    const val FILTER_ENVELOPE_ARGUMENT = "filter-envelope"

    fun envelopeFilterRoute(filter: String) = "envelope-filter/$filter"
}
