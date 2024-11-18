package rs.raf.AuthService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.AuthService.model.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role,Long> {
}
