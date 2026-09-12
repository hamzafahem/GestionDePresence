package ma.uh1.mpresence.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/backoffice/login")
    public String login() {
        return "backoffice/login";
    }
}
