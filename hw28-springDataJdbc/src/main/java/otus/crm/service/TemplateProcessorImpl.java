package otus.crm.service;

import java.io.IOException;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Component
public class TemplateProcessorImpl implements TemplateProcessor {

    private final SpringTemplateEngine templateEngine;
    private static final String TEMPLATES_DIR = "/templates/";

    public TemplateProcessorImpl() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix(TEMPLATES_DIR); // "/templates/"
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false); // dev

        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);
        this.templateEngine = engine;
    }

    @Override
    public String getPage(String filename, Map<String, Object> data) throws IOException {
        Context context = new Context();
        context.setVariables(data);
        return templateEngine.process(filename, context);
    }
}
