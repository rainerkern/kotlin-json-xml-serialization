package at.rainerkern.kotlinjsonxmlserialization

import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private class  SerializationTest {

    @Test fun `Can serialize DTO to Json`() {
        val orig = SimpleDto(
            id = 42,
            name = "test_name",
            factor = 99.2f
        )
        with(Json.stringify(SimpleDto.serializer(), orig)) {
            assertThat(this).isEqualTo("""{"id":42,"name":"test_name","factor":99.2}""".trimMargin())
        }
    }

    @Test fun `Can deserialize Json to DTO`() {
        val json = """{"id":42,"name":"test_name","factor":99.2}"""
        with(Json.parse(SimpleDto.serializer(), json)) {
            assertThat(id).isEqualTo(42)
            assertThat(name).isEqualTo("test_name")
            assertThat(factor).isEqualTo(99.2f)
        }
    }

    @Test fun `Can deserialize Json to DTO with unknown fields`() {
        val json = """{"id":42,"name":"test_name","factor":99.2, "newField":"some_new_value"}"""
        with(Json(strictMode = false).parse(SimpleDto2.serializer(), json)) {
            assertThat(id).isEqualTo(42)
            assertThat(name).isEqualTo("test_name")
            assertThat(factor).isEqualTo(99.2f)
        }
    }

    @Test fun `Can deserialize Json to DTO with missing fields`() {
        val json = """{"id":42,"name":"test_name","factor":99.2, "newField":"some_new_value"}"""
        with(Json(strictMode = false).parse(SimpleDto3.serializer(), json)) {
            assertThat(id).isEqualTo(42)
            assertThat(name).isEqualTo("test_name")
            assertThat(factor).isEqualTo(99.2f)
            assertThat(iAmMissingFromTheJsonString).isNull()
        }
    }
}

@Serializable
private data class SimpleDto(
    val id: Int = -1,
    val name: String = "",
    val factor: Float = 0.0f
)

@Serializable
private data class SimpleDto2(
    val id: Int = -1,
    val name: String = "",
    val factor: Float = 0.0f
)

@Serializable
private data class SimpleDto3(
    val id: Int = -1,
    val name: String = "",
    val factor: Float = 0.0f,
    @Optional
    val iAmMissingFromTheJsonString: Any? = null
)


