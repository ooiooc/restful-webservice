package com.project.restfulwebservice.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue
    private Integer id;

    private String description;

    // User : Post 의 관계 -> 1 : (0~N) / Main : Sub -> Parent(주 테이블) : Child(참조 테이블)
    @ManyToOne(fetch = FetchType.LAZY) // 하나의 유저 데이터 값과 매칭, 한 명의 유저가 여러 개의 게시물을 작성할 수 있기 때문에 ManyToOne
    @JsonIgnore
    private User user; // post 호출 시 바로 user 데이터를 가져오지 않도록 LAZY 설정

}