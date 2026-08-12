public class Square implements Shape {
    private final int length;

    public Square(int length) {
        this.length = length;
    }

    @Override
    public int getArea() {
        return length * length;
    }
}
