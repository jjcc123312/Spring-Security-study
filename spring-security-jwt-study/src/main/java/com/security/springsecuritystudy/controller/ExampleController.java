package com.security.springsecuritystudy.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @author Jjcc
 * @version 1.0.0
 * @className ExampleController.java
 * @createTime 2019年11月18日 21:52:00
 */
@Api("security测试类")
@Slf4j
@RestController
public class ExampleController {

    @GetMapping("helloWorld")
    public List<String> helloWorld() {
        return Arrays.asList("Spring Security simple demo");
    }
}
