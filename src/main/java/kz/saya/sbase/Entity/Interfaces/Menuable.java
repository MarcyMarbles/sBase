package kz.saya.sbase.Entity.Interfaces;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component // чтобы Spring мог найти аннотацию
public @interface Menuable {
    /** human‑readable название пункта меню */
    String caption();
    /** иконка (например, имя из библиотеки feather/heroicons) */
    String icon() default "";
    /** сортировка внутри уровня меню */
    int order() default 0;
    /** родитель (для вложенного меню) ― задаём dot‑path, чтобы без циклов */
    String parent() default "";
    boolean group() default false;
}
