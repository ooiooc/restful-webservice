package com.project.restfulwebservice.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//lombok
@Data
@AllArgsConstructor // 모든 아규먼트를 가지는 생성자를 생성
@NoArgsConstructor// 디폴트 생성자 생성
public class HelloWorldBean {
    private String message;



}
