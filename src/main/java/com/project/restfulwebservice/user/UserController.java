package com.project.restfulwebservice.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
public class UserController {

    private UserDaoService userDaoService;

    // 생성자 통해 의존성 주입
    public UserController (UserDaoService userDaoService){
        this.userDaoService = userDaoService;
    }

    // 전체 사용자 조회
    @GetMapping("/users")
    public List<User> retriveAllUsers(){
        return userDaoService.findAll();
    }

    // 단일 사용자 조회
    // GET /users/1 or /users/10 ->  컨트롤러에는 문자형태로 전달됨
    @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable int id){ // int로 선언하면 문자 형태 데이터 값이 int 형태로 컨버팅된다
        User user = userDaoService.findOne(id);

        if(user == null){
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
            // 존재하지 않는 클래스 선언하여 생성가능
        }

        return user;
    }

    // 사용자 추가
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user){
        User savedUser = userDaoService.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return  ResponseEntity.created(location).build();
    }

    // 사용자 삭제
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id){
        User user = userDaoService.deleteById(id);

        if(user == null){ // 삭제할 데이터가 존재하지 않으면
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
    }

}
