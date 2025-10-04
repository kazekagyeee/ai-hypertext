package kazekagyee.domain.controller

import kazekagyee.domain.service.AskService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/ask")
class AskController(
    private val askService: AskService,
) {

    @PostMapping
    fun ask(@RequestBody body: Map<String, String>): String {
        val prompt = body["prompt"] ?: ""
        return askService.answer(prompt)
    }

    @GetMapping("/{word}")
    fun askAboutWord(@PathVariable word: String): String {
        return askService.askAboutWord(word)
    }
}