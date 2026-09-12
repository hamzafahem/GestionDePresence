package ma.uh1.mpresence.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.PublicationDto;
import ma.uh1.mpresence.dto.PublicationUpsertRequest;
import ma.uh1.mpresence.service.PublicationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Regroupe Articles, Proceedings et Brevets (voir PublicationRecherche).
 */
@RestController
@RequestMapping("/publications")
@RequiredArgsConstructor
public class PublicationController {

    private final PublicationService publicationService;

    @GetMapping
    public List<PublicationDto> getAll() {
        return publicationService.getAll();
    }

    @GetMapping("/{id}")
    public PublicationDto getOne(@PathVariable Long id) {
        return publicationService.getOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PublicationDto create(@Valid @RequestBody PublicationUpsertRequest request) {
        return publicationService.create(request);
    }

    @PutMapping("/{id}")
    public PublicationDto update(@PathVariable Long id, @Valid @RequestBody PublicationUpsertRequest request) {
        return publicationService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        publicationService.delete(id);
    }
}
