public class Main {
    public static void main(String[] args) {
        // 실습 1: 유선 키보드 + 모니터 조합
        System.out.println("=== 유선 장치 조합 ===");
        InputDevice keyboard = new Keyboard();
        OutputDevice monitor = new Monitor();
        Computer computer1 = new Computer(keyboard, monitor);
        computer1.operate();

        // 실습 2: 무선 키보드 + 무선 디스플레이 조합
        // → Computer 클래스는 단 한 글자도 수정하지 않았음! (OCP + DIP 동시 증명)
        System.out.println("\n=== 무선 장치 조합 ===");
        InputDevice wirelessKeyboard = new WirelessKeyboard();
        OutputDevice wirelessDisplay = new WirelessDisplay();
        Computer computer2 = new Computer(wirelessKeyboard, wirelessDisplay);
        computer2.operate();
    }
}
