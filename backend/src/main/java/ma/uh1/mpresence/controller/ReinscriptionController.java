package ma.uh1.mpresence.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.ReinscriptionCreateRequest;
import ma.uh1.mpresence.dto.ReinscriptionDto;
import ma.uh1.mpresence.dto.ReinscriptionTraitementRequest;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.service.ReinscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reinscriptions")
@RequiredArgsConstructor
public class ReinscriptionController {

    private final ReinscriptionService reinscriptionService;

    @GetMapping
    public List<ReinscriptionDto> getAll() {
        return reinscriptionService.getAll();
    }

    @GetMapping("/{id}")
    public ReinscriptionDto getOne(@PathVariable Long id) {
        return reinscriptionService.getOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReinscriptionDto create(@Valid @RequestBody ReinscriptionCreateRequest request) {
        return reinscriptionService.create(request);
    }

    @PostMapping("/{id}/traiter")
    public ReinscriptionDto traiter(@PathVariable Long id, @Valid @RequestBody ReinscriptionTraitementRequest request,
                                     @AuthenticationPrincipal User currentUser) {
        return reinscriptionService.traiter(id, request, currentUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reinscriptionService.delete(id);
    }
}
