package ma.uh1.mpresence.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.DoctorantDto;
import ma.uh1.mpresence.dto.DoctorantUpsertRequest;
import ma.uh1.mpresence.service.DoctorantService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD complet correspondant à src/app/doctorant/* côté front (create, edit,
 * delete, doct — actuellement du code mort à reconnecter à ces endpoints).
 */
@RestController
@RequestMapping("/doctorants")
@RequiredArgsConstructor
public class DoctorantController {

    private final DoctorantService doctorantService;

    @GetMapping
    public List<DoctorantDto> getAll() {
        return doctorantService.getAll();
    }

    @GetMapping("/{id}")
    public DoctorantDto getOne(@PathVariable Long id) {
        return doctorantService.getOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DoctorantDto create(@Valid @RequestBody DoctorantUpsertRequest request) {
        return doctorantService.create(request);
    }

    @PutMapping("/{id}")
    public DoctorantDto update(@PathVariable Long id, @Valid @RequestBody DoctorantUpsertRequest request) {
        return doctorantService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        doctorantService.delete(id);
    }

    @PostMapping("/{id}/encadrant/{formateurId}")
    public DoctorantDto affecterEncadrant(@PathVariable Long id, @PathVariable Long formateurId) {
        return doctorantService.affecterEncadrant(id, formateurId);
    }

    @DeleteMapping("/{id}/encadrant")
    public DoctorantDto retirerEncadrant(@PathVariable Long id) {
        return doctorantService.retirerEncadrant(id);
    }
}
