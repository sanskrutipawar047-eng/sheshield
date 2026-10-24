package edunet.sheshield_app.repository;

import edunet.sheshield_app.entity.TrustedContact;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TrustedContactRepository extends JpaRepository<TrustedContact, Long> {
    List<TrustedContact> findByUser_UserId(Long userId);
}
