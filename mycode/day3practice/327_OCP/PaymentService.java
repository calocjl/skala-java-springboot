public class PaymentService {
    public void processPayment(Payment payment, int amount) {
        // 어떤 결제 수단 클래스가 들어오든 이 코드는 절대 변경되지 않습니다.
        payment.pay(amount);
    }
}
