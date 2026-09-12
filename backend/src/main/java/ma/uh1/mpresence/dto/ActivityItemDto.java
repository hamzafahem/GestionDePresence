package ma.uh1.mpresence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ActivityItemDto {
    private String iconClass;
    private String colorClass;
    private String title;
    private String description;
    private LocalDateTime dateCreation;
}
