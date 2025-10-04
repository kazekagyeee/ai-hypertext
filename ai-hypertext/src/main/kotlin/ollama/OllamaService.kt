package kazekagyee.ollama

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

data class OllamaRequest(
    val model: String,
    val prompt: String,
    val system: String? = null
)

data class OllamaResponse(
    val model: String,
    val created_at: String,
    val response: String?
)

@Service
class OllamaService(
    @Value("\${app.ollama.host:http://localhost:11434}")
    private val ollamaHost: String,

    @Value("\${app.ollama.model:qwen3:8b}")
    private val model: String,

    @Value("\${app.ollama.system-prompt:Ты умный ассистент, который отвечает кратко и говорит только по-русски}")
    private val systemPrompt: String,
) {
    private val client = WebClient.create(ollamaHost)

    fun ask(prompt: String): Mono<String?> {
        val request = OllamaRequest(
            model = model,
            prompt = prompt,
            system = systemPrompt,
        )

        return client.post()
            .uri("/api/generate")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(OllamaResponse::class.java)
            .mapNotNull { it.response }
            .defaultIfEmpty("Не удалось получить ответ")
    }
}