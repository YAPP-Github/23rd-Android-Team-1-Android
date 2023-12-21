package com.susu.core.designsystem.component.button.basic

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.susu.core.designsystem.theme.SusuTheme
import com.susu.core.ui.extension.susuClickable

@Composable
fun BasicButton(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    text: String = "",
    textStyle: TextStyle = TextStyle.Default,
    contentColor: Color = Color.Unspecified,
    rippleColor: Color = Color.Unspecified,
    borderColor: Color = Color.Unspecified,
    borderWidth: Dp = 0.dp,
    backgroundColor: Color = Color.Unspecified,
    padding: PaddingValues = PaddingValues(0.dp),
    @DrawableRes leftIcon: Int? = null,
    leftIconContentDescription: String? = null,
    @DrawableRes rightIcon: Int? = null,
    rightIconContentDescription: String? = null,
    iconSpacing: Dp = 0.dp,
    isActive: Boolean = true,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .clip(shape = shape)
            .background(backgroundColor)
            .border(
                width = borderWidth,
                color = borderColor,
                shape = shape,
            )
            .susuClickable(
                rippleColor = rippleColor,
                runIf = isActive,
                onClick = onClick,
            )
            .padding(padding),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier
                .wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(iconSpacing),
        ) {
            leftIcon?.let {
                Icon(
                    modifier = Modifier.height(textStyle.lineHeight.value.dp),
                    painter = painterResource(id = it),
                    contentDescription = leftIconContentDescription,
                    tint = contentColor,
                )
            }

            Text(
                text = text,
                style = textStyle,
                color = contentColor,
            )

            rightIcon?.let {
                Icon(
                    modifier = Modifier.height(textStyle.lineHeight.value.dp),
                    painter = painterResource(id = it),
                    contentDescription = rightIconContentDescription,
                    tint = contentColor,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BasicButtonPreview() {
    SusuTheme {
        BasicButton(
            text = "Button",
            backgroundColor = SusuTheme.colorScheme.primary,
        )
    }
}
