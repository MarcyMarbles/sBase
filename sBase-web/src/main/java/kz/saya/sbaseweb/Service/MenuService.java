package kz.saya.sbaseweb.Service;

import jakarta.transaction.Transactional;
import kz.saya.sbasecore.Entity.Roles;
import kz.saya.sbaseweb.Components.MenuScanner;
import kz.saya.sbaseweb.Entity.MenuElement;
import kz.saya.sbaseweb.Repositories.MenuElementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuElementRepository repo;
    private final MenuScanner scanner;

    @Transactional
    public void init() {
        // 1. сканируем код
        List<MenuElement> scanned = scanner.scan();
        if (scanned.isEmpty()) return;

        // 2. читаем, что уже есть в таблице
        Map<String, MenuElement> dbByCode = repo.findAll().stream()
                .collect(Collectors.toMap(MenuElement::getCode, Function.identity()));

        List<MenuElement> toInsert = new ArrayList<>();
        List<MenuElement> toUpdate = new ArrayList<>();

        // 3. обновляем/добавляем
        scanned.forEach(s -> {
            MenuElement db = dbByCode.remove(s.getCode()); // убрали из map — останутся «лишние»
            if (db == null) {
                // новый пункт
                toInsert.add(s);
            } else {
                // существующий: проверяем, поменялось ли что‑нибудь
                boolean changed = false;
                if (!Objects.equals(db.getName(), s.getName())) {
                    db.setName(s.getName());
                    changed = true;
                }
                if (!Objects.equals(db.getIcon(), s.getIcon())) {
                    db.setIcon(s.getIcon());
                    changed = true;
                }
                if (!Objects.equals(db.getUrl(), s.getUrl())) {
                    db.setUrl(s.getUrl());
                    changed = true;
                }
                if (db.getRank() != s.getRank()) {
                    db.setRank(s.getRank());
                    changed = true;
                }
                if (!Objects.equals(db.getParentCode(), s.getParentCode())) {
                    db.setParentCode(s.getParentCode());
                    changed = true;
                }
                if (changed) toUpdate.add(db);
            }
        });

        // 4. то, что осталось в dbByCode — лишние пункты, которых больше нет в коде
        //    решите, нужно ли их удалять:
        // repo.deleteAll(dbByCode.values());

        repo.saveAll(toInsert);
        repo.saveAll(toUpdate);

        // 5. второй проход — линковка родителей (уже на полном наборе)
        Map<String, MenuElement> byCode =
                repo.findAll().stream()
                        .collect(Collectors.toMap(MenuElement::getCode, Function.identity()));

        byCode.values().forEach(e -> {
            if (e.getParentCode() != null && !e.getParentCode().isBlank()) {
                e.setParentElement(byCode.get(e.getParentCode()));
            }
        });
        repo.saveAll(byCode.values());
    }


    /**
     * меню для текущего пользователя
     */
    public LinkedHashSet<MenuElement> generateMenu(Set<Roles> userRoles) {
        // пункты, доступные по ролям
        List<MenuElement> allowed = repo.findAllowed(userRoles);

        // соберём все элементы + их родителей
        Set<MenuElement> collected = new HashSet<>(allowed);
        allowed.forEach(e -> {
            if(e.isGroupp()) e.setUrl(null); // чтобы не вызывать href в UI
            for (MenuElement p = e.getParentElement(); p != null; p = p.getParentElement()) {
                collected.add(p);
            }
        });

        // сортируем по rank и возвращаем как LinkedHashSet
        return collected.stream()
                .sorted(Comparator.comparingInt(MenuElement::getRank))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

}

