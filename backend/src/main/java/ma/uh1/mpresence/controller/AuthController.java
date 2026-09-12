package ma.uh1.mpresence.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.LoginRequest;
import ma.uh1.mpresence.dto.LoginResponse;
import ma.uh1.mpresence.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Correspond à AuthtestService.auth() côté front (sign-in.page.ts).
     */
    @PostMapping("/adminLogin")
    public LoginResponse adminLogin(@Valid @RequestBody LoginRequest request) {
        return authService.login(request.getCin(), request.getPassword());
    }
}
