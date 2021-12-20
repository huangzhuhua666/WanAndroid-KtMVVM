package com.example.hzh.ktmvvm.compose.common.textfiled

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.offset
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Create by hzh on 2021/12/7
 */
@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape =
        MaterialTheme.shapes.small.copy(bottomEnd = ZeroCornerSize, bottomStart = ZeroCornerSize),
    colors: TextFieldColors = TextFieldDefaults.textFieldColors()
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = textFieldValueState.copy(text = value)

    MyTextField(
        enabled = enabled,
        readOnly = readOnly,
        value = textFieldValue,
        onValueChange = {
            textFieldValueState = it
            if (value != it.text) {
                onValueChange(it.text)
            }
        },
        modifier = modifier,
        singleLine = singleLine,
        textStyle = textStyle,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        maxLines = maxLines,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors
    )
}

@Composable
fun MyTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape =
        MaterialTheme.shapes.small.copy(bottomEnd = ZeroCornerSize, bottomStart = ZeroCornerSize),
    colors: TextFieldColors = TextFieldDefaults.textFieldColors()
) {
    MyTextFieldImpl(
        enabled = enabled,
        readOnly = readOnly,
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = singleLine,
        textStyle = textStyle,
        placeholder = placeholder,
        leading = leadingIcon,
        trailing = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        maxLines = maxLines,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors
    )
}

@Composable
internal fun TextFieldLayout(
    modifier: Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    enabled: Boolean,
    readOnly: Boolean,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    textStyle: TextStyle,
    singleLine: Boolean,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation,
    interactionSource: MutableInteractionSource,
    decoratedPlaceholder: @Composable ((Modifier) -> Unit)?,
    leading: @Composable (() -> Unit)?,
    trailing: @Composable (() -> Unit)?,
    leadingColor: Color,
    trailingColor: Color,
    indicatorWidth: Dp,
    indicatorColor: Color,
    backgroundColor: Color,
    cursorColor: Color,
    shape: Shape
) {

    BasicTextField(
        value = value,
        modifier = modifier
            .defaultMinSize(
                minWidth = TextFieldDefaults.MinWidth,
                minHeight = TextFieldDefaults.MinHeight
            )
            .background(color = backgroundColor, shape = shape)
            .drawIndicatorLine(lineWidth = indicatorWidth, color = indicatorColor),
        onValueChange = onValueChange,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        cursorBrush = SolidColor(cursorColor),
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        maxLines = maxLines,
        decorationBox = @Composable { coreTextField ->
            // places leading icon, text field with placeholder, trailing icon
            IconsWithTextFieldLayout(
                textField = coreTextField,
                placeholder = decoratedPlaceholder,
                leading = leading,
                trailing = trailing,
                singleLine = singleLine,
                leadingColor = leadingColor,
                trailingColor = trailingColor,
            )
        }
    )
}

@Composable
private fun IconsWithTextFieldLayout(
    textField: @Composable () -> Unit,
    placeholder: @Composable ((Modifier) -> Unit)?,
    leading: @Composable (() -> Unit)?,
    trailing: @Composable (() -> Unit)?,
    singleLine: Boolean,
    leadingColor: Color,
    trailingColor: Color,
) {
    val measurePolicy = remember(singleLine) { TextFieldMeasurePolicy(singleLine) }
    Layout(
        content = {
            if (leading != null) {
                Box(
                    modifier = Modifier
                        .layoutId(LeadingId)
                        .then(IconDefaultSizeModifier),
                    contentAlignment = Alignment.Center
                ) {
                    Decoration(
                        contentColor = leadingColor,
                        content = leading
                    )
                }
            }
            if (trailing != null) {
                Box(
                    modifier = Modifier
                        .layoutId(TrailingId)
                        .then(IconDefaultSizeModifier),
                    contentAlignment = Alignment.Center
                ) {
                    Decoration(
                        contentColor = trailingColor,
                        content = trailing
                    )
                }
            }

            val paddingToIcon = TextFieldPadding - HorizontalIconPadding
            val padding = Modifier.padding(
                start = if (leading != null) paddingToIcon else TextFieldPadding,
                end = if (trailing != null) paddingToIcon else TextFieldPadding
            )
            if (placeholder != null) {
                placeholder(
                    Modifier
                        .layoutId(PlaceholderId)
                        .then(padding))
            }

            Box(
                modifier = Modifier
                    .layoutId(TextFieldId)
                    .then(padding),
                propagateMinConstraints = true,
            ) {
                textField()
            }
        },
        measurePolicy = measurePolicy
    )
}

private class TextFieldMeasurePolicy(private val singleLine: Boolean) : MeasurePolicy {
    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints
    ): MeasureResult {
        val topBottomPadding = TextFieldPadding.roundToPx()
        var occupiedSpaceHorizontally = 0

        // measure leading icon
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val leadingPlaceable = measurables.find { it.layoutId == LeadingId }
            ?.measure(looseConstraints)
        occupiedSpaceHorizontally += widthOrZero(
            leadingPlaceable
        )

        // measure trailing icon
        val trailingPlaceable = measurables.find { it.layoutId == TrailingId }
            ?.measure(looseConstraints.offset(horizontal = -occupiedSpaceHorizontally))
        occupiedSpaceHorizontally += widthOrZero(
            trailingPlaceable
        )

        // measure input field
        val textFieldConstraints = constraints
            .copy(minHeight = 0)
            .offset(
                vertical = -topBottomPadding,
                horizontal = -occupiedSpaceHorizontally
            )
        val textFieldPlaceable = measurables
            .first { it.layoutId == TextFieldId }
            .measure(textFieldConstraints)

        // measure placeholder
        val placeholderConstraints = textFieldConstraints.copy(minWidth = 0)
        val placeholderPlaceable = measurables.find { it.layoutId == PlaceholderId }
            ?.measure(placeholderConstraints)

        val width = calculateWidth(
            widthOrZero(leadingPlaceable),
            widthOrZero(trailingPlaceable),
            textFieldPlaceable.width,
            widthOrZero(placeholderPlaceable),
            constraints
        )
        val height = calculateHeight(
            textFieldPlaceable.height,
            heightOrZero(leadingPlaceable),
            heightOrZero(trailingPlaceable),
            heightOrZero(placeholderPlaceable),
            constraints,
            density
        )

        return layout(width, height) {
            placeWithoutLabel(
                width,
                height,
                textFieldPlaceable,
                placeholderPlaceable,
                leadingPlaceable,
                trailingPlaceable,
                singleLine,
                density
            )
        }
    }

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(
        measurables: List<IntrinsicMeasurable>,
        width: Int
    ): Int {
        return intrinsicHeight(measurables, width) { intrinsicMeasurable, w ->
            intrinsicMeasurable.maxIntrinsicHeight(w)
        }
    }

    override fun IntrinsicMeasureScope.minIntrinsicHeight(
        measurables: List<IntrinsicMeasurable>,
        width: Int
    ): Int {
        return intrinsicHeight(measurables, width) { intrinsicMeasurable, w ->
            intrinsicMeasurable.minIntrinsicHeight(w)
        }
    }

    override fun IntrinsicMeasureScope.maxIntrinsicWidth(
        measurables: List<IntrinsicMeasurable>,
        height: Int
    ): Int {
        return intrinsicWidth(measurables, height) { intrinsicMeasurable, h ->
            intrinsicMeasurable.maxIntrinsicWidth(h)
        }
    }

    override fun IntrinsicMeasureScope.minIntrinsicWidth(
        measurables: List<IntrinsicMeasurable>,
        height: Int
    ): Int {
        return intrinsicWidth(measurables, height) { intrinsicMeasurable, h ->
            intrinsicMeasurable.minIntrinsicWidth(h)
        }
    }

    private fun intrinsicWidth(
        measurables: List<IntrinsicMeasurable>,
        height: Int,
        intrinsicMeasurer: (IntrinsicMeasurable, Int) -> Int
    ): Int {
        val textFieldWidth =
            intrinsicMeasurer(
                measurables.first { it.layoutId == TextFieldId },
                height
            )
        val trailingWidth =
            measurables.find { it.layoutId == TrailingId }?.let {
                intrinsicMeasurer(it, height)
            } ?: 0
        val leadingWidth =
            measurables.find { it.layoutId == LeadingId }?.let {
                intrinsicMeasurer(it, height)
            } ?: 0
        val placeholderWidth =
            measurables.find { it.layoutId == PlaceholderId }?.let {
                intrinsicMeasurer(it, height)
            } ?: 0
        return calculateWidth(
            leadingWidth = leadingWidth,
            trailingWidth = trailingWidth,
            textFieldWidth = textFieldWidth,
            placeholderWidth = placeholderWidth,
            constraints = ZeroConstraints
        )
    }

    private fun IntrinsicMeasureScope.intrinsicHeight(
        measurables: List<IntrinsicMeasurable>,
        width: Int,
        intrinsicMeasurer: (IntrinsicMeasurable, Int) -> Int
    ): Int {
        val textFieldHeight =
            intrinsicMeasurer(
                measurables.first { it.layoutId == TextFieldId },
                width
            )
        val trailingHeight =
            measurables.find { it.layoutId == TrailingId }?.let {
                intrinsicMeasurer(it, width)
            } ?: 0
        val leadingHeight =
            measurables.find { it.layoutId == LeadingId }?.let {
                intrinsicMeasurer(it, width)
            } ?: 0
        val placeholderHeight =
            measurables.find { it.layoutId == PlaceholderId }?.let {
                intrinsicMeasurer(it, width)
            } ?: 0
        return calculateHeight(
            textFieldHeight = textFieldHeight,
            leadingHeight = leadingHeight,
            trailingHeight = trailingHeight,
            placeholderHeight = placeholderHeight,
            constraints = ZeroConstraints,
            density = density
        )
    }
}

private fun calculateWidth(
    leadingWidth: Int,
    trailingWidth: Int,
    textFieldWidth: Int,
    placeholderWidth: Int,
    constraints: Constraints
): Int {
    val middleSection = maxOf(
        textFieldWidth,
        placeholderWidth
    )
    val wrappedWidth = leadingWidth + middleSection + trailingWidth
    return max(wrappedWidth, constraints.minWidth)
}

private fun calculateHeight(
    textFieldHeight: Int,
    leadingHeight: Int,
    trailingHeight: Int,
    placeholderHeight: Int,
    constraints: Constraints,
    density: Float
): Int {
    val topBottomPadding = TextFieldPadding.value * density

    val inputFieldHeight = max(textFieldHeight, placeholderHeight)
    val middleSectionHeight = topBottomPadding * 2 + inputFieldHeight

    return maxOf(
        middleSectionHeight.roundToInt(),
        max(leadingHeight, trailingHeight),
        constraints.minHeight
    )
}

private fun Placeable.PlacementScope.placeWithoutLabel(
    width: Int,
    height: Int,
    textPlaceable: Placeable,
    placeholderPlaceable: Placeable?,
    leadingPlaceable: Placeable?,
    trailingPlaceable: Placeable?,
    singleLine: Boolean,
    density: Float
) {
    val topBottomPadding = (TextFieldPadding.value * density).roundToInt()

    leadingPlaceable?.placeRelative(
        0,
        Alignment.CenterVertically.align(leadingPlaceable.height, height)
    )
    trailingPlaceable?.placeRelative(
        width - trailingPlaceable.width,
        Alignment.CenterVertically.align(trailingPlaceable.height, height)
    )

    // Single line text field without label places its input center vertically. Multiline text
    // field without label places its input at the top with padding
    val textVerticalPosition = if (singleLine) {
        Alignment.CenterVertically.align(textPlaceable.height, height)
    } else {
        topBottomPadding
    }
    textPlaceable.placeRelative(
        widthOrZero(leadingPlaceable),
        textVerticalPosition
    )

    // placeholder is placed similar to the text input above
    placeholderPlaceable?.let {
        val placeholderVerticalPosition = if (singleLine) {
            Alignment.CenterVertically.align(placeholderPlaceable.height, height)
        } else {
            topBottomPadding
        }
        it.placeRelative(
            widthOrZero(leadingPlaceable),
            placeholderVerticalPosition
        )
    }
}

internal fun Modifier.drawIndicatorLine(lineWidth: Dp, color: Color): Modifier {
    return drawBehind {
        val strokeWidth = lineWidth.value * density
        val y = size.height - strokeWidth / 2
        drawLine(
            color,
            Offset(0f, y),
            Offset(size.width, y),
            strokeWidth
        )
    }
}

private val IntrinsicMeasurable.layoutId: Any?
    get() = (parentData as? LayoutIdParentData)?.layoutId

private val ZeroConstraints = Constraints(0, 0, 0, 0)