package ma.uh1.mpresence.service;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.AnneeUniversitaireDto;
import ma.uh1.mpresence.dto.LoginResponse;
import ma.uh1.mpresence.dto.UserDto;
import ma.uh1.mpresence.entity.AnneeUniversitaire;
import ma.uh1.mpresence.entity.Doctorant;
import ma.uh1.mpresence.entity.Permission;
import ma.uh1.mpresence.entity.Role;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.exception.BadCredentialsApiException;
import ma.uh1.mpresence.repository.AnneeUniversitaireRepository;
import ma.uh1.mpresence.repository.DoctorantRepository;
import ma.uh1.mpresence.repository.UserRepository;
import ma.uh1.mpresence.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Un seul point d'entrée de connexion pour toute l'app mobile : essaie
 * d'abord un compte Admin/Formateur (CIN), puis un Doctorant (Code Apogée),
 * pour que le front (une seule page sign-in) n'ait rien à changer.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final DoctorantRepository doctorantRepository;
    private final AnneeUniversitaireRepository anneeUniversitaireRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(String identifiant, String rawPassword) {
        var userOpt = userRepository.findByCinIgnoreCase(identifiant);
        if (userOpt.isPresent()) {
            return loginAsUser(userOpt.get(), rawPassword);
        }

        var doctorantOpt = doctorantRepository.findByCodeApogeeIgnoreCase(identifiant);
        if (doctorantOpt.isPresent()) {
            return loginAsDoctorant(doctorantOpt.get(), rawPassword);
        }

        throw new BadCredentialsApiException("Identifiant ou mot de passe incorrect");
    }

    private LoginResponse loginAsUser(User user, String rawPassword) {
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsApiException("Identifiant ou mot de passe incorrect");
        }

        String token = jwtService.generateToken(user);
        AnneeUniversitaire annee = anneeUniversitaireRepository.findByActiveTrue().orElse(null);

        return new LoginResponse(token, toUserDto(user), toAnneeDto(annee));
    }

    private LoginResponse loginAsDoctorant(Doctorant doctorant, String rawPassword) {
        if (doctorant.getPassword() == null || !passwordEncoder.matches(rawPassword, doctorant.getPassword())) {
            throw new BadCredentialsApiException("Identifiant ou mot de passe incorrect");
        }

        String token = jwtService.generateToken(doctorant);
        AnneeUniversitaire annee = anneeUniversitaireRepository.findByActiveTrue().orElse(null);

        UserDto userDto = new UserDto(
                doctorant.getId(),
                doctorant.getNom(),
                doctorant.getPrenom(),
                doctorant.getEmail(),
                doctorant.getPhoto(),
                List.of("DOCTORANT"),
                List.of()
        );

        return new LoginResponse(token, userDto, toAnneeDto(annee));
    }

    private UserDto toUserDto(User user) {
        List<String> roleNames = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
        List<String> permissionNames = user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .distinct()
                .collect(Collectors.toList());

        return new UserDto(
                user.getId(),
                user.getNomFr(),
                user.getPrenomFr(),
                user.getEmail(),
                user.getPhoto(),
                roleNames,
                permissionNames
        );
    }

    private AnneeUniversitaireDto toAnneeDto(AnneeUniversitaire annee) {
        if (annee == null) {
            return null;
        }
        return new AnneeUniversitaireDto(annee.getId(), annee.getLibelle(), annee.getDateDebut(), annee.getDateFin());
    }
}
