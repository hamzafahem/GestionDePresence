package ma.uh1.mpresence.repository;

import ma.uh1.mpresence.entity.PublicationRecherche;
import ma.uh1.mpresence.entity.TypePublication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PublicationRechercheRepository extends JpaRepository<PublicationRecherche, Long> {
    List<PublicationRecherche> findByDoctorantId(Long doctorantId);
    List<PublicationRecherche> findByType(TypePublication type);
    List<PublicationRecherche> findByDateCreationAfter(LocalDateTime after);
    List<PublicationRecherche> findTop5ByOrderByDateCreationDesc();
}
