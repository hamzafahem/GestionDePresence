package ma.uh1.mpresence.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.FormationInterneDto;
import ma.uh1.mpresence.dto.FormationInterneUpsertRequest;
import ma.uh1.mpresence.service.FormationInterneService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/formations-internes")
@RequiredArgsConstructor
public class FormationInterneController {

    private final FormationInterneService formationInterneService;

    @GetMapping
    public List<FormationInterneDto> getAll() {
        return formationInterneService.getAll();
    }

    @GetMapping("/{id}")
    public FormationInterneDto getOne(@PathVariable Long id) {
        return formationInterneService.getOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormationInterneDto create(@Valid @RequestBody FormationInterneUpsertRequest request) {
        return formationInterneService.create(request);
    }

    @PutMapping("/{id}")
    public FormationInterneDto update(@PathVariable Long id, @Valid @RequestBody FormationInterneUpsertRequest request) {
        return formationInterneService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        formationInterneService.delete(id);
    }

    @PostMapping("/{id}/participants/{doctorantId}")
    public FormationInterneDto inscrire(@PathVariable Long id, @PathVariable Long doctorantId) {
        return formationInterneService.inscrire(id, doctorantId);
    }

    @DeleteMapping("/{id}/participants/{doctorantId}")
    public FormationInterneDto desinscrire(@PathVariable Long id, @PathVariable Long doctorantId) {
        return formationInterneService.desinscrire(id, doctorantId);
    }
}
