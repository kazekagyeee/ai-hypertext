package kazekagyee.hyperlink

import org.springframework.stereotype.Service
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Service
class HypertextService {

    /**
     * Превращает каждое слово в тексте в гиперссылку.
     *
     * @param text исходный текст
     * @return HTML-строка с гиперссылками
     */
    fun wrapText(text: String): String {
        if (text.isBlank()) return ""

        val regex = Regex("(\\w+|[^\\w]+)")
        val parts = regex.findAll(text).map { it.value }.toList()

        return parts.joinToString("") { part ->
            if (part.matches(Regex("\\w+"))) {
                val escaped = URLEncoder.encode(part, StandardCharsets.UTF_8)
                """<span class="clickable-word" onclick="askAboutWord('$escaped')">$part</span>"""
            } else {
                part
            }
        }
    }
}