package com.example.hzh.ktmvvm.compose.common

import android.text.TextUtils
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.compose.util.fastClickable
import com.example.hzh.ktmvvm.data.TempData
import com.example.hzh.ktmvvm.data.bean.Article
import kotlinx.coroutines.flow.StateFlow

/**
 * Create by hzh on 2021/12/8
 */
data class ArticleClickCallback(

    val onItemClick: (Article) -> Unit,

    val onCollectClick: (Article) -> Unit
)

@Composable
fun ArticleList(
    modifier: Modifier = Modifier,
    articles: List<Article>,
    callbacks: ArticleClickCallback
) {
    LazyColumn(modifier = modifier) {
        items(
            items = articles,
            key = { article ->
                article.articleId
            },
        ) { article ->
            ArticleItem(
                article = article,
                onItemClick = { callbacks.onItemClick(article) },
                onCollectClick = { callbacks.onCollectClick(article) }
            )
        }
    }
}

@Composable
fun ArticleItem(
    article: Article,
    onItemClick: () -> Unit,
    onCollectClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .fastClickable { onItemClick() }
            .fillMaxWidth()
            .wrapContentHeight(),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(10.dp),
        elevation = 3.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            ArticleTopItem(article = article)

            Spacer(modifier = Modifier.height(10.dp))

            ArticleCenterItem(
                imageUrl = article.envelopePic,
                title = article.title,
                desc = article.desc
            )

            Spacer(modifier = Modifier.height(10.dp))

            ArticleBotItem(
                superChapterName = article.superChapterName,
                chapterName = article.chapterName,
                flowCollect = article.flowCollect,
                onCollectClick = onCollectClick
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewArticleItem() {
    Surface {
        ArticleItem(
            article = TempData.articles[0],
            onItemClick = { },
            onCollectClick = { }
        )
    }
}

@Composable
private fun ArticleTopItem(article: Article) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val colorRed = colorResource(R.color.color_ff3b30)
        val appColor = colorResource(R.color.appColor)

        if (article.type == 1) {
            ArticleTag(
                text = stringResource(R.string.set_to_top),
                textColor = colorRed,
                strokeColor = colorRed
            )
        }

        if (article.fresh) {
            ArticleTag(
                text = stringResource(R.string.fresh),
                textColor = colorRed,
                strokeColor = colorRed
            )
        }

        when (article.superChapterId) {
            440 -> ArticleTag(
                text = stringResource(R.string.QA),
                textColor = appColor,
                strokeColor = appColor
            )
            408 -> ArticleTag(
                text = stringResource(R.string.wechat_author),
                textColor = appColor,
                strokeColor = appColor
            )
            294 -> ArticleTag(
                text = stringResource(R.string.project),
                textColor = appColor,
                strokeColor = appColor
            )
        }

        val textGray = colorResource(R.color.color_999999)
        Text(
            text = article.author,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            color = textGray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = article.niceDate,
            fontSize = 13.sp,
            color = textGray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ArticleTag(
    text: String,
    textColor: Color,
    strokeColor: Color,
) {
    BorderText(
        text = text,
        outerModifier = Modifier
            .padding(end = 10.dp)
            .wrapContentSize(),
        innerModifier = Modifier.padding(5.dp),
        fontSize = 12.sp,
        textColor = textColor,
        border = BorderStroke(1.dp, strokeColor),
        shape = RoundedCornerShape(6.dp),
    )
}

@Suppress("DEPRECATION")
@Composable
private fun ArticleCenterItem(
    imageUrl: String,
    title: String,
    desc: String
) {
    val context = LocalContext.current
    Row {
        if (imageUrl.trim().isNotEmpty()) {
            ArticleImage(imageUrl = imageUrl)

            Spacer(modifier = Modifier.width(8.dp))
        }

        Column {
            HtmlText(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textColor = context.resources.getColor(R.color.color_000000),
                textSize = 16f,
                maxLines = 2,
                ellipsize = TextUtils.TruncateAt.END
            )

            if (desc.trim().isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))

                HtmlText(
                    text = desc,
                    modifier = Modifier.fillMaxWidth(),
                    textColor = context.resources.getColor(R.color.color_666666),
                    textSize = 15f,
                    maxLines = 2,
                    ellipsize = TextUtils.TruncateAt.END
                )
            }
        }
    }
}

@Composable
private fun ArticleImage(imageUrl: String) {
    val painter = rememberImagePainter(
        data = imageUrl,
        builder = { crossfade(true) }
    )

    Card(
        border = BorderStroke(
            color = colorResource(R.color.color_00aeff),
            width = 1.dp
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Image(
            painter = painter,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier.size(90.dp)
        )
    }
}

@Composable
private fun ArticleBotItem(
    superChapterName: String,
    chapterName: String,
    flowCollect: StateFlow<Boolean>,
    onCollectClick: () -> Unit
) {
    val isCollect by flowCollect.collectAsState()

    val category = if (superChapterName.trim().isEmpty() && chapterName.trim().isEmpty()) ""
    else stringResource(R.string.article_category, superChapterName, chapterName)

    val heartIcon = if (isCollect) R.drawable.ic_heart_pink
    else R.drawable.ic_heart_empty

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = category,
            fontSize = 14.sp,
            color = colorResource(R.color.color_666666),
            modifier = Modifier
                .padding(end = 10.dp)
                .weight(1f)
        )

        MyIconButton(
            modifier = Modifier.size(30.dp),
            onClick = onCollectClick
        ) {
            Icon(
                painter = painterResource(heartIcon),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}