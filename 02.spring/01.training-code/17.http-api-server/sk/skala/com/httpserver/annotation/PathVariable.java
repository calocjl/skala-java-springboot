package sk.skala.com.httpserver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 이 파라미터가 URL 경로의 변수 값({id} 등)을 받는 것임을 표시하는 파라미터 대상 어노테이션
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface PathVariable {
    String value();
}
