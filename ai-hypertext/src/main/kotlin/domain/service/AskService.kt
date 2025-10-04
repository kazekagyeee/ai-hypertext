package kazekagyee.domain.service

import kazekagyee.hyperlink.HypertextService
import kazekagyee.ollama.OllamaService
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicReference

@Service
class AskService(
    private val ollamaService: OllamaService,
    private val hypertextService: HypertextService,
) {
    private var lastAnswer = AtomicReference("")

    fun askAboutWord(word: String): String {
        val last = lastAnswer.get()
        val prompt = "Расскажи подробнее про это слово ${word}, не отходя от текущей темы: $last"
        return answer(prompt)
    }

    fun answer(prompt: String): String {
        val answer = ollamaService.ask(prompt)
        lastAnswer.set(answer)
        return hypertextService.wrapText(answer)
    }
}