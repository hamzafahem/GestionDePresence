package ma.uh1.mpresence.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.FormationDto;
import ma.uh1.mpresence.dto.FormationListResponse;
import ma.uh1.mpresence.dto.FormationUpsertRequest;
import ma.uh1.mpresence.service.FormationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/administration/formations")
@RequiredArgsConstructor
public class FormationController {

    private final FormationService formationService;

    /**
     * Correspond à QrService.getFts() côté front — réponse enveloppée dans "items".
     */
    @GetMapping
    public FormationListResponse getAll() {
        return new FormationListResponse(formationService.getAll());
    }

    /**
     * Correspond à QrService.getOne(id) côté front.
     */
    @GetMapping("/getOne/{id}")
    public FormationDto getOne(@PathVariable Long id) {
        return formationService.getOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormationDto create(@Valid @RequestBody FormationUpsertRequest request) {
        return formationService.create(request);
    }

    @PutMapping("/{id}")
    public FormationDto update(@PathVariable Long id, @Valid @RequestBody FormationUpsertRequest request) {
        return formationService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        formationService.delete(id);
    }
}
