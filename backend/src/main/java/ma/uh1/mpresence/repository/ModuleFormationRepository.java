package ma.uh1.mpresence.repository;

import ma.uh1.mpresence.entity.ModuleFormation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ModuleFormationRepository extends JpaRepository<ModuleFormation, Long> {
    Optional<ModuleFormation> findByIdAndFormationId(Long id, Long formationId);
    List<ModuleFormation> findByFormationId(Long formationId);
    long countByFormateurId(Long formateurId);
}
