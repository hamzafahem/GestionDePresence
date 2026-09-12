package ma.uh1.mpresence.service;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.UserProfileDto;
import ma.uh1.mpresence.dto.UserProfileUpdateRequest;
import ma.uh1.mpresence.dto.UserSettingsDto;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.exception.BadCredentialsApiException;
import ma.uh1.mpresence.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    public UserProfileDto getProfile(User user) {
        return toProfileDto(user);
    }

    public UserProfileDto updateProfile(User user, UserProfileUpdateRequest request) {
        if (request.getNomFr() != null) user.setNomFr(request.getNomFr());
        if (request.getPrenomFr() != null) user.setPrenomFr(request.getPrenomFr());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPhoto() != null) user.setPhoto(request.getPhoto());
        if (request.getAge() != null) user.setAge(request.getAge());
        if (request.getSexe() != null) user.setSexe(request.getSexe());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        if (request.getPhone() != null) user.setPhone(request.getPhone());

        return toProfileDto(userRepository.save(user));
    }

    public UserProfileDto updatePhoto(User user, MultipartFile file) {
        if (user.getPhoto() != null) {
            fileStorageService.delete(user.getPhoto());
        }
        user.setPhoto(fileStorageService.store(file));
        return toProfileDto(userRepository.save(user));
    }

    public UserProfileDto deletePhoto(User user) {
        fileStorageService.delete(user.getPhoto());
        user.setPhoto(null);
        return toProfileDto(userRepository.save(user));
    }

    public UserSettingsDto getSettings(User user) {
        return toSettingsDto(user);
    }

    public UserSettingsDto updateSettings(User user, UserSettingsDto request) {
        user.getSettings().setSmsEmailNotification(request.isSmsEmailNotification());
        user.getSettings().setAppNotification(request.isAppNotification());
        user.getSettings().setSecurityEnabled(request.isSecurityEnabled());
        user.getSettings().setAccountDeactivated(request.isAccountDeactivated());

        return toSettingsDto(userRepository.save(user));
    }

    public void changePassword(User user, String oldPassword, String newPassword) {
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadCredentialsApiException("Ancien mot de passe incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private UserProfileDto toProfileDto(User user) {
        return new UserProfileDto(
                user.getId(), user.getCin(), user.getNomFr(), user.getPrenomFr(),
                user.getEmail(), user.getPhoto(), user.getAge(), user.getSexe(),
                user.getAddress(), user.getPhone()
        );
    }

    private UserSettingsDto toSettingsDto(User user) {
        return new UserSettingsDto(
                user.getSettings().isSmsEmailNotification(),
                user.getSettings().isAppNotification(),
                user.getSettings().isSecurityEnabled(),
                user.getSettings().isAccountDeactivated()
        );
    }
}
