package com.project.restfulwebservice.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/jpa") // prefix 설정
public class UserJpaController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    // http://localhost:8088/jpa/users
    // 전체 사용자 조회
    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    // 개별 사용자 조회
    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent()) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        // HATEOAS 기능
        EntityModel userModel = EntityModel.of(user);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        userModel.add(linkTo.withRel("all-users"));

        return userModel;
    }

    // 사용자 삭제
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
    }

    // 사용자 추가
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri(); // uri 데이터로 변경

        return ResponseEntity.created(location).build();
    }

    // 사용자의 전체 post 확인
    @GetMapping("/users/{id}/posts")
    public List<Post> retrieveAllPostsByUser(@PathVariable int id) {
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent()) { // user가 존재한다면
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
        return user.get().getPosts(); // 해당 user의 post를 조회

    }

    // post 추가
    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> createUser(@PathVariable int id, @RequestBody Post post) {

        // 사용자 정보 검색한 후 정보의 id 값을 post에 지정
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent()) { // 사용자가 존재하면
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        post.setUser(user.get()); // 아이디 값 지정된 상태에서 post 추가
        Post savedPost = postRepository.save(post);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri(); // uri 데이터로 변경

        return ResponseEntity.created(location).build();
    }


}
