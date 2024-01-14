package com.susu.feature.envelopeedit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.susu.core.designsystem.theme.Gray70
import com.susu.core.designsystem.theme.SusuTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditDetailItem(
    modifier: Modifier = Modifier,
    categoryText: String,
    categoryStyle: TextStyle = SusuTheme.typography.title_xxs,
    categoryTextColor: Color = Gray70,
    categoryWidth: Dp = 72.dp,
    categoryTextAlign: Alignment.Vertical,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = SusuTheme.spacing.spacing_m),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = categoryText,
            style = categoryStyle,
            color = categoryTextColor,
            modifier = modifier
                .width(categoryWidth)
                .align(categoryTextAlign),
        )
        Spacer(modifier = modifier.size(SusuTheme.spacing.spacing_m))
        FlowRow(
            modifier = modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(SusuTheme.spacing.spacing_xxs),
            verticalArrangement = Arrangement.spacedBy(SusuTheme.spacing.spacing_xxs),
        ) {
            content()
        }
    }
}
