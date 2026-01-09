package com.monolux.authorization.controllers.opened;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = TurnstileController.BASE_PATH)
public class TurnstileController extends BaseOpenedController {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    public static final String BASE_PATH = "/turnstile";

    // endregion

    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    @Value("${cloud-flare.turnstile.validate-url}")
    private String cloudFlareTurnstileValidateUrl;

    @Value("${cloud-flare.turnstile.secret-key}")
    private String cloudFlareTurnstileSecretKey;

    // endregion

    // region ▒▒▒▒▒ Post Mapping ▒▒▒▒▒

    @PostMapping(value = "/validate")
    public ResponseEntity<String> ValidateTurnstileToken(final HttpServletRequest httpServletRequest,
                                                         @RequestHeader final HttpHeaders reqHeaders,
                                                         @RequestParam("token") final String token) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("secret", this.cloudFlareTurnstileSecretKey);
        requestBody.add("response", token);
        requestBody.add("remoteip", httpServletRequest.getRemoteAddr());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.postForEntity(this.cloudFlareTurnstileValidateUrl, requestEntity, String.class);
    }

    // endregion
}