public class Main {
    public static void main(String[] args) {
        PaymentService paymentService = new PaymentService();

        // 1. 카카오페이 결제 시도
        Payment kakao = new KakaoPay();
        paymentService.processPayment(kakao, 15000);

        // 2. 신용카드 결제 시도
        Payment card = new CardPay();
        paymentService.processPayment(card, 53000);

        // 3. OCP를 증명하는 핵심: 기존 코드를 전혀 고치지 않고 새로 추가된 NaverPay를 바로 수용함
        Payment naver = new NaverPay();
        paymentService.processPayment(naver, 21000);
    }
}
