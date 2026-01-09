package com.monolux.resource.controllers;

import com.monolux.common.constants.http.HttpHeaderNames;
import com.monolux.utils.IpV4Util;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public abstract class BaseController {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    public static final String API_PREFIX = "/api/v1";

    // endregion

    // region ▒▒▒▒▒ Member Variables ▒▒▒▒▒

    protected final ModelMapper modelMapper;

    // endregion

    // region ▒▒▒▒▒ Constructors ▒▒▒▒▒

    protected BaseController() {
        this.modelMapper = new ModelMapper();
    }

    // endregion

    // region ▒▒▒▒▒ Methods ▒▒▒▒▒

    protected String getClientIp() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String result = IpV4Util.isValidIpAddress(IpV4Util.ipWithoutPort(req.getHeader(HttpHeaderNames.X_FORWARDED_FOR))) ?
                IpV4Util.ipWithoutPort(req.getHeader(HttpHeaderNames.X_FORWARDED_FOR)) : null;

        if (result == null && IpV4Util.isValidIpAddress(IpV4Util.ipWithoutPort(req.getHeader(HttpHeaderNames.PROXY_CLIENT_IP)))) {
            result = IpV4Util.ipWithoutPort(req.getHeader(HttpHeaderNames.PROXY_CLIENT_IP));
        }

        if (result == null && IpV4Util.isValidIpAddress(IpV4Util.ipWithoutPort(req.getHeader(HttpHeaderNames.WL_PROXY_CLIENT_IP)))) {
            result = IpV4Util.ipWithoutPort(req.getHeader(HttpHeaderNames.WL_PROXY_CLIENT_IP));
        }

        if (result == null && IpV4Util.isValidIpAddress(IpV4Util.ipWithoutPort(req.getHeader(HttpHeaderNames.HTTP_CLIENT_IP)))) {
            result = IpV4Util.ipWithoutPort(req.getHeader(HttpHeaderNames.HTTP_CLIENT_IP));
        }

        if (result == null && IpV4Util.isValidIpAddress(IpV4Util.ipWithoutPort(req.getHeader(HttpHeaderNames.HTTP_X_FORWARDED_FOR)))) {
            result = IpV4Util.ipWithoutPort(req.getHeader(HttpHeaderNames.HTTP_X_FORWARDED_FOR));
        }

        if (result == null && IpV4Util.isValidIpAddress(IpV4Util.ipWithoutPort(req.getRemoteAddr()))) {
            result = IpV4Util.ipWithoutPort(req.getRemoteAddr());
        }

        if (!IpV4Util.isValidIpAddress(result)) {
            result = "127.0.0.1";
        }

        return result;
    }

    // endregion
}