package com.monolux.resource.configs;

import com.monolux.common.constants.http.HttpHeaderNames;
import com.monolux.domain.enumerations.Scope;
import com.monolux.resource.controllers.opened.BaseOpenedController;
import com.monolux.resource.controllers.secured.BaseSecuredController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Profile({"local", "dev"})
@Configuration(proxyBeanMethods = false)
public class SwaggerConfig {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    private static final String API_VERSION = "1.0";

    protected static final String[] PERMIT_SWAGGER_URLS = {
            /* swagger v2 */
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            /* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    // endregion

    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    private final Environment environment;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    @Autowired
    SwaggerConfig(final Environment environment) {
        this.environment = environment;
    }

    // endregion

    // region ▒▒▒▒▒ Beans ▒▒▒▒▒

    @Bean
    public Docket swaggerOpenedAPI() {
        if (Arrays.stream(this.environment.getActiveProfiles())
                .anyMatch(profile -> profile.contains("local") || profile.contains("dev"))) {

            return new Docket(DocumentationType.OAS_30)
                    .groupName("Monolux Opened API")
                    .useDefaultResponseMessages(true) //기본 응답 메시지 표시 여부
                    .apiInfo(new ApiInfoBuilder()
                            .title("Monolux Opened API Document")
                            .description("Monolux Opened API Document")
                            .version(SwaggerConfig.API_VERSION)
                            .contact(this.contact())
                            .build())
                    .globalRequestParameters(List.of(new RequestParameterBuilder()
                            .in(ParameterType.HEADER)
                            .name(HttpHeaderNames.X_FORWARDED_FOR)
                            .description(HttpHeaderNames.X_FORWARDED_FOR)
                            .required(false)
                            .build()))
                    .select()
                    .apis(RequestHandlerSelectors.basePackage(BaseOpenedController.class.getPackageName()))
                    .paths(PathSelectors.any())
                    .build();
        } else {
            return null;
        }
    }

    @Bean
    public Docket swaggerSecuredAPI() {
        if (Arrays.stream(this.environment.getActiveProfiles())
                .anyMatch(profile -> profile.contains("local") || profile.contains("dev"))) {

            return new Docket(DocumentationType.OAS_30)
                    .groupName("Monolux Secured API")
                    .useDefaultResponseMessages(true) //기본 응답 메시지 표시 여부
                    .apiInfo(new ApiInfoBuilder()
                            .title("Monolux Secured API Document")
                            .description("Monolux Secured API Document")
                            .version(SwaggerConfig.API_VERSION)
                            .contact(this.contact())
                            .build())
                    .globalRequestParameters(List.of(new RequestParameterBuilder()
                            .in(ParameterType.HEADER)
                            .name(HttpHeaderNames.X_FORWARDED_FOR)
                            .description(HttpHeaderNames.X_FORWARDED_FOR)
                            .required(false)
                            .build()))
                    .select()
                    .apis(RequestHandlerSelectors.basePackage(BaseSecuredController.class.getPackageName()))
                    .paths(PathSelectors.any())
                    .build()
                    .securitySchemes(List.of(new ApiKey(HttpHeaders.AUTHORIZATION, HttpHeaders.AUTHORIZATION, "header")))
                    .securityContexts(List.of(SecurityContext.builder()
                            .securityReferences(List.of(new SecurityReference(HttpHeaders.AUTHORIZATION,
                                    Arrays.stream(Scope.values())
                                            .map(scope -> new AuthorizationScope(scope.getValue(), scope.getDesc()))
                                            .toArray(AuthorizationScope[]::new))))
                            .build()));
        } else {
            return null;
        }
    }

    // endregion

    // region ▒▒▒▒▒ Methods ▒▒▒▒▒

    private Contact contact() {
        return new Contact("Monolux", "https://github.com/monolux", "freecji@gmail.com");
    }

    // endregion
}