import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ChatController {

    @GetMapping("/chat")
    fun chatPage(model: Model): String {
        model.addAttribute("title", "Hypertext Chat")
        return "chat"
    }
}
