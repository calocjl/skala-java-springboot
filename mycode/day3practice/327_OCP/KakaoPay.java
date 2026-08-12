public class KakaoPay implements Payment {
    @Override
    public void pay(int amount) {
        System.out.println("카카오페이 " + amount + "원 결제 완료");
    }
}
