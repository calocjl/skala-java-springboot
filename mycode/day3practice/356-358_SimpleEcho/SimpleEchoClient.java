import java.io.*;
import java.net.*;

public class SimpleEchoClient {
    public static void main(String[] args) throws Exception {
        String host = "localhost"; // 또는 서버 IP
        int port = 8080;

        InetAddress serverAddr = InetAddress.getByName(host);

        try (
            Socket socket = new Socket(serverAddr, port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        ) {
            // 서버로 메시지 전송
            writer.println("Hello Server!");

            // 서버에서 Echo 응답 수신
            String response = reader.readLine();
            System.out.println("Response: " + response);
        }
    }
}
