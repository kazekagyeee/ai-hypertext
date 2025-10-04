package kazekagyee.domain.service

import kazekagyee.ollama.OllamaService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.concurrent.atomic.AtomicReference

@Service
class AskService(
    private val ollamaService: OllamaService,
) {
    private var lastAnswer = AtomicReference("")

    fun askAboutWord(word: String): Mono<String> {
        val last = lastAnswer.get()
        val prompt = "Расскажи подробнее про это слово ${word}, не отходя от текущей темы: $last"
        return answer(prompt)
    }

    fun answer(prompt: String): Mono<String> {
        return ollamaService.ask(prompt)
            .doOnNext { response -> lastAnswer.set(response) }
    }
}