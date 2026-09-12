package ma.uh1.mpresence.service;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.ModuleDto;
import ma.uh1.mpresence.dto.ModuleUpsertRequest;
import ma.uh1.mpresence.entity.Formation;
import ma.uh1.mpresence.entity.ModuleFormation;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.exception.ResourceNotFoundException;
import ma.uh1.mpresence.repository.FormationRepository;
import ma.uh1.mpresence.repository.ModuleFormationRepository;
import ma.uh1.mpresence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuleService {

    private final ModuleFormationRepository moduleFormationRepository;
    private final FormationRepository formationRepository;
    private final UserRepository userRepository;

    public List<ModuleDto> getAllForFormation(Long formationId) {
        ensureFormationExists(formationId);
        return moduleFormationRepository.findByFormationId(formationId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ModuleDto create(Long formationId, ModuleUpsertRequest request) {
        Formation formation = ensureFormationExists(formationId);
        ModuleFormation module = new ModuleFormation();
        module.setNom(request.getNom());
        module.setFormation(formation);
        applyFormateur(module, request.getFormateurId());
        return toDto(moduleFormationRepository.save(module));
    }

    public ModuleDto update(Long formationId, Long moduleId, ModuleUpsertRequest request) {
        ModuleFormation module = moduleFormationRepository.findByIdAndFormationId(moduleId, formationId)
                .orElseThrow(() -> new ResourceNotFoundException("Module introuvable: " + moduleId));
        module.setNom(request.getNom());
        applyFormateur(module, request.getFormateurId());
        return toDto(moduleFormationRepository.save(module));
    }

    public ModuleDto affecterFormateur(Long formationId, Long moduleId, Long formateurId) {
        ModuleFormation module = moduleFormationRepository.findByIdAndFormationId(moduleId, formationId)
                .orElseThrow(() -> new ResourceNotFoundException("Module introuvable: " + moduleId));
        applyFormateur(module, formateurId);
        return toDto(moduleFormationRepository.save(module));
    }

    public void delete(Long formationId, Long moduleId) {
        ModuleFormation module = moduleFormationRepository.findByIdAndFormationId(moduleId, formationId)
                .orElseThrow(() -> new ResourceNotFoundException("Module introuvable: " + moduleId));
        moduleFormationRepository.delete(module);
    }

    private void applyFormateur(ModuleFormation module, Long formateurId) {
        if (formateurId == null) {
            module.setFormateur(null);
            return;
        }
        User formateur = userRepository.findById(formateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Formateur introuvable: " + formateurId));
        module.setFormateur(formateur);
    }

    private Formation ensureFormationExists(Long formationId) {
        return formationRepository.findById(formationId)
                .orElseThrow(() -> new ResourceNotFoundException("Formation introuvable: " + formationId));
    }

    private ModuleDto toDto(ModuleFormation module) {
        return new ModuleDto(
                module.getId(), module.getNom(), module.getFormation().getId(),
                module.getFormateur() != null ? module.getFormateur().getId() : null,
                module.getFormateur() != null ? module.getFormateur().getPrenomFr() + " " + module.getFormateur().getNomFr() : null
        );
    }
}
