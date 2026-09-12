package ma.uh1.mpresence.service;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.FormateurDto;
import ma.uh1.mpresence.dto.FormateurUpsertRequest;
import ma.uh1.mpresence.entity.Role;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.exception.ResourceNotFoundException;
import ma.uh1.mpresence.repository.FormationRepository;
import ma.uh1.mpresence.repository.ModuleFormationRepository;
import ma.uh1.mpresence.repository.RoleRepository;
import ma.uh1.mpresence.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Gestion globale des comptes Formateur depuis le back-office : création,
 * modification, suppression — distinct de la gestion des Doctorants.
 */
@Service
@RequiredArgsConstructor
public class FormateurService {

    private static final String ROLE_FORMATEUR = "FORMATEUR";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FormationRepository formationRepository;
    private final ModuleFormationRepository moduleFormationRepository;
    private final PasswordEncoder passwordEncoder;

    public List<FormateurDto> getAll() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRoles().stream().anyMatch(r -> ROLE_FORMATEUR.equals(r.getName())))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public FormateurDto getOne(Long id) {
        return toDto(findEntity(id));
    }

    public FormateurDto create(FormateurUpsertRequest request) {
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Le mot de passe est requis à la création");
        }
        User user = new User();
        applyRequest(user, request);
        user.setRoles(Set.of(formateurRole()));
        return toDto(userRepository.save(user));
    }

    public FormateurDto update(Long id, FormateurUpsertRequest request) {
        User user = findEntity(id);
        applyRequest(user, request);
        return toDto(userRepository.save(user));
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Formateur introuvable: " + id);
        }
        userRepository.deleteById(id);
    }

    private void applyRequest(User user, FormateurUpsertRequest request) {
        user.setNomFr(request.getNomFr());
        user.setPrenomFr(request.getPrenomFr());
        user.setCin(request.getCin());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAge(request.getAge());
        user.setSexe(request.getSexe());
        user.setAddress(request.getAddress());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
    }

    private Role formateurRole() {
        return roleRepository.findByName(ROLE_FORMATEUR)
                .orElseThrow(() -> new IllegalStateException("Rôle FORMATEUR introuvable"));
    }

    private User findEntity(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Formateur introuvable: " + id));
    }

    private FormateurDto toDto(User u) {
        return new FormateurDto(
                u.getId(), u.getCin(), u.getNomFr(), u.getPrenomFr(), u.getEmail(), u.getPhone(),
                u.getAge(), u.getSexe(), u.getAddress(), u.getPhoto(), u.isEnabled(),
                formationRepository.countByFormateurId(u.getId()),
                moduleFormationRepository.countByFormateurId(u.getId())
        );
    }
}
