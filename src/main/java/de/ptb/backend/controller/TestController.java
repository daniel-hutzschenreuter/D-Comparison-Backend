package de.ptb.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
public class TestController {
    @GetMapping("/api/sayHello")
    public String sayHelloWorld(){
        return "Hello World!";
    }

}
