package com.project.restfulwebservice.user;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class UserDaoService {

    private static List<User> users = new ArrayList<>();

    private static int usersCount = 3;

    // 사용자 등록
    static {
        users.add(new User(1, "Jenny", new Date(), "pass1", "0000000-111111"));
        users.add(new User(2, "Lisa", new Date(), "pass2", "0000000-222222"));
        users.add(new User(3, "Rose", new Date(), "pass3", "0000000-333333"));
    }

    // 전체 조회
    public List<User> findAll(){
        return users;
    }

    public User save(User user){
        if(user.getId() == null){ // 회원이 존재하지 않은 경우
            user.setId(++usersCount); // users 증가값을 통해 아이디 추가
       }
        users.add(user);
        return user;
    }
    // 회원 조회
    public User findOne(int id){
        for(User user : users){
            if(user.getId() == id){
                return user;
            }
        }
        return null;
    }

    // 사용자 삭제
    public User deleteById(int id){
        //열거형 타입의 데이터 iterator : 가지고 있는 리스트 배열 형태의 데이터를 순차적으로 접근해서 사용하기 위함
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()){ // 전체 데이터를 순차적으로 가져오기
            User user = iterator.next();

            if(user.getId() == id){ // 매개변수 전달받은 id와 동일한 id 값이 들어가있는 경우
                iterator.remove(); // 데이터 삭제
                return user;
            }
        }
        return null;
    }

}
