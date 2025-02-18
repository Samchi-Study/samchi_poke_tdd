package com.samchi.feature.sanghyeong.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.samchi.poke.model.Pokemon

@Composable
internal fun PokemonItem(pokemon: Pokemon) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.LightGray, shape = RoundedCornerShape(size = 20.dp))
            .padding(all = 50.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            modifier = Modifier.size(150.dp),
            model = pokemon.getImageUrl(),
            contentDescription = null
        )

        Text(
            text = pokemon.name,
            fontWeight = FontWeight.W400,
            fontSize = 20.sp,
        )
    }
}

@Preview
@Composable
private fun PokemonItemPreview() {
    PokemonItem(pokemon = Pokemon(name = "test", url = ""))
}