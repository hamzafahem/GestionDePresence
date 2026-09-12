package ma.uh1.mpresence.repository;

import ma.uh1.mpresence.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByDoctorantIdOrDoctorantIsNull(Long doctorantId);
    long countByLuFalse();
    List<Notification> findTop5ByOrderByDateCreationDesc();
}
