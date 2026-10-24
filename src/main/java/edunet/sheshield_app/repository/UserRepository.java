package edunet.sheshield_app.repository;

import edunet.sheshield_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/** Database access for users. Spring writes the code from the method names. */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}