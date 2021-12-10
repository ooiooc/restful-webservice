package com.project.restfulwebservice.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private UserDaoService userDaoService;

    public AdminController(UserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    // 전체 사용자 조회
    @GetMapping("/users")
    public MappingJacksonValue retriveAllUsers() {
        List<User> users = userDaoService.findAll();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id","name","joinDate","password"); // 프로그램 내부에서 제어

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(users);
        mapping.setFilters(filters);

        return mapping;
    }

    // 단일 사용자 조회
    // GET /admin/users/1 > /admin/v1/users/1 컨트롤러에는 문자형태로 전달됨
    //@GetMapping("v1/users/{id}")
    //@GetMapping(value = "/users/{id}/", params = "version=1") // parameter
    //@GetMapping(value = "/users/{id}", headers = "X-API-VERSION=1") // header 값에 정보를 받아 구분
    @GetMapping(value = "/users/{id}", produces = "application/vnd.company.appv1+json") // mime 타입 사용
    public MappingJacksonValue retrieveUserV1(@PathVariable int id) { // int로 선언하면 문자 형태 데이터 값이 int 형태로 컨버팅된다

        User user = userDaoService.findOne(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
            // 존재하지 않는 클래스 선언하여 생성가능
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id","name","joinDate","ssn"); // 프로그램 내부에서 제어

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);

        return mapping;
    }

    // v2
    //@GetMapping("v2/users/{id}")
    //@GetMapping(value = "/users/{id}/", params = "version=2")
    //@GetMapping(value = "/users/{id}", headers = "X-API-VERSION=2")
    @GetMapping(value = "/users/{id}", produces = "application/vnd.company.appv2+json")
    public MappingJacksonValue retrieveUserV2(@PathVariable int id) { // int로 선언하면 문자 형태 데이터 값이 int 형태로 컨버팅된다

        User user = userDaoService.findOne(id);

        if (user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
            // 존재하지 않는 클래스 선언하여 생성가능
        }

        // User -> User2
        UserV2 userV2 = new UserV2();
        BeanUtils.copyProperties(user, userV2); // id, name, joinDate, password, ssn
        userV2.setGrade("VIP"); // grade 값 설정


        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id","name","joinDate","grade"); // 화면에 보여주고자 하는 필드 값 제어

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);

        MappingJacksonValue mapping = new MappingJacksonValue(userV2);
        mapping.setFilters(filters);

        return mapping;
    }

}
