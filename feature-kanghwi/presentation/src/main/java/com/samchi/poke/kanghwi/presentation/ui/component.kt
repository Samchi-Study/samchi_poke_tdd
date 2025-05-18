package com.samchi.poke.kanghwi.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SnapshotMutationPolicy
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.palette.graphics.Palette
import coil3.SingletonImageLoader
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.allowHardware


@Composable
internal fun KanghwiAsyncImage(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    placeholder: Painter? = null,
    error: Painter? = null,
    fallback: Painter? = error,
    onLoading: ((AsyncImagePainter.State.Loading) -> Unit)? = null,
    onSuccess: ((AsyncImagePainter.State.Success) -> Unit)? = null,
    onError: ((AsyncImagePainter.State.Error) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality,
    clipToBounds: Boolean = true,
) = AsyncImage(
    model = ImageRequest.Builder(LocalContext.current)
        .data(url)
        .allowHardware(false)
        .build(),
    contentDescription = contentDescription,
    imageLoader = SingletonImageLoader.get(LocalPlatformContext.current),
    modifier = modifier,
    placeholder = placeholder,
    error = error,
    fallback = fallback,
    onLoading = onLoading,
    onSuccess = onSuccess,
    onError = onError,
    alignment = alignment,
    contentScale = contentScale,
    alpha = alpha,
    colorFilter = colorFilter,
    filterQuality = filterQuality,
    clipToBounds = clipToBounds,
)

@Composable
internal fun rememberPaletteState(
    value: Palette? = null,
    policy: SnapshotMutationPolicy<Palette?> = structuralEqualityPolicy(),
): MutableState<Palette?> = remember(key1 = value) {
    mutableStateOf(value = value, policy = policy)
}

@Composable
internal fun Palette?.getTextColor(): State<Color> {
    val defaultColor = KanghwiPokeTddTheme.textColor

    return remember(this) {
        derivedStateOf {
            val rgb = this?.darkMutedSwatch?.rgb

            if (rgb != null) {
                Color(rgb)
            } else {
                defaultColor
            }
        }
    }
}


@Composable
internal fun Palette?.getBackgroundColor(): State<Color> {
    val defaultColor = KanghwiPokeTddTheme.backgroundColor

    return remember(this) {
        derivedStateOf {
            val rgb = this?.dominantSwatch?.rgb

            if (rgb != null) {
                Color(rgb)
            } else {
                defaultColor
            }
        }
    }
}
