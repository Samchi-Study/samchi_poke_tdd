package com.samchi.poke.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Serializable
@Parcelize
data class Pokemon(
    val name: String,
    @Serializable(with = UriSerializer::class)
    private val url: String
) : Parcelable {

    fun getImageUrl(): String {
        val index = url.split("/".toRegex()).dropLast(1).last()
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/" +
            "pokemon/other/official-artwork/$index.png"
    }
}

object UriSerializer : KSerializer<String> {
    override val descriptor = PrimitiveSerialDescriptor("Uri", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(URLEncoder.encode(value, StandardCharsets.UTF_8.name()))
    }

    override fun deserialize(decoder: Decoder): String {
        return URLDecoder.decode(decoder.decodeString(), StandardCharsets.UTF_8.name())
    }
}
