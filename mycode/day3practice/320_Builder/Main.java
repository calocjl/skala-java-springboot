public class Main {
    public static void main(String[] args) {
        User user = new User.Builder()
                .setName("김철수")
                .setAge(25)
                .setEmail("kim@example.com")
                .setPhoneNumber("010-1234-5678")
                .build();

        System.out.println(user);
    }
}
