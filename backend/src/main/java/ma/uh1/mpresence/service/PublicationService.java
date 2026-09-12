package ma.uh1.mpresence.service;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.PublicationDto;
import ma.uh1.mpresence.dto.PublicationUpsertRequest;
import ma.uh1.mpresence.entity.Doctorant;
import ma.uh1.mpresence.entity.PublicationRecherche;
import ma.uh1.mpresence.entity.TypePublication;
import ma.uh1.mpresence.exception.ResourceNotFoundException;
import ma.uh1.mpresence.repository.DoctorantRepository;
import ma.uh1.mpresence.repository.PublicationRechercheRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicationService {

    private final PublicationRechercheRepository publicationRepository;
    private final DoctorantRepository doctorantRepository;

    public List<PublicationDto> getAll() {
        return publicationRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public PublicationDto getOne(Long id) {
        return toDto(findEntity(id));
    }

    public PublicationDto create(PublicationUpsertRequest request) {
        PublicationRecherche publication = new PublicationRecherche();
        applyRequest(publication, request);
        return toDto(publicationRepository.save(publication));
    }

    public PublicationDto update(Long id, PublicationUpsertRequest request) {
        PublicationRecherche publication = findEntity(id);
        applyRequest(publication, request);
        return toDto(publicationRepository.save(publication));
    }

    public void delete(Long id) {
        if (!publicationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Publication introuvable: " + id);
        }
        publicationRepository.deleteById(id);
    }

    private void applyRequest(PublicationRecherche publication, PublicationUpsertRequest request) {
        Doctorant doctorant = doctorantRepository.findById(request.getDoctorantId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctorant introuvable: " + request.getDoctorantId()));

        publication.setDoctorant(doctorant);
        publication.setType(TypePublication.valueOf(request.getType()));
        publication.setTitre(request.getTitre());
        publication.setReference(request.getReference());
        publication.setDatePublication(request.getDatePublication());
        publication.setLien(request.getLien());
        publication.setDescription(request.getDescription());
    }

    private PublicationRecherche findEntity(Long id) {
        return publicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publication introuvable: " + id));
    }

    private PublicationDto toDto(PublicationRecherche p) {
        return new PublicationDto(
                p.getId(),
                p.getDoctorant().getId(), p.getDoctorant().getNom(), p.getDoctorant().getPrenom(),
                p.getType().name(), p.getTitre(), p.getReference(), p.getDatePublication(),
                p.getLien(), p.getDescription()
        );
    }
}
