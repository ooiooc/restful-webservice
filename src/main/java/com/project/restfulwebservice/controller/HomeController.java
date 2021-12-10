package com.project.restfulwebservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class HomeController {

    @Autowired // 어노테이션 의존성 주입
    private MessageSource messageSource;

    // GET
    // Hello-world (endpoint)
    @GetMapping(path = "/hello-world")
    public String helloWorld(){
        return "Hello World!";
    }

    // 자바 빈 형태로 반환하게 되면 json 형태로 반환
    // alt + enter -> 해당 Bean 클래스 자동 생성
    @GetMapping(path = "/hello-world-bean/path-variable/{name}")
    public HelloWorldBean helloWorldBean(@PathVariable String name){
        // 서로 다른 값을 사용할 경우 value 지정해주기 (@PathVariable(value = ""))
        return new HelloWorldBean(String.format("Hello World, %s 님", name));
    }

    @GetMapping(path = "/hello-world-internationalized")
    public String helloWorldInternationalized(
        @RequestHeader(name = "Accept-Language", required = false) Locale locale){
            return messageSource.getMessage("greeting.message", null, locale);
        }

}
