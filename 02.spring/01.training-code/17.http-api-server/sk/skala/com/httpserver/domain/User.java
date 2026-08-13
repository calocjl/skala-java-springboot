package sk.skala.com.httpserver.domain;

// 사용자 도메인 객체 (DTO). id, name, role, email 필드를 가진 불변 객체.
// Spring에서 Jackson이 자동으로 해주는 객체 -> JSON 변환을, 여기서는 직접 문자열을 조립해서 수행한다.
public class User {

    private final int id;
    private final String name;
    private final String role;
    private final String email;

    public User(int id, String name, String role, String email) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    // 자기 자신을 JSON 문자열로 직렬화
    public String toJson() {
        return "{"
                + "\"id\":" + id + ","
                + "\"name\":\"" + name + "\","
                + "\"role\":\"" + role + "\","
                + "\"email\":\"" + email + "\""
                + "}";
    }
}
