package com.example.hzh.ktmvvm.compose.common

import android.graphics.Color
import android.text.TextUtils
import android.widget.TextView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.Card
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import com.example.hzh.ktmvvm.R

/**
 * Create by hzh on 2021/12/8
 */
@Composable
fun HtmlText(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Int = Color.BLACK,
    textSize: Float = 15f,
    maxLines: Int = Int.MAX_VALUE,
    ellipsize: TextUtils.TruncateAt = TextUtils.TruncateAt.END
) {
    val htmlDescription = remember(key1 = text) {
        HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    AndroidView(
        factory = { context ->
            TextView(context).apply {
                setTextColor(textColor)
                this.textSize = textSize
                this.ellipsize = ellipsize
                this.maxLines = maxLines
            }
        },
        modifier = modifier,
        update = {
            it.text = htmlDescription
        }
    )
}

@Suppress("ModifierParameter")
@Composable
fun BorderText(
    text: String,
    outerModifier: Modifier = Modifier,
    innerModifier: Modifier = Modifier,
    textColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
    border: BorderStroke? = null,
    shape: Shape? = null
) {
    BorderText(
        AnnotatedString(text),
        outerModifier,
        innerModifier,
        textColor,
        fontSize,
        fontStyle,
        fontWeight,
        fontFamily,
        letterSpacing,
        textDecoration,
        textAlign,
        lineHeight,
        overflow,
        softWrap,
        maxLines,
        emptyMap(),
        onTextLayout,
        style,
        border,
        shape
    )
}

@Suppress("ModifierParameter")
@Composable
fun BorderText(
    text: AnnotatedString,
    outerModifier: Modifier = Modifier,
    innerModifier: Modifier = Modifier,
    textColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
    border: BorderStroke? = null,
    shape: Shape? = null
) {
    Card(
        modifier = outerModifier,
        border = border,
        shape = shape ?: RoundedCornerShape(0.dp),
    ) {
        val modifier = Modifier
            .background(androidx.compose.ui.graphics.Color.Transparent)
            .then(innerModifier)
        Text(
            text,
            modifier,
            textColor,
            fontSize,
            fontStyle,
            fontWeight,
            fontFamily,
            letterSpacing,
            textDecoration,
            textAlign,
            lineHeight,
            overflow,
            softWrap,
            maxLines,
            inlineContent,
            onTextLayout,
            style
        )
    }
}

@Preview
@Composable
fun PreviewBorderText() {
    Surface {
        val appColor = colorResource(R.color.appColor)

        BorderText(
            text = "公众号",
            outerModifier = Modifier
                .padding(end = 10.dp)
                .wrapContentSize(),
            innerModifier = Modifier.padding(5.dp),
            textColor = appColor,
            fontSize = 12.sp,
            border = BorderStroke(1.dp, appColor),
            shape = RoundedCornerShape(6.dp)
        )
    }
}