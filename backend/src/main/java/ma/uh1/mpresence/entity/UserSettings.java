package ma.uh1.mpresence.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Correspond aux toggles de src/app/page/page/page/setting (actuellement
 * non fonctionnels côté front, à connecter à ces champs).
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class UserSettings {

    private boolean smsEmailNotification = true;

    private boolean appNotification = true;

    private boolean securityEnabled = false;

    private boolean accountDeactivated = false;
}
