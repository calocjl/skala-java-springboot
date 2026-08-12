public class NaverPay implements Payment {
    @Override
    public void pay(int amount) {
        System.out.println("네이버페이 " + amount + "원 결제 완료");
    }
}
