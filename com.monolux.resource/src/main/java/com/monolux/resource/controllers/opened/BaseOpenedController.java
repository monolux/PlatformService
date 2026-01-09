package com.monolux.resource.controllers.opened;

import com.monolux.resource.controllers.BaseController;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class BaseOpenedController extends BaseController {
    // region ▒▒▒▒▒ Constants ▒▒▒▒▒

    public static final String OPENED_API_PREFIX = BaseController.API_PREFIX + "/opened";

    // endregion
}