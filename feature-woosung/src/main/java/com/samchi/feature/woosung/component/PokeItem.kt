package com.samchi.feature.woosung.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.samchi.feature.woosung.R
import com.samchi.poke.model.Pokemon

@Composable
internal fun PokeItem(
    modifier: Modifier = Modifier,
    pokemonImage: String = "",
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Card(modifier) {
        Box {
            Column {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (LocalInspectionMode.current && pokemonImage.isEmpty()) {
                        Image(
                            painter = painterResource(R.drawable.pizzon),
                            contentDescription = null
                        )
                    } else {
                        AsyncImage(
                            model = pokemonImage,
                            contentDescription = null,
                        )
                    }
                }
                content()
            }

            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (isFavorite) "좋아요 취소" else "좋아요",
                    tint = androidx.compose.ui.graphics.Color.Red
                )
            }
        }
    }
}


@Preview
@Composable
private fun PokeItemPreview() {
    PokeItem(isFavorite = false)
}

@Preview
@Composable
private fun PokeItemFavoritePreview() {
    PokeItem(isFavorite = true)
}
