package kazekagyee.ollama

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

data class OllamaRequest(
    val model: String,
    val prompt: String,
    val stream: Boolean = false,
)

data class OllamaResponse(
    val model: String,
    val created_at: String,
    val response: String?
)

@Service
class OllamaService(
    @Value("\${app.ollama.host:http://host.docker.internal:11434}")
    private val ollamaHost: String,

    @Value("\${app.ollama.model:qwen3:8b}")
    private val model: String,

    @Value("\${app.ollama.system-prompt:" +
            "Ты умный ассистент, который отвечает кратко и говорит только по-русски, ответь на сообщение: }")
    private val systemPrompt: String,
) {
    private val client = WebClient.builder()
        .baseUrl(ollamaHost)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build()

    fun ask(prompt: String): Mono<String> {
        val request = OllamaRequest(
            model = model,
            prompt = systemPrompt + prompt,
        )

        return client.post()
            .uri("/api/generate")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(OllamaResponse::class.java)
            .mapNotNull { it.response }
            .map { extractAfterThink(it) }
            .defaultIfEmpty("Не удалось получить ответ")
    }

    private fun extractAfterThink(text: String?): String {
        if (text == null) {
            return ""
        }
        val regex = Regex(
            """</think>\s*(.*)""",
            setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.IGNORE_CASE)
        )
        val match = regex.find(text)
        return match?.groupValues?.get(1)?.trim() ?: text.trim()
    }
}