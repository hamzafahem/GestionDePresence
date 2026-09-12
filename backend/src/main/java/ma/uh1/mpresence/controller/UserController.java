package ma.uh1.mpresence.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.ChangePasswordRequest;
import ma.uh1.mpresence.dto.UserProfileDto;
import ma.uh1.mpresence.dto.UserProfileUpdateRequest;
import ma.uh1.mpresence.dto.UserSettingsDto;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.service.UserProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Utilisateur courant : correspond à profil (GET/PUT /users/me), setting
 * (GET/PUT /users/me/settings) et change-password côté front — toutes ces
 * pages sont aujourd'hui hardcodées/statiques et doivent être reconnectées
 * ici.
 */
@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
public class UserController {

    private final UserProfileService userProfileService;

    @GetMapping
    public UserProfileDto getProfile(@AuthenticationPrincipal User user) {
        return userProfileService.getProfile(user);
    }

    @PutMapping
    public UserProfileDto updateProfile(@AuthenticationPrincipal User user, @RequestBody UserProfileUpdateRequest request) {
        return userProfileService.updateProfile(user, request);
    }

    @PostMapping(value = "/photo", consumes = "multipart/form-data")
    public UserProfileDto uploadPhoto(@AuthenticationPrincipal User user, @RequestParam("file") MultipartFile file) {
        return userProfileService.updatePhoto(user, file);
    }

    @DeleteMapping("/photo")
    public UserProfileDto deletePhoto(@AuthenticationPrincipal User user) {
        return userProfileService.deletePhoto(user);
    }

    @GetMapping("/settings")
    public UserSettingsDto getSettings(@AuthenticationPrincipal User user) {
        return userProfileService.getSettings(user);
    }

    @PutMapping("/settings")
    public UserSettingsDto updateSettings(@AuthenticationPrincipal User user, @RequestBody UserSettingsDto request) {
        return userProfileService.updateSettings(user, request);
    }

    @PostMapping("/change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@AuthenticationPrincipal User user, @Valid @RequestBody ChangePasswordRequest request) {
        userProfileService.changePassword(user, request.getOldPassword(), request.getNewPassword());
    }
}
