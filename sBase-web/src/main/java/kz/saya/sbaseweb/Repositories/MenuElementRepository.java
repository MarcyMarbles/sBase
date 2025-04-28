package kz.saya.sbaseweb.Repositories;

import kz.saya.sbasecore.Entity.Roles;
import kz.saya.sbaseweb.Entity.MenuElement;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuElementRepository extends JpaRepository<MenuElement, UUID> {

    /**
     * пункты, доступные любому из переданных ролей
     */
    @Query("""
             select distinct e\s
               from MenuElement e
                    left join e.accessibleViaRoles r
              where r is null or r in :roles
             order by e.rank
            \s""")
    List<MenuElement> findAllowed(@Param("roles") Collection<Roles> roles);

    /**
     * доступны для ленивой подгрузки целиком (N+1‑safe)
     */
    @EntityGraph(attributePaths = {"parentElement"})
    List<MenuElement> findAllByAccessibleViaRolesIn(Collection<List<Roles>> accessibleViaRoles);

    /**
     * проверка, что такого кода ещё нет
     */
    boolean existsByCode(String code);

    /**
     * поиск по коду — пригодится при связывании parent
     */
    Optional<MenuElement> findByCode(String code);


    List<MenuElement> findAllByGrouppFalseAndAccessibleViaRolesIn(Collection<List<Roles>> accessibleViaRoles);

}
