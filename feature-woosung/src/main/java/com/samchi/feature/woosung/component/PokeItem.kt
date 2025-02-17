package com.samchi.feature.woosung.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.samchi.feature.woosung.R
import com.samchi.feature.woosung.screen.list.PokemonTest
import com.samchi.poke.model.Pokemon

@Composable
internal fun PokeItem(
    modifier: Modifier = Modifier,
    pokemonImage: String = "",
    content: @Composable ColumnScope.() -> Unit = {}
) {
    Card(modifier) {
        Column {
            if (LocalInspectionMode.current && pokemonImage.isEmpty()) {
                Image(painter = painterResource(R.drawable.pizzon), contentDescription = null)
            } else {
                AsyncImage(
                    model = pokemonImage,
                    contentDescription = null,
                )
            }
            content()
        }
    }
}


@Preview
@Composable
private fun PokeItemPreview() {
    PokeItem()
}
