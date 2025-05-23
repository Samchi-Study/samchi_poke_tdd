package com.samchi.feature.sanghyeong.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.samchi.feature.sanghyeong.R
import com.samchi.feature.sanghyeong.model.SangHyeongPokemon

@Composable
internal fun PokemonItem(
    pokemon: SangHyeongPokemon,
    onFavoriteClick: (SangHyeongPokemon) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.LightGray, shape = RoundedCornerShape(size = 20.dp))
            .padding(
                vertical = 25.dp,
                horizontal = 40.dp,
            ),
        horizontalArrangement = Arrangement.spacedBy(space = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            modifier = Modifier.size(100.dp),
            model = pokemon.getImageUrl(),
            contentDescription = null
        )

        Text(
            modifier = Modifier.weight(1f),
            text = pokemon.name,
            fontSize = 20.sp,
        )

        Image(
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    onFavoriteClick.invoke(pokemon)
                },
            painter = painterResource(
                id = if (pokemon.isFavorite) {
                    R.drawable.ic_favorite_filled
                } else {
                    R.drawable.ic_favorite_border
                }
            ),
            contentDescription = null,
        )
    }
}

@Preview
@Composable
private fun PokemonItemPreview() {
    PokemonItem(
        pokemon = SangHyeongPokemon(
            name = "test",
            url = "",
            isFavorite = false,
        ),
        onFavoriteClick = { },
    )
}