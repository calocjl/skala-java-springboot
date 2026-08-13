package sk.skala.com.httpserver.server;

import sk.skala.com.httpserver.annotation.Controller;
import sk.skala.com.httpserver.annotation.GetMapping;
import sk.skala.com.httpserver.annotation.PathVariable;
import sk.skala.com.httpserver.annotation.PostMapping;
import sk.skala.com.httpserver.domain.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 소켓을 열어 요청을 받고, 라우팅 테이블에서 처리할 메서드를 찾아 리플렉션으로 호출한 뒤
 * 응답을 만들어 보내는 프레임워크 본체.
 *
 * Spring에서의 대응 개념: 내장 톰캣 + DispatcherServlet + RequestMappingHandlerMapping
 */
public class HttpWebServer {

    private final int port;

    // 정확히 일치하는 라우트: "GET /users" -> RouteEntry
    private final Map<String, RouteEntry> routes = new HashMap<>();

    // 경로 변수({id})가 있는 라우트: prefix로 시작 여부를 검사
    private final List<PatternRoute> patternRoutes = new ArrayList<>();

    public HttpWebServer(int port) {
        this.port = port;
    }

    // 라우팅 테이블의 값 타입: (컨트롤러 인스턴스, Method) 쌍을 저장
    private record RouteEntry(Object controller, Method method) {
    }

    // 경로 변수가 있는 라우트 정보: (HTTP 메서드, URL prefix, RouteEntry) 저장
    private record PatternRoute(String httpMethod, String prefix, RouteEntry entry) {
    }

    /**
     * @Controller 여부를 검사한 후, 클래스의 모든 메서드를 훑어
     * @GetMapping / @PostMapping이 있으면 registerRoute()를 호출한다.
     */
    public void registerController(Object controller) {
        Class<?> clazz = controller.getClass();

        if (!clazz.isAnnotationPresent(Controller.class)) {
            System.out.println("[경고] @Controller가 없는 클래스입니다: " + clazz.getSimpleName());
            return;
        }

        System.out.println("[컨트롤러 등록] " + clazz.getSimpleName());

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(GetMapping.class)) {
                String path = method.getAnnotation(GetMapping.class).value();
                registerRoute("GET", path, controller, method);
                if (path.contains("{")) {
                    System.out.println("  GET " + path + " -> " + method.getName() + "(경로 변수)");
                } else {
                    System.out.println("  GET " + path + " -> " + method.getName() + "()");
                }
            } else if (method.isAnnotationPresent(PostMapping.class)) {
                String path = method.getAnnotation(PostMapping.class).value();
                registerRoute("POST", path, controller, method);
                System.out.println("  POST " + path + " -> " + method.getName() + "()");
            }
        }
    }

    /**
     * 경로에 '{'가 없으면 routes Map(정확 일치)에,
     * 있으면 patternRoutes 목록(prefix 일치)에 등록한다.
     */
    private void registerRoute(String httpMethod, String path, Object controller, Method method) {
        RouteEntry entry = new RouteEntry(controller, method);

        if (path.contains("{")) {
            // "/users/{id}" -> prefix "/users/"
            int braceIndex = path.indexOf('{');
            String prefix = path.substring(0, braceIndex);
            patternRoutes.add(new PatternRoute(httpMethod, prefix, entry));
        } else {
            routes.put(httpMethod + " " + path, entry);
        }
    }

    /**
     * ServerSocket을 열고 accept()로 클라이언트 연결을 반복 대기,
     * 연결마다 handleRequest()를 호출한다.
     */
    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println();
            System.out.println("HttpWebServer started on port " + port);
            System.out.println("등록된 라우트 수: " + routes.size());
            System.out.println("========================================");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleRequest(clientSocket);
            }
        }
    }

    /**
     * 요청 한 건을 읽고, 라우팅하고, 메서드를 호출하고, 응답을 전송하는 전체 처리를 담당한다.
     */
    private void handleRequest(Socket clientSocket) {
        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), false, StandardCharsets.UTF_8)
        ) {
            // Step 1: 요청 라인 한 줄을 읽는다 (예: "GET /users/1 HTTP/1.1")
            String requestLine = reader.readLine();
            if (requestLine == null || requestLine.isBlank()) {
                clientSocket.close();
                return;
            }
            System.out.println("Request: " + requestLine);

            // Step 2: 빈 줄이 나올 때까지 헤더를 읽으며 Content-Length를 찾는다
            int contentLength = 0;
            String headerLine;
            while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
                if (headerLine.toLowerCase().startsWith("content-length:")) {
                    contentLength = Integer.parseInt(headerLine.split(":")[1].trim());
                }
            }

            // Step 3: contentLength가 0보다 크면(POST인 경우) 그 길이만큼 문자를 읽는다
            String requestBody = "";
            if (contentLength > 0) {
                char[] bodyChars = new char[contentLength];
                reader.read(bodyChars, 0, contentLength);
                requestBody = new String(bodyChars);
            }

            // Step 4: 요청 라인을 공백 기준으로 나눠 httpMethod와 path를 분리
            String[] parts = requestLine.split(" ");
            String httpMethod = parts[0];
            String path = parts.length > 1 ? parts[1] : "/";

            // Step 5: 라우팅 검색
            RouteEntry matched = routes.get(httpMethod + " " + path);
            String pathVariable = null;

            if (matched == null) {
                for (PatternRoute patternRoute : patternRoutes) {
                    if (patternRoute.httpMethod().equals(httpMethod) && path.startsWith(patternRoute.prefix())) {
                        matched = patternRoute.entry();
                        pathVariable = path.substring(patternRoute.prefix().length());
                        break;
                    }
                }
            }

            if (matched == null) {
                sendResponse(writer, 404, "Not Found", "{\"error\":\"Not Found\",\"path\":\"" + path + "\"}");
                return;
            }

            // Step 6: 찾은 Method를 리플렉션으로 호출
            Object result;
            try {
                Parameter[] parameters = matched.method().getParameters();
                if (parameters.length == 0) {
                    result = matched.method().invoke(matched.controller());
                } else {
                    Parameter param = parameters[0];
                    String rawValue;
                    if (param.isAnnotationPresent(PathVariable.class)) {
                        rawValue = pathVariable;
                    } else {
                        rawValue = requestBody;
                    }
                    Object converted = convertParam(param.getType(), rawValue);
                    result = matched.method().invoke(matched.controller(), converted);
                }
            } catch (Exception e) {
                sendResponse(writer, 500, "Internal Server Error",
                        "{\"error\":\"" + e.getMessage() + "\"}");
                return;
            }

            // 반환 객체를 JSON 문자열로 변환 후 응답
            String responseBody = toResponseBody(result);
            sendResponse(writer, 200, "OK", responseBody);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * 문자열 값을 메서드 파라미터 타입(int, User, String)에 맞게 변환한다.
     */
    private Object convertParam(Class<?> type, String rawValue) {
        if (type == int.class || type == Integer.class) {
            return Integer.parseInt(rawValue);
        } else if (type == User.class) {
            return parseUser(rawValue);
        } else {
            return rawValue; // String
        }
    }

    /**
     * 메서드 반환값(User, List<User>, 기타)을 JSON 문자열로 변환한다.
     */
    private String toResponseBody(Object result) {
        if (result == null) {
            return "null";
        }
        if (result instanceof User user) {
            return user.toJson();
        }
        if (result instanceof List<?> list) {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                Object item = list.get(i);
                if (item instanceof User user) {
                    sb.append(user.toJson());
                } else {
                    sb.append("\"").append(item).append("\"");
                }
                if (i < list.size() - 1) {
                    sb.append(",");
                }
            }
            sb.append("]");
            return sb.toString();
        }
        return "\"" + result + "\"";
    }

    /**
     * 정규식으로 JSON 문자열에서 id/name/role/email을 추출해 User 객체를 생성한다.
     */
    private User parseUser(String json) {
        int id = extractInt(json, "id");
        String name = extractString(json, "name");
        String role = extractString(json, "role");
        String email = extractString(json, "email");
        return new User(id, name, role, email);
    }

    private String extractString(String json, String field) {
        Pattern pattern = Pattern.compile("\"" + field + "\"\\s*:\\s*\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(json);
        return matcher.find() ? matcher.group(1) : "";
    }

    private int extractInt(String json, String field) {
        Pattern pattern = Pattern.compile("\"" + field + "\"\\s*:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(json);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : 0;
    }

    /**
     * 상태라인 + 헤더 + 빈 줄 + Body 순서로 HTTP 응답을 조립해 전송한다.
     */
    private void sendResponse(PrintWriter writer, int statusCode, String statusText, String body) {
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

        writer.print("HTTP/1.1 " + statusCode + " " + statusText + "\r\n");
        writer.print("Content-Type: application/json; charset=UTF-8\r\n");
        writer.print("Content-Length: " + bodyBytes.length + "\r\n");
        writer.print("Connection: close\r\n");
        writer.print("\r\n");
        writer.print(body);
        writer.flush();

        System.out.println("Response: " + statusCode + " " + statusText);
    }
}
