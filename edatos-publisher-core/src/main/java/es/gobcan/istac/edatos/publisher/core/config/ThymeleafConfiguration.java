package es.gobcan.istac.edatos.publisher.core.config;

import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
@EnableConfigurationProperties(ThymeleafProperties.class)
@AutoConfigureAfter(ThymeleafAutoConfiguration.class)

public class ThymeleafConfiguration {

    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(ThymeleafConfiguration.class);

    @Bean
    public SpringTemplateEngine templateEngine(ThymeleafProperties properties) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(emailTemplateResolver(properties));
        templateEngine.addTemplateResolver(jsTemplateResolver(properties));
        templateEngine.addTemplateResolver(htmlTemplateResolver(properties));
        if(!properties.isCache()) {
            templateEngine.setCacheManager(null);
        }
        return templateEngine;
    }

    @Bean
    @Qualifier("emailTemplateResolver")
    @Description("Thymeleaf template resolver serving HTML 5 emails")
    public ClassLoaderTemplateResolver emailTemplateResolver(ThymeleafProperties properties) {
        ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver();
        emailTemplateResolver.setOrder(1);
        emailTemplateResolver.setPrefix("mails/");
        emailTemplateResolver.setSuffix(".html");
        emailTemplateResolver.setTemplateMode(TemplateMode.HTML);
        emailTemplateResolver.setCheckExistence(true);
        emailTemplateResolver.setCharacterEncoding(CharEncoding.UTF_8);
        emailTemplateResolver.setCacheable(properties.isCache());
        return emailTemplateResolver;
    }

    @Bean
    @Qualifier("jsTemplateResolver")
    @Description("Thymeleaf template resolver serving JS")
    public ClassLoaderTemplateResolver jsTemplateResolver(ThymeleafProperties properties) {
        ClassLoaderTemplateResolver jsTemplateResolver = new ClassLoaderTemplateResolver();
        jsTemplateResolver.setOrder(2);
        jsTemplateResolver.setPrefix("js/");
        jsTemplateResolver.setSuffix(".js");
        jsTemplateResolver.setTemplateMode(TemplateMode.JAVASCRIPT);
        jsTemplateResolver.setCheckExistence(true);
        jsTemplateResolver.setCharacterEncoding(CharEncoding.UTF_8);
        jsTemplateResolver.setCacheable(properties.isCache());
        return jsTemplateResolver;
    }

    @Bean
    @Qualifier("htmlTemplateResolver")
    @Description("Thymeleaf template resolver serving HTML")
    public SpringResourceTemplateResolver  htmlTemplateResolver(ThymeleafProperties properties) {
        SpringResourceTemplateResolver  htmlTemplateResolver = new SpringResourceTemplateResolver ();
        htmlTemplateResolver.setOrder(3);
        htmlTemplateResolver.setPrefix("/");
        htmlTemplateResolver.setSuffix(".html");
        htmlTemplateResolver.setTemplateMode(TemplateMode.HTML);
        htmlTemplateResolver.setCharacterEncoding(CharEncoding.UTF_8);
        htmlTemplateResolver.setCacheable(properties.isCache());
        return htmlTemplateResolver;
    }
}
