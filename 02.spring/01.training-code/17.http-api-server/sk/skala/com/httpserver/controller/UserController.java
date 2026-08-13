package sk.skala.com.httpserver.controller;

import sk.skala.com.httpserver.annotation.Controller;
import sk.skala.com.httpserver.annotation.GetMapping;
import sk.skala.com.httpserver.annotation.PathVariable;
import sk.skala.com.httpserver.annotation.PostMapping;
import sk.skala.com.httpserver.domain.User;

import java.util.ArrayList;
import java.util.List;

// @Controller가 붙은 클래스. 메모리 리스트(List<User>)로 사용자 데이터를 관리하며
// GET/POST 요청 처리 메서드 3개를 제공한다.
@Controller
public class UserController {

    private final List<User> users = new ArrayList<>();

    public UserController() {
        users.add(new User(1, "홍길동", "USER", "hong@sk.com"));
        users.add(new User(2, "김철수", "ADMIN", "kim@sk.com"));
        users.add(new User(3, "이영희", "USER", "lee@sk.com"));
    }

    // 전체 사용자 목록 반환
    @GetMapping("/users")
    public List<User> getUsers() {
        return users;
    }

    // id로 사용자 한 명 조회 (없으면 null)
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    // 새 사용자를 목록에 추가 후 반환
    @PostMapping("/users")
    public User createUser(User user) {
        users.add(user);
        return user;
    }
}
