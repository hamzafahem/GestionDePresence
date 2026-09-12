package ma.uh1.mpresence.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.ModuleDto;
import ma.uh1.mpresence.dto.ModuleUpsertRequest;
import ma.uh1.mpresence.service.ModuleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Modules d'une formation (nested resource) — remplace l'usage détourné de
 * GET /administration/formations pour lister les "modules" côté front
 * (page/menu/forN, sous-pages module-c et module-n).
 */
@RestController
@RequestMapping("/administration/formations/{formationId}/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    @GetMapping
    public List<ModuleDto> getAll(@PathVariable Long formationId) {
        return moduleService.getAllForFormation(formationId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ModuleDto create(@PathVariable Long formationId, @Valid @RequestBody ModuleUpsertRequest request) {
        return moduleService.create(formationId, request);
    }

    @PutMapping("/{moduleId}")
    public ModuleDto update(@PathVariable Long formationId, @PathVariable Long moduleId,
                             @Valid @RequestBody ModuleUpsertRequest request) {
        return moduleService.update(formationId, moduleId, request);
    }

    @DeleteMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long formationId, @PathVariable Long moduleId) {
        moduleService.delete(formationId, moduleId);
    }
}
