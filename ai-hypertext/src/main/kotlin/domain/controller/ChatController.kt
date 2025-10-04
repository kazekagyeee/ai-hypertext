package kazekagyee.domain.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import reactor.core.publisher.Mono


@Controller
class ChatController {

    @GetMapping("/chat")
    fun chatPage(): Mono<String> = Mono.just("chat")
}