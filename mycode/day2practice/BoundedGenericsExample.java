import java.util.ArrayList;
import java.util.List;

public class BoundedGenericsExample {

    // -------------------------------------------------------
    // Upper Bound Wildcard 메서드: <? extends Number>
    // Number 하위 타입 리스트의 합계를 반환 (읽기 전용)
    // -------------------------------------------------------
    public static double sumBox(List<? extends Number> list) {
        double sum = 0;
        for (Number n : list) {
            sum += n.doubleValue();
        }

        // 컴파일 에러: upper bound wildcard 리스트에는 추가 불가
        // list.add(10);

        return sum;
    }

    // -------------------------------------------------------
    // Lower Bound Wildcard 메서드: <? super Integer>
    // Integer 및 그 상위 타입(Number, Object)만 허용 (쓰기 전용)
    // → 값을 하나씩 받아서 리스트에 추가
    // -------------------------------------------------------
    public static void addBox(List<? super Integer> list, int value) {
        list.add(value);

        // 컴파일 에러: lower bound wildcard는 Object로만 꺼낼 수 있음
        // Integer first = list.get(0);
    }

    public static void main(String[] args) {
        // ---- Upper Bound Wildcard: sumBox(List<? extends Number>) ----
        List<Integer> intList = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        List<Double> dblList = new ArrayList<>(List.of(1.1, 2.2, 3.3));

        System.out.println("Integer 리스트 합계: " + sumBox(intList));
        System.out.println("Double 리스트 합계: " + sumBox(dblList));

        // ---- Lower Bound Wildcard: addBox(List<? super Integer>, int) ----
        List<Number> numberList = new ArrayList<>(); // Number는 Integer의 상위 타입 → 허용

        addBox(numberList, 10);
        addBox(numberList, 20);
        addBox(numberList, 30);

        System.out.println("numberList: " + numberList);
    }
}