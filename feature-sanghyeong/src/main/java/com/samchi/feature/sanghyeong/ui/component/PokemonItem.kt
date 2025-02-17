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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        // TODO - 포켓몬 이미지 자리. 정원님 PR 머지되면 coil 사용해서 로드하기.
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(color = Color.Yellow)
        )

        Text(
            text = pokemon.name
        )
    }
}

@Preview
@Composable
private fun PokemonItemPreview() {
    PokemonItem(pokemon = Pokemon(name = "test", url = ""))
}