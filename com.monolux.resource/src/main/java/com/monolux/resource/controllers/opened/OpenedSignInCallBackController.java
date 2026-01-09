package com.monolux.resource.controllers.opened;

import com.monolux.common.constants.oauth2.OAuth2GrantType;
import com.monolux.common.constants.oauth2.OAuth2ParamNames;
import com.monolux.common.constants.oauth2.Oauth2ResponseType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

@ApiIgnore
@Api(tags = "Sing In CallBack API")
@RestController
@RequestMapping(value = OpenedSignInCallBackController.BASE_PATH)
public class OpenedSignInCallBackController extends BaseOpenedController {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    protected static final String BASE_PATH = BaseOpenedController.OPENED_API_PREFIX + "/signin/callback";

    // endregion

    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    @Value("${spring.security.oauth2.client.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.resource.token-uri}")
    private String tokenEndpointUri;

    // endregion

    // region ▒▒▒▒▒ Get Mapping ▒▒▒▒▒

    @ApiOperation(value = "Sign In CallBack")
    @GetMapping
    public ResponseEntity<String> getToken(final HttpServletRequest request,
                                           @RequestHeader final HttpHeaders reqHeaders,
                                           @RequestParam final String code) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add(OAuth2ParamNames.GRANT_TYPE, OAuth2GrantType.AUTHORIZATION_CODE);
        requestBody.add(Oauth2ResponseType.CODE, code);
        requestBody.add(OAuth2Utils.REDIRECT_URI, request.getRequestURL().toString());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setBasicAuth(this.clientId, this.clientId);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, httpHeaders);

        return new RestTemplate().postForEntity(this.tokenEndpointUri, requestEntity, String.class);
    }

    // endregion
}