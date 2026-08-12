import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class HttpEchoServer {
    public static void main(String[] args) {
        int port = 8080;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("HTTP Echo Server started on port " + port);
            System.out.println("접속 경로 예시:");
            System.out.println("  GET /         → 서버 정보");
            System.out.println("  GET /users    → 사용자 목록");
            System.out.println("  GET /time     → 현재 시간");
            System.out.println("  GET /other    → 404 Not Found");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleRequest(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRequest(Socket clientSocket) {
        try (
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream())
            );
            OutputStream out = clientSocket.getOutputStream();
        ) {
            // 1. 요청 라인(첫 줄)만 파싱: 예) "GET /users HTTP/1.1"
            String requestLine = reader.readLine();
            System.out.println("Request: " + requestLine);

            // 헤더는 빈 줄이 나올 때까지 그냥 읽어서 버림 (이 실습에서는 처리 안 함)
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                // 헤더 라인들 (Host, Connection 등)
            }

            String path = "/";
            if (requestLine != null) {
                String[] parts = requestLine.split(" ");
                if (parts.length >= 2) {
                    path = parts[1];
                }
            }

            // 2. 경로에 따라 응답 바디(JSON) 결정
            String body;
            String status;

            switch (path) {
                case "/" -> {
                    status = "200 OK";
                    body = "{\"service\":\"HttpEchoServer\",\"status\":\"running\",\"port\":8080}";
                }
                case "/users" -> {
                    status = "200 OK";
                    body = "{\"users\":[{\"id\":1,\"name\":\"Alice\"},{\"id\":2,\"name\":\"Bob\"},{\"id\":3,\"name\":\"Charlie\"}]}";
                }
                case "/time" -> {
                    status = "200 OK";
                    body = "{\"time\":\"" + java.time.LocalDateTime.now() + "\"}";
                }
                default -> {
                    status = "404 Not Found";
                    body = "{\"error\":\"Not Found\",\"path\":\"" + path + "\"}";
                }
            }

            // 3. HTTP 응답 메시지 조립 (상태줄 → 헤더 → 빈줄 → 본문)
            byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

            StringBuilder response = new StringBuilder();
            response.append("HTTP/1.1 ").append(status).append("\r\n");
            response.append("Content-Type: application/json; charset=UTF-8\r\n");
            response.append("Content-Length: ").append(bodyBytes.length).append("\r\n");
            response.append("Connection: close\r\n");
            response.append("\r\n"); // 빈 줄 - 헤더의 끝을 알림
            response.append(body);

            out.write(response.toString().getBytes(StandardCharsets.UTF_8));
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ignored) {}
        }
    }
}
