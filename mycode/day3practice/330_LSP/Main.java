public class Main {
    public static void renderArea(Shape shape) {
        // 어떤 도형(Shape)이 들어오든, 개발자는 뒤통수 맞을 걱정 없이 getArea() 규칙만 믿고 쓰면 됨
        System.out.println("도형의 넓이: " + shape.getArea());
    }

    public static void main(String[] args) {
        Shape rect = new Rectangle(5, 10);
        Shape square = new Square(5);

        renderArea(rect);   // 출력: 50
        renderArea(square); // 출력: 25
    }
}
