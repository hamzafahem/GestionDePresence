package ma.uh1.mpresence.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.DemandeCreateRequest;
import ma.uh1.mpresence.dto.DemandeDto;
import ma.uh1.mpresence.dto.DemandeTraitementRequest;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.service.DemandeService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/demandes")
@RequiredArgsConstructor
public class DemandeController {

    private final DemandeService demandeService;

    @GetMapping
    public List<DemandeDto> getAll() {
        return demandeService.getAll();
    }

    @GetMapping("/{id}")
    public DemandeDto getOne(@PathVariable Long id) {
        return demandeService.getOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DemandeDto create(@Valid @RequestBody DemandeCreateRequest request) {
        return demandeService.create(request);
    }

    @PostMapping("/{id}/traiter")
    public DemandeDto traiter(@PathVariable Long id, @Valid @RequestBody DemandeTraitementRequest request,
                               @AuthenticationPrincipal User currentUser) {
        return demandeService.traiter(id, request, currentUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        demandeService.delete(id);
    }
}
