public class CardPay implements Payment {
    @Override
    public void pay(int amount) {
        System.out.println("신용카드 " + amount + "원 결제 완료");
    }
}
