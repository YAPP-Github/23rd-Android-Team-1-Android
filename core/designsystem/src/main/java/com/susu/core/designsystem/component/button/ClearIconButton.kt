package com.susu.core.designsystem.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.susu.core.designsystem.R
import com.susu.core.designsystem.theme.Gray30
import com.susu.core.ui.extension.susuClickable

@Composable
fun ClearIconButton(
    iconSize: Dp,
    tint: Color = Gray30,
    onClick: () -> Unit,
) {
    Icon(
        modifier = Modifier
            .clip(CircleShape)
            .size(iconSize)
            .susuClickable(onClick = onClick),
        painter = painterResource(id = R.drawable.ic_clear),
        contentDescription = "",
        tint = tint,
    )
}
