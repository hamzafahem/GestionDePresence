package ma.uh1.mpresence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class NotificationDto {
    private Long id;
    private String titre;
    private String message;
    private LocalDateTime dateCreation;
    private Long doctorantId;
    private String doctorantNom;
    private boolean lu;
}
