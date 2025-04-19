package com.example.sbase.Components;

import com.example.sbase.Entity.Interfaces.Menuable;
import com.example.sbase.Entity.MenuElement;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MenuScanner {

    private final ApplicationContext ctx;

    public List<MenuElement> scan() {
        return ctx.getBeansWithAnnotation(Menuable.class).values().stream()
                .map(bean -> build(AopUtils.getTargetClass(bean))) // правильно извлекаем оригинальный класс
                .sorted(Comparator.comparingInt(MenuElement::getRank))
                .toList();
    }

    private MenuElement build(Class<?> clazz) {
        Menuable a = clazz.getAnnotation(Menuable.class);
        MenuElement e = new MenuElement();
        e.setGroupp(a.group());
        if (a.group()) {
            e.setUrl("/#");
        } else {
            e.setUrl(resolveUrl(clazz));
        }
        e.setName(a.caption());
        e.setIcon(a.icon());
        e.setRank(a.order());
        e.setParentCode(a.parent());

        RequestMapping rm = AnnotationUtils.findAnnotation(clazz, RequestMapping.class);
        if (rm != null && rm.value().length > 0) {
            e.setUrl(rm.value()[0]);
            e.setCode(rm.value()[0]);
        }
        return e;
    }

    private String resolveUrl(Class<?> clazz) {

        RequestMapping rm = AnnotationUtils.findAnnotation(clazz, RequestMapping.class);
        if (rm != null && rm.value().length > 0) {
            return rm.value()[0];
        }

        for (Method m : clazz.getDeclaredMethods()) {
            GetMapping get = AnnotationUtils.findAnnotation(m, GetMapping.class);
            if (get != null && get.value().length > 0) return get.value()[0];

            rm = AnnotationUtils.findAnnotation(m, RequestMapping.class);
            if (rm != null && rm.value().length > 0) return rm.value()[0];

        }
        return null;
    }

}


