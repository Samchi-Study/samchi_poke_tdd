package com.samchi.feature.sanghyeong.ui.extension

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow

@Composable
fun LazyListState.OnBottomReached(
    bufferSize: Int = 3,
    onLoadMore: () -> Unit,
) {
    require(bufferSize >= 0) { "buffer should be positive" }

    val itemsCount = layoutInfo.totalItemsCount
    val shouldLoadMore = remember(itemsCount) {
        derivedStateOf {
            if (itemsCount == 0) return@derivedStateOf false
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf false

            lastVisibleItem.index >= layoutInfo.totalItemsCount - 1 - bufferSize
        }
    }

    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }.collect { if (it) onLoadMore() }
    }
}