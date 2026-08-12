public class User {
    private String name;
    private int age;
    private String email;
    private String phoneNumber;

    // private 생성자 → Builder로만 객체 생성 가능
    private User(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.email = builder.email;
        this.phoneNumber = builder.phoneNumber;
    }

    @Override
    public String toString() {
        return "User{name='" + name + "', age=" + age +
                ", email='" + email + "', phoneNumber='" + phoneNumber + "'}";
    }

    // 정적 내부 클래스(Static Inner Class) Builder
    public static class Builder {
        private String name;
        private int age;
        private String email;
        private String phoneNumber;

        // 빌더 생성자 (필수 값이 있다면 여기에 인자로 추가 가능, 현재는 기본 생성자)
        public Builder() {}

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        // 추가 필드에 대한 빌더 메서드
        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        // 최종 객체를 조립하여 반환하는 build() 메서드
        public User build() {
            return new User(this);
        }
    }
}
