package kz.saya.sbase.Repos;

import kz.saya.sbase.Entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Roles, UUID> {
    Optional<Roles> findByName(String name);

    Optional<Roles> findByDescription(String description);

    Optional<Roles> findByNameAndDescription(String name, String description);

    Optional<Roles> findByDefaultRole(boolean defaultRole);
}
