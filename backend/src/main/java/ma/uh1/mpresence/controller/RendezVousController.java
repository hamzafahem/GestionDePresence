package ma.uh1.mpresence.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.RendezVousDto;
import ma.uh1.mpresence.dto.RendezVousUpsertRequest;
import ma.uh1.mpresence.service.RendezVousService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rendezvous")
@RequiredArgsConstructor
public class RendezVousController {

    private final RendezVousService rendezVousService;

    @GetMapping
    public List<RendezVousDto> getAll() {
        return rendezVousService.getAll();
    }

    @GetMapping("/{id}")
    public RendezVousDto getOne(@PathVariable Long id) {
        return rendezVousService.getOne(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RendezVousDto create(@Valid @RequestBody RendezVousUpsertRequest request) {
        return rendezVousService.create(request);
    }

    @PutMapping("/{id}")
    public RendezVousDto update(@PathVariable Long id, @Valid @RequestBody RendezVousUpsertRequest request) {
        return rendezVousService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        rendezVousService.delete(id);
    }
}
