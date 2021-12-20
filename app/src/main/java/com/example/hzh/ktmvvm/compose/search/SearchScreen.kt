package com.example.hzh.ktmvvm.compose.search

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.app.App
import com.example.hzh.ktmvvm.compose.common.ArticleClickCallback
import com.example.hzh.ktmvvm.compose.common.ArticleList
import com.example.hzh.ktmvvm.compose.common.MyIconButton
import com.example.hzh.ktmvvm.compose.common.RandomColorTextItem
import com.example.hzh.ktmvvm.compose.common.textfiled.MyTextField
import com.example.hzh.ktmvvm.compose.state.InputTextState
import com.example.hzh.ktmvvm.compose.state.rememberInputTextState
import com.example.hzh.ktmvvm.compose.util.BackPressHandler
import com.example.hzh.ktmvvm.data.TempData
import com.example.hzh.ktmvvm.data.bean.Article
import com.example.hzh.ktmvvm.data.bean.Website
import com.example.hzh.ktmvvm.util.InjectorUtils
import com.example.hzh.ktmvvm.viewmodel.ComposeSearchVM
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Create by hzh on 2021/12/7
 */
data class SearchTitleCallback(

    val onBackClick: () -> Unit,

    val onSearchClick: () -> Unit
)

@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    /**
     * first -> url; second -> title
     */
    onWebActivityOpen: (String, String) -> Unit,
    onLoginAction: () -> Unit
) {
    val searchVM: ComposeSearchVM = viewModel(factory = InjectorUtils.provideSearchVMFactory())

    val inputTextState = rememberInputTextState(initialText = "")

    val isResultPage by searchVM.isResultPage.collectAsState()
    val histories by searchVM.historyList.collectAsState()
    val hotKeyList by searchVM.hotKeyList.collectAsState()
    val commonWebList by searchVM.commonWebList.collectAsState()
    val searchResults by searchVM.articleList.collectAsState()

    val searchAction = { searchVM.search(inputTextState.text) }
    Surface {
        Column {
            SearchTitle(
                inputTextState = inputTextState,
                SearchTitleCallback(
                    onBackClick = onBackClick,
                    onSearchClick = searchAction
                )
            )

            if (isResultPage) {
                SearchResultPage(
                    searchResults = searchResults,
                    callbacks = ArticleClickCallback(
                        onItemClick = { onWebActivityOpen(it.link, it.title) },
                        onCollectClick = {
                            if (App.isLogin) searchVM.collectOrNot(it)
                            else onLoginAction()
                        }
                    )
                )
            } else {
                SearchHomePage(
                    histories = histories,
                    hotKeyList = hotKeyList,
                    commonWebList = commonWebList,
                    onClearHistory = { searchVM.cleanHistory() },
                    onHistoryClick = {
                        inputTextState.updateText(it)
                        searchAction()
                    },
                    onHotKeyClick = {
                        inputTextState.updateText(it.name)
                        searchAction()
                    },
                    onCommonWebClick = { onWebActivityOpen(it.link, it.name) }
                )
            }
        }
    }

    val context = LocalContext.current
    BackPressHandler {
        if (isResultPage) searchVM.updatePage(false)
        else (context as? Activity)?.run { finish() }
    }
}

@Composable
private fun SearchTitle(
    inputTextState: InputTextState,
    callbacks: SearchTitleCallback,
) {
    val titleColor = colorResource(R.color.appColor)
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(titleColor, false)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(titleColor)
            .height(49.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MyIconButton(
                modifier = Modifier.size(49.dp),
                onClick = callbacks.onBackClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = null,
                    tint = Color.White
                )
            }

            TitleTextField(
                inputTextState = inputTextState,
                onSearch = callbacks.onSearchClick
            )
        }
    }
}

@Preview
@Composable
fun PreviewSearchTitle() {
    Surface {
        val inputTextState = rememberInputTextState(initialText = "")
        SearchTitle(
            inputTextState = inputTextState,
            SearchTitleCallback(
                onBackClick = { },
                onSearchClick = { }
            )
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun TitleTextField(
    inputTextState: InputTextState,
    onSearch: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val textSize = 16.sp
    MyTextField(
        value = inputTextState.text,
        onValueChange = { inputTextState.updateText(it) },
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .fillMaxWidth(),
        textStyle = TextStyle(fontSize = textSize),
        placeholder = {
            Text(
                text = stringResource(R.string.search_something),
                color = colorResource(R.color.color_ececec),
                fontSize = textSize
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White,
            disabledIndicatorColor = Color.White
        ),
        singleLine = true,
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_search_white),
                contentDescription = null,
                tint = Color.White
            )
        },
        trailingIcon = {
            if (!inputTextState.isTextEmpty) {
                MyIconButton(onClick = { inputTextState.updateText("") }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close_white),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            onSearch()
            keyboardController?.hide()
        })
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SearchHomePage(
    histories: List<String>,
    hotKeyList: List<Website>,
    commonWebList: List<Website>,
    onClearHistory: () -> Unit,
    onHistoryClick: (String) -> Unit,
    onHotKeyClick: (Website) -> Unit,
    onCommonWebClick: (Website) -> Unit
) {
    LazyColumn(modifier = Modifier.background(Color.White)) {
        if (histories.isNotEmpty()) {
            stickyHeader {
                SearchHistoryHead(onClearHistory = onClearHistory)
            }
            item {
                SearchHomeFlowContent(
                    items = histories,
                    fillText = { it },
                    onItemClick = { onHistoryClick(it) }
                )
            }
        }

        if (hotKeyList.isNotEmpty()) {
            stickyHeader {
                SearchCommonHead(text = stringResource(R.string.hot_search))
            }
            item {
                SearchHomeFlowContent(
                    items = hotKeyList,
                    fillText = { it.name },
                    onItemClick = { onHotKeyClick(it) }
                )
            }
        }

        if (commonWebList.isNotEmpty()) {
            stickyHeader {
                SearchCommonHead(text = stringResource(R.string.common_website))
            }
            item {
                SearchHomeFlowContent(
                    items = commonWebList,
                    fillText = { it.name },
                    onItemClick = { onCommonWebClick(it) }
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewSearchHomePage() {
    Surface {
        SearchHomePage(
            histories = TempData.histories,
            hotKeyList = TempData.hotKeyList,
            commonWebList = TempData.commonWebList,
            onClearHistory = { },
            onHistoryClick = { },
            onHotKeyClick = { },
            onCommonWebClick = { }
        )
    }
}

@Composable
private fun SearchHistoryHead(
    onClearHistory: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.search_history),
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            color = colorResource(R.color.color_333333),
            fontWeight = FontWeight.Bold
        )

        MyIconButton(onClick = onClearHistory) {
            Icon(
                painter = painterResource(R.drawable.ic_delete_gray),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}

@Composable
private fun SearchCommonHead(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        fontSize = 16.sp,
        color = colorResource(R.color.color_333333),
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun <T> SearchHomeFlowContent(
    items: List<T>,
    fillText: (T) -> String,
    onItemClick: (T) -> Unit
) {
    FlowRow(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
    ) {
        items.forEach {
            RandomColorTextItem(
                text = fillText(it),
                onClick = { onItemClick(it) }
            )
        }
    }
}

@Composable
private fun SearchResultPage(
    searchResults: List<Article>,
    callbacks: ArticleClickCallback
) {
    ArticleList(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(vertical = 5.dp),
        articles = searchResults,
        callbacks = callbacks
    )
}

@Preview
@Composable
fun PreviewSearchResultPage() {
    Surface {
        SearchResultPage(
            searchResults = TempData.articles,
            callbacks = ArticleClickCallback(
                onItemClick = { },
                onCollectClick = { }
            )
        )
    }
}