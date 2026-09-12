package ma.uh1.mpresence.web;

import lombok.RequiredArgsConstructor;
import ma.uh1.mpresence.entity.User;
import ma.uh1.mpresence.service.DashboardService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/backoffice/dashboard")
    public String dashboard(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("currentUser", user);
        model.addAttribute("stats", dashboardService.getStats());
        model.addAttribute("chart", dashboardService.getWeeklyActivity());
        model.addAttribute("activities", dashboardService.getRecentActivity());
        return "backoffice/dashboard";
    }
}
