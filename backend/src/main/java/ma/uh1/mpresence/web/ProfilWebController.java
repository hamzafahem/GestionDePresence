package ma.uh1.mpresence.web;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.dto.UserProfileUpdateRequest;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.service.UserProfileService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ProfilWebController {

    private final UserProfileService userProfileService;

    @GetMapping("/backoffice/profil")
    public String profil(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("profile", userProfileService.getProfile(user));
        return "backoffice/profil";
    }

    @PostMapping("/backoffice/profil")
    public String updateProfil(@AuthenticationPrincipal User user,
                                @ModelAttribute UserProfileUpdateRequest request,
                                Model model) {
        model.addAttribute("profile", userProfileService.updateProfile(user, request));
        model.addAttribute("saved", true);
        return "backoffice/profil";
    }
}
