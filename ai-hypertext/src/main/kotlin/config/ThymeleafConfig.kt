package kazekagyee.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.thymeleaf.spring6.SpringWebFluxTemplateEngine
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver

@Configuration
class ThymeleafConfig {

    @Bean
    fun templateResolver(): SpringResourceTemplateResolver {
        val resolver = SpringResourceTemplateResolver()
        resolver.prefix = "classpath:/templates/"
        resolver.suffix = ".html"
        resolver.characterEncoding = "UTF-8"
        resolver.isCacheable = false
        return resolver
    }

    @Bean
    fun templateEngine(templateResolver: SpringResourceTemplateResolver): SpringWebFluxTemplateEngine {
        val engine = SpringWebFluxTemplateEngine()
        engine.setTemplateResolver(templateResolver)
        return engine
    }
}
