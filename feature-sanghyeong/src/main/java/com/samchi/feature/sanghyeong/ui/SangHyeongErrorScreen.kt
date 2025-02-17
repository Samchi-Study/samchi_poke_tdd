package com.samchi.feature.sanghyeong.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
internal fun SangHyeongErrorScreen(
    throwable: Throwable,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = throwable.message.toString())

        Spacer(modifier = Modifier.height(height = 20.dp))

        Box(
            modifier = Modifier
                .background(color = Color.DarkGray, shape = RoundedCornerShape(size = 10.dp))
                .clickable { onRetry.invoke() },
        ) {
            Text(
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 10.dp,
                ),
                text = "Retry",
            )
        }
    }
}

@Preview
@Composable
private fun SangHyeongErrorScreenPreview() {
    SangHyeongErrorScreen(throwable = Throwable(message = "Error Test Error Test"), onRetry = { })
}