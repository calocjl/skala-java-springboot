import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class Calculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueCalc = true;

        // 변경 전: List<String> history = new ArrayList<>();
        // 변경 후:
        Map<Integer, String> historyMap = new HashMap<>();

        while (continueCalc) {
            System.out.print("첫 번째 숫자: ");
            double firstNumber = scanner.nextDouble();

            System.out.print("연산자(+ - * /): ");
            String operator = scanner.next();

            System.out.print("두 번째 숫자: ");
            double secondNumber = scanner.nextDouble();

            try {
                if (operator.equals("/") && secondNumber == 0) {
                    throw new ArithmeticException("0으로 나눌 수 없습니다.");
                }

                double result = switch (operator) {
                    case "+" -> firstNumber + secondNumber;
                    case "-" -> firstNumber - secondNumber;
                    case "*" -> firstNumber * secondNumber;
                    case "/" -> firstNumber / secondNumber;
                    default -> Double.NaN;
                };

                System.out.println("결과: " + result);

                String record = firstNumber + " " + operator + " " + secondNumber + " = " + result;

                // 변경 전: history.add(record);
                // 변경 후: Key는 문자열의 hashCode(), Value는 기록 문자열
                historyMap.put(record.hashCode(), record);

            } catch (ArithmeticException e) {
                System.out.println("예외 발생: " + e.getMessage());
            }

            System.out.print("계속하려면 c, 종료하려면 q: ");
            String choice = scanner.next();

            if (choice.equals("q")) {
                continueCalc = false;
            }
        }

        // 변경 전: Iterator<String>로 List 순회
        // 변경 후: Map.Entry를 Iterator로 순회
        System.out.println("\n=== 계산 기록 ===");
        Iterator<Map.Entry<Integer, String>> iterator = historyMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, String> entry = iterator.next();
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("계산기를 종료합니다.");
        scanner.close();
    }
}