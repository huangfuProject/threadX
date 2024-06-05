package com.threadx.metrics.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @author huangfu
 * @date 2024/4/28 15:23
 */
@Controller
public class IndexController {
    @GetMapping("/")
    public String redirectVue() {
        return "index.html";
    }
}