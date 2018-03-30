package com.zgiot.app.server.module.sfsystems.controller;

import com.zgiot.common.constants.GlobalConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = GlobalConstants.API  + GlobalConstants.API_VERSION + "/sfsystems")
public class SfSystemController {
}
