package com.fantasy.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("/")
public class IndexController {

    @RequestMapping(path = "/status", method = RequestMethod.GET)
    public @ResponseBody JSONObject status() {
        JSONObject json = new JSONObject();
        return json;
    }

}
