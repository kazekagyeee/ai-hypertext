package kazekagyee.hyperlink

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class HypertextService(
    private val keywordExtractor: KeywordExtractorService
) {

    /**
     * Превращает ключевые слова в тексте в гиперссылки.
     *
     * @param text исходный текст
     * @return HTML-строка с гиперссылками
     */
    fun processText(text: String): Mono<String> {
        if (text.isBlank()) return Mono.just("")

        return Mono.fromCallable {
            val keywords = keywordExtractor.extractKeywords(text)
            keywordExtractor.highlightKeywords(text, keywords)
        }
    }
}