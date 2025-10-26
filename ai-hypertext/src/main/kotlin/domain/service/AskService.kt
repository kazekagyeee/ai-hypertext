package kazekagyee.domain.service

import kazekagyee.hyperlink.HypertextService
import kazekagyee.ollama.OllamaService
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.concurrent.atomic.AtomicReference

@Service
class AskService(
    private val redis: ReactiveRedisTemplate<String, String>,
    private val ollamaService: OllamaService,
    private val hypertextService: HypertextService,
) {
    private var lastAnswer = AtomicReference("")

    /**
     * Сгенерировать ответ для окна пояснения
     */
    fun askAboutWord(word: String, context: String): Mono<String> {
        val key = "explain:$word:${context.hashCode()}"

        return redis.opsForValue().get(key)
            .flatMap { cached -> if (cached != null) Mono.just(cached) else Mono.empty() }
            .switchIfEmpty(
                Mono.defer {
                    val prompt = """Твой прошлый ответ: $context, не отходя от текущей темы расскажи про: $word"""
                    generateAnswer(prompt)
                        .flatMap { generated ->
                            redis.opsForValue().set(key, generated).thenReturn(generated)
                        }
                }
            )
    }

    /**
     * Ответ для чата
     */
    fun answer(prompt: String): Mono<String> {
        return generateAnswer(prompt)
            .flatMap { response -> hypertextService.processText(response) }
    }

    /**
     * Генерация ответа
     */
    fun generateAnswer(prompt: String): Mono<String> {
        return ollamaService.ask(prompt)
            .doOnNext { response -> lastAnswer.set(response) }
    }
}