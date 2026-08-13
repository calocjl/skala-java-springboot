package sk.skala.com.httpserver;

import sk.skala.com.httpserver.controller.UserController;
import sk.skala.com.httpserver.server.HttpWebServer;

// 애플리케이션 진입점. HttpWebServer를 생성하고 UserController를 등록한 뒤 서버를 시작한다.
// Spring에서의 대응 개념: @SpringBootApplication + main()
public class Main {
    public static void main(String[] args) throws Exception {
        HttpWebServer server = new HttpWebServer(8080);
        server.registerController(new UserController());
        server.start();
    }
}
