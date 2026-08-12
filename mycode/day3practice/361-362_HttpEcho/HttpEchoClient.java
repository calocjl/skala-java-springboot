import java.io.*;
import java.net.*;

public class HttpEchoClient {
    public static void main(String[] args) throws Exception {
        // 요청할 경로들을 순서대로 테스트
        String[] paths = {"/", "/users", "/time", "/other"};

        for (String path : paths) {
            requestGet(path);
        }
    }

    private static void requestGet(String path) throws IOException {
        String host = "localhost";
        int port = 8080;

        System.out.println("==================================================");
        System.out.println("▶ GET " + path + " 요청");

        try (
            Socket socket = new Socket(host, port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        ) {
            // 1. HTTP 요청 메시지 작성 및 전송
            String request = "GET " + path + " HTTP/1.1\r\n" +
                              "Host: localhost\r\n" +
                              "Connection: close\r\n" +
                              "\r\n";

            System.out.println("[전송 요청]");
            System.out.println("  GET " + path + " HTTP/1.1");
            System.out.println("  Host: localhost");
            System.out.println("  Connection: close");
            System.out.println("  (빈 줄 - 헤더 끝)\n");

            writer.print(request);
            writer.flush();

            // 2. 응답 수신 및 파싱
            String statusLine = reader.readLine();
            System.out.println("[수신 응답]");
            System.out.println("  상태 라인: " + statusLine);

            String line;
            int contentLength = 0;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                System.out.println("  헤더: " + line);
                if (line.startsWith("Content-Length:")) {
                    contentLength = Integer.parseInt(line.split(":")[1].trim());
                }
            }
            System.out.println("  (빈 줄 - 헤더 끝)");

            // 3. 본문(body) 읽기
            char[] bodyChars = new char[contentLength];
            reader.read(bodyChars, 0, contentLength);
            String body = new String(bodyChars);

            System.out.println("  본문(JSON):");
            System.out.println(body);

            int statusCode = Integer.parseInt(statusLine.split(" ")[1]);
            String result = (statusCode == 200) ? "성공" : "실패";
            System.out.println("\n[결과] 상태코드=" + statusCode + " " +
                    (statusCode == 200 ? "✓" : "✗") + " " + result);
        }

        System.out.println("==================================================\n");
    }
}
