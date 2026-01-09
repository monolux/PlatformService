package com.monolux.resource.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.util.Locale;

@Configuration(proxyBeanMethods = false)
public class LocaleConfig {
    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    @Value("${app.locale.language}")
    private String language;

    @Value("${app.locale.country}")
    private String country;

    // endregion

    // region ▒▒▒▒▒ Beans ▒▒▒▒▒

    @Bean
    public LocaleResolver localeResolver() {
        return new FixedLocaleResolver(new Locale(this.language, this.country));
    }

    // endregion
}