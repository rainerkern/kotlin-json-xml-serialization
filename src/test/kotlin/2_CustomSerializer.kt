package at.rainerkern.kotlinjsonxmlserialization

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.decode
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.json.Json
import kotlinx.serialization.withName
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.util.UUID

private class CustomSerializerTest {

    @Test fun `Can serialize UUID DTO to Json`() {
        val orig = UuidJsonDto()
        with(Json.stringify(UuidJsonDto.serializer(), orig)) {
            Assertions.assertThat(this).isEqualTo("""{"id":"${orig.id}"}""".trimMargin())
        }
    }

    @Test fun `Can deserialize UUID DTO to Json`() {
        val json = """{"id":"dba7d35a-f255-42fe-965f-8a4cc94edab8"}"""
        val res = Json.parse(UuidJsonDto.serializer(), json)
        Assertions.assertThat(res.id).isEqualTo(UUID.fromString("dba7d35a-f255-42fe-965f-8a4cc94edab8"))
    }
}


@Serializable
private data class UuidJsonDto(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID()
)

@Serializer(forClass = UUID::class)
private object UUIDSerializer : KSerializer<UUID> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("UUID")

    override fun serialize(output: Encoder, obj: UUID) {
        output.encodeString(obj.toString())
    }

    @ImplicitReflectionSerializer
    override fun deserialize(input: Decoder): UUID {
        return UUID.fromString(input.decode())
    }
}
