import java.util.Scanner;

public class CalculatorString {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueCalc = true;

        String[] history = new String[100];
        int historyCount = 0;

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

                if (historyCount < history.length) {
                    history[historyCount] = record;
                    historyCount++;
                }

            } catch (ArithmeticException e) {
                System.out.println("예외 발생: " + e.getMessage());
            }

            System.out.print("계속하려면 c, 종료하려면 q: ");
            String choice = scanner.next();

            if (choice.equals("q")) {
                continueCalc = false;
            }
        }

        System.out.println("\n=== 계산 기록 ===");
        for (String record : history) {
            if (record != null) {
                System.out.println(record);
            }
        }

        System.out.println("계산기를 종료합니다.");
        scanner.close();
    }
}