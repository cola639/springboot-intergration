package org.spring.springboot.controller;

import com.demo.starter.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private DemoService demo;

    @Autowired
    private DemoService demo1;

    @GetMapping("/say")
    public String say() {
        return demo.say();
    }

    @GetMapping("/say1")
    public String say1() {
        return demo1.say();
    }
}
