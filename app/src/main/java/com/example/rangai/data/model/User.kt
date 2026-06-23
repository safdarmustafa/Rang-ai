package com.example.rangai.data.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull

/**
 * Supabase may return phone_number as a JSON number (bigint column)
 * or as a JSON string (text column). This serializer accepts both.
 */
object FlexiblePhoneSerializer : KSerializer<String> {

    override val descriptor =
        PrimitiveSerialDescriptor("phone_number", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }

    override fun deserialize(decoder: Decoder): String {
        val jsonDecoder = decoder as? JsonDecoder
        if (jsonDecoder != null) {
            return when (val element = jsonDecoder.decodeJsonElement()) {
                is JsonPrimitive -> {
                    element.longOrNull?.toString()
                        ?: element.intOrNull?.toString()
                        ?: element.content
                }
                else -> element.toString()
            }
        }
        return decoder.decodeString()
    }
}

@Serializable
data class User(
    @Serializable(with = FlexiblePhoneSerializer::class)
    val phone_number: String,
    val name: String? = null,
    val age: Int? = null
)
