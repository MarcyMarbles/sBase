package kz.saya.sbase.Mappers;

import kz.saya.sbase.Entity.MenuElement;
import kz.saya.sbase.POJOs.MenuPOJO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MenuMapper {

    public List<MenuPOJO> toDtoTree(Collection<MenuElement> elements) {

        // 1. преобразуем каждую сущность в DTO и положим в map по id
        Map<Integer, MenuPOJO> byId = elements.stream()
                .collect(Collectors.toMap(MenuElement::getId, this::toDto));

        // 2. связываем детей с родителями
        List<MenuPOJO> roots = new ArrayList<>();

        for (MenuElement e : elements) {
            MenuPOJO dto = byId.get(e.getId());
            if (e.getParentElement() == null) {
                roots.add(dto);                 // корневой пункт
            } else {
                MenuPOJO parent = byId.get(e.getParentElement().getId());
                parent.getChildren().add(dto);  // вложенный пункт
            }
        }

        // 3. сортировка по rank внутри каждого уровня
        sortRecursively(roots);
        return roots;
    }

    /* -------- helpers -------- */

    private MenuPOJO toDto(MenuElement e) {
        MenuPOJO d = new MenuPOJO();
        d.setId(e.getId());
        d.setName(e.getName());
        d.setIcon(e.getIcon());
        d.setUrl(e.getUrl());
        d.setGroup(e.isGroupp());
        return d;
    }

    private void sortRecursively(List<MenuPOJO> list) {
        list.sort(Comparator.comparingInt(MenuPOJO::getId)); // или по rank, если нужен
        list.forEach(n -> sortRecursively(n.getChildren()));
    }
}
