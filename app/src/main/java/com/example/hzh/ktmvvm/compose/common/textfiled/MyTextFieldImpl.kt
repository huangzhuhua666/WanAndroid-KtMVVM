package com.example.hzh.ktmvvm.compose.common.textfiled

import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Create by hzh on 2021/12/7
 */
@Composable
internal fun MyTextFieldImpl(
    enabled: Boolean,
    readOnly: Boolean,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier,
    singleLine: Boolean,
    textStyle: TextStyle,
    placeholder: @Composable (() -> Unit)?,
    leading: @Composable (() -> Unit)?,
    trailing: @Composable (() -> Unit)?,
    visualTransformation: VisualTransformation,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource,
    shape: Shape,
    colors: TextFieldColors
) {
    // If color is not provided via the text style, use content color as a default
    val textColor = textStyle.color.takeOrElse {
        colors.textColor(enabled).value
    }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    val isFocused = interactionSource.collectIsFocusedAsState().value
    val transformedText = remember(value.annotatedString, visualTransformation) {
        visualTransformation.filter(value.annotatedString)
    }.text
    val inputState = when {
        isFocused -> InputPhase.Focused
        transformedText.isEmpty() -> InputPhase.UnfocusedEmpty
        else -> InputPhase.UnfocusedNotEmpty
    }

    TextFieldTransitionScope.Transition(
        inputState = inputState, showLabel = false
    ) { indicatorWidth, placeholderAlphaProgress ->
        val decoratedPlaceholder: @Composable ((Modifier) -> Unit)? =
            if (placeholder != null && transformedText.isEmpty()) {
                @Composable { modifier ->
                    Box(modifier.alpha(placeholderAlphaProgress)) {
                        Decoration(
                            contentColor = colors.placeholderColor(enabled).value,
                            typography = MaterialTheme.typography.subtitle1,
                            content = placeholder
                        )
                    }
                }
            } else null

        // Developers need to handle invalid input manually. But since we don't provide error
        // message slot API, we can set the default error message in case developers forget about
        // it.
        TextFieldLayout(
            modifier = modifier,
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            textStyle = mergedTextStyle,
            singleLine = singleLine,
            maxLines = maxLines,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            decoratedPlaceholder = decoratedPlaceholder,
            leading = leading,
            trailing = trailing,
            leadingColor = colors.leadingIconColor(enabled, false).value,
            trailingColor = colors.trailingIconColor(enabled, false).value,
            indicatorWidth = indicatorWidth,
            indicatorColor =
            colors.indicatorColor(enabled, false, interactionSource).value,
            backgroundColor = colors.backgroundColor(enabled).value,
            cursorColor = colors.cursorColor(false).value,
            shape = shape
        )
    }
}

@Composable
internal fun Decoration(
    contentColor: Color,
    typography: TextStyle? = null,
    contentAlpha: Float? = null,
    content: @Composable () -> Unit
) {
    val colorAndEmphasis: @Composable () -> Unit = @Composable {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            if (contentAlpha != null) {
                CompositionLocalProvider(
                    LocalContentAlpha provides contentAlpha,
                    content = content
                )
            } else {
                CompositionLocalProvider(
                    LocalContentAlpha provides contentColor.alpha,
                    content = content
                )
            }
        }
    }
    if (typography != null) ProvideTextStyle(typography, colorAndEmphasis) else colorAndEmphasis()
}

private object TextFieldTransitionScope {

    @Composable
    fun Transition(
        inputState: InputPhase,
        showLabel: Boolean,
        content: @Composable (
            indicatorWidth: Dp,
            placeholderOpacity: Float
        ) -> Unit
    ) {
        // Transitions from/to InputPhase.Focused are the most critical in the transition below.
        // UnfocusedEmpty <-> UnfocusedNotEmpty are needed when a single state is used to control
        // multiple text fields.
        val transition = updateTransition(inputState, label = "")

        val indicatorWidth by transition.animateDp(
            transitionSpec = { tween(durationMillis = AnimationDuration) }, label = ""
        ) {
            when (it) {
                InputPhase.Focused -> IndicatorFocusedWidth
                InputPhase.UnfocusedEmpty -> IndicatorUnfocusedWidth
                InputPhase.UnfocusedNotEmpty -> IndicatorUnfocusedWidth
            }
        }

        val placeholderOpacity by transition.animateFloat(
            transitionSpec = {
                if (InputPhase.Focused isTransitioningTo InputPhase.UnfocusedEmpty) {
                    tween(
                        durationMillis = PlaceholderAnimationDelayOrDuration,
                        easing = LinearEasing
                    )
                } else if (InputPhase.UnfocusedEmpty isTransitioningTo InputPhase.Focused ||
                    InputPhase.UnfocusedNotEmpty isTransitioningTo InputPhase.UnfocusedEmpty
                ) {
                    tween(
                        durationMillis = PlaceholderAnimationDuration,
                        delayMillis = PlaceholderAnimationDelayOrDuration,
                        easing = LinearEasing
                    )
                } else {
                    spring()
                }
            }, label = ""
        ) {
            when (it) {
                InputPhase.Focused -> 1f
                InputPhase.UnfocusedEmpty -> if (showLabel) 0f else 1f
                InputPhase.UnfocusedNotEmpty -> 0f
            }
        }

        content(
            indicatorWidth,
            placeholderOpacity
        )
    }
}

internal fun widthOrZero(placeable: Placeable?) = placeable?.width ?: 0
internal fun heightOrZero(placeable: Placeable?) = placeable?.height ?: 0

private enum class InputPhase {
    // Text field is focused
    Focused,

    // Text field is not focused and input text is empty
    UnfocusedEmpty,

    // Text field is not focused but input text is not empty
    UnfocusedNotEmpty
}

internal const val TextFieldId = "TextField"
internal const val PlaceholderId = "Hint"
internal const val LeadingId = "Leading"
internal const val TrailingId = "Trailing"

internal const val AnimationDuration = 150
private const val PlaceholderAnimationDuration = 83
private const val PlaceholderAnimationDelayOrDuration = 67

private val IndicatorUnfocusedWidth = 1.dp
private val IndicatorFocusedWidth = 1.dp
internal val TextFieldPadding = 16.dp
internal val HorizontalIconPadding = 12.dp

internal val IconDefaultSizeModifier = Modifier.defaultMinSize(24.dp, 48.dp)