package ma.uh1.mpresence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Correspond aux toggles de src/app/page/page/page/setting.
 */
@Getter
@Setter
@AllArgsConstructor
public class UserSettingsDto {
    private boolean smsEmailNotification;
    private boolean appNotification;
    private boolean securityEnabled;
    private boolean accountDeactivated;
}
