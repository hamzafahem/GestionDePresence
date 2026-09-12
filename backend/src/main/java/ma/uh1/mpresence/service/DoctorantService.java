package ma.uh1.mpresence.service;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.DoctorantDto;
import ma.uh1.mpresence.dto.DoctorantUpsertRequest;
import ma.uh1.mpresence.dto.UserSettingsDto;
import ma.uh1.mpresence.entity.Doctorant;
import ma.uh1.mpresence.entity.Formation;
import ma.uh1.mpresence.entity.StatutDoctorant;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.exception.BadCredentialsApiException;
import ma.uh1.mpresence.exception.ResourceNotFoundException;
import ma.uh1.mpresence.repository.DoctorantRepository;
import ma.uh1.mpresence.repository.FormationRepository;
import ma.uh1.mpresence.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorantService {

    private final DoctorantRepository doctorantRepository;
    private final FormationRepository formationRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<DoctorantDto> getAll() {
        return doctorantRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public DoctorantDto getOne(Long id) {
        return toDto(findEntity(id));
    }

    public DoctorantDto create(DoctorantUpsertRequest request) {
        Doctorant doctorant = new Doctorant();
        applyRequest(doctorant, request);
        return toDto(doctorantRepository.save(doctorant));
    }

    public DoctorantDto update(Long id, DoctorantUpsertRequest request) {
        Doctorant doctorant = findEntity(id);
        applyRequest(doctorant, request);
        return toDto(doctorantRepository.save(doctorant));
    }

    public void delete(Long id) {
        if (!doctorantRepository.existsById(id)) {
            throw new ResourceNotFoundException("Doctorant introuvable: " + id);
        }
        doctorantRepository.deleteById(id);
    }

    public DoctorantDto affecterEncadrant(Long id, Long formateurId) {
        Doctorant doctorant = findEntity(id);
        User formateur = userRepository.findById(formateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Formateur introuvable: " + formateurId));
        doctorant.setEncadrant(formateur);
        return toDto(doctorantRepository.save(doctorant));
    }

    public DoctorantDto retirerEncadrant(Long id) {
        Doctorant doctorant = findEntity(id);
        doctorant.setEncadrant(null);
        return toDto(doctorantRepository.save(doctorant));
    }

    public void changePassword(Doctorant doctorant, String oldPassword, String newPassword) {
        if (doctorant.getPassword() == null || !passwordEncoder.matches(oldPassword, doctorant.getPassword())) {
            throw new BadCredentialsApiException("Ancien mot de passe incorrect");
        }
        doctorant.setPassword(passwordEncoder.encode(newPassword));
        doctorantRepository.save(doctorant);
    }

    public UserSettingsDto getSettings(Doctorant doctorant) {
        return toSettingsDto(doctorant);
    }

    public UserSettingsDto updateSettings(Doctorant doctorant, UserSettingsDto request) {
        doctorant.getSettings().setSmsEmailNotification(request.isSmsEmailNotification());
        doctorant.getSettings().setAppNotification(request.isAppNotification());
        doctorant.getSettings().setSecurityEnabled(request.isSecurityEnabled());
        doctorant.getSettings().setAccountDeactivated(request.isAccountDeactivated());
        return toSettingsDto(doctorantRepository.save(doctorant));
    }

    private UserSettingsDto toSettingsDto(Doctorant d) {
        return new UserSettingsDto(
                d.getSettings().isSmsEmailNotification(),
                d.getSettings().isAppNotification(),
                d.getSettings().isSecurityEnabled(),
                d.getSettings().isAccountDeactivated()
        );
    }

    private Doctorant findEntity(Long id) {
        return doctorantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctorant introuvable: " + id));
    }

    private void applyRequest(Doctorant doctorant, DoctorantUpsertRequest request) {
        doctorant.setNom(request.getNom());
        doctorant.setPrenom(request.getPrenom());
        doctorant.setEmail(request.getEmail());
        doctorant.setMobile(request.getMobile());
        doctorant.setCin(request.getCin());
        doctorant.setCne(request.getCne());
        doctorant.setCodeApogee(request.getCodeApogee());
        doctorant.setFiliere(request.getFiliere());
        doctorant.setNationalite(request.getNationalite() != null && !request.getNationalite().isBlank()
                ? request.getNationalite() : "Marocaine");
        doctorant.setPhoto(request.getPhoto());
        doctorant.setDateNaissance(request.getDateNaissance());
        doctorant.setDetails(request.getDetails());
        doctorant.setStatut(request.getStatut() != null ? StatutDoctorant.valueOf(request.getStatut()) : StatutDoctorant.ACTIF);

        if (request.getFormationId() != null) {
            Formation formation = formationRepository.findById(request.getFormationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Formation introuvable: " + request.getFormationId()));
            doctorant.setFormation(formation);
        } else {
            doctorant.setFormation(null);
        }
    }

    private DoctorantDto toDto(Doctorant d) {
        return new DoctorantDto(
                d.getId(), d.getNom(), d.getPrenom(), d.getEmail(), d.getMobile(),
                d.getCin(), d.getCne(), d.getCodeApogee(), d.getFiliere(), d.getNationalite(), d.getPhoto(),
                d.getDateNaissance(), d.getDetails(),
                d.getFormation() != null ? d.getFormation().getId() : null,
                d.getFormation() != null ? d.getFormation().getIntitule() : null,
                d.getFormation() != null && d.getFormation().getAnneeUniversitaire() != null
                        ? d.getFormation().getAnneeUniversitaire().getLibelle() : null,
                d.getStatut().name(),
                d.getEncadrant() != null ? d.getEncadrant().getId() : null,
                d.getEncadrant() != null ? d.getEncadrant().getPrenomFr() + " " + d.getEncadrant().getNomFr() : null,
                d.getDateCreation()
        );
    }
}
